package fi.vm.sade.viestintapalvelu.letter;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;
import javax.inject.Singleton;

import org.jgroups.util.ConcurrentLinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;
import fi.vm.sade.viestintapalvelu.letter.impl.LetterServiceImpl;

@Component
@Singleton
public class LetterBatchPDFProcessor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name="batchJobExecutorService")
    private ExecutorService batchJobExecutorService;
    @Resource(name="letterReceiverExecutorService")
    private ExecutorService letterReceiverExecutorService;
    @Value("#{poolSizes['threadsPerBatchJob'] != null ? poolSizes['threadsPerBatchJob'] : 4}")
    private int letterBatchJobThreadCount=4;

    private volatile Set<Job<?>> jobsBeingProcessed = new HashSet<Job<?>>();

    @Autowired
    private LetterService letterService;

    public LetterBatchPDFProcessor() {
    }

    public LetterBatchPDFProcessor(ExecutorService batchJobExecutorService,
                                   ExecutorService letterReceiverExecutorService,
                                   LetterServiceImpl letterService) {
        this.batchJobExecutorService = batchJobExecutorService;
        this.letterReceiverExecutorService = letterReceiverExecutorService;
        this.letterService = letterService;
    }

    public Future<Boolean> processLetterBatch(long letterBatchId) {
        letterService.updateBatchProcessingStarted(letterBatchId, LetterBatchProcess.LETTER);

        LetterReceiverJob job = new LetterReceiverJob(letterBatchId);
        reserveJob(job);
        BatchJob batchJob = new BatchJob(new JobDescription<LetterReceiverProcessable>(job,
            receiverProcessablesForIds(letterService.findUnprocessedLetterReceiverIdsByBatch(letterBatchId))),
            letterBatchJobThreadCount);
        return batchJobExecutorService.submit(batchJob);
    }

    /// For testing:
    public boolean isProcessingLetterBatch(long letterBatchId) {
        return jobsBeingProcessed.contains(new LetterReceiverJob(letterBatchId));
    }

    private synchronized void reserveJob(Job<?> job) {
        if (!jobsBeingProcessed.add(job)) {
            throw new ConcurrentModificationException("Trying to process same job "+job+" more than once");
        }
        logger.info("Reserved job={}", job);
    }

    /**
     * A processable for a given job. Implementation specific.
     */
    public static interface Processable extends Serializable {
    }

    /**
     * MUST implement {@link Object#equals(Object)} and {@link Object#hashCode()}
     *
     * @param <T> the type of Processable this Job is cabable of handling
     */
    public static interface Job<T extends Processable> {
        /**
         * @param processable to process
         * @throws Exception
         */
        void process(T processable) throws Exception;

        /**
         * @param e exception occured
         * @param processable the processable with which the exception occured
         */
        void handleFailure(Exception e, T processable);

        /**
         * @return the possible next job to do
         */
        Optional<JobDescription<?>> jobFinished();
    }

    /**
     * Describes a job and it's processables
     *
     * @param <T> the type of processable for the job
     */
    public static class JobDescription<T extends Processable> {
        private final Job<T> job;
        private final List<T> processables;

        public JobDescription(Job<T> job, List<T> processables) {
            this.job = job;
            this.processables = processables;
        }

        public Job<T> getJob() {
            return job;
        }

        public List<T> getProcessables() {
            return processables;
        }
    }

    public static List<LetterReceiverProcessable> receiverProcessablesForIds(List<Long> ids) {
        List<LetterReceiverProcessable> processables = new ArrayList<LetterReceiverProcessable>();
        for (Long id : ids) {
            processables.add(new LetterReceiverProcessable(id));
        }
        return processables;
    }

    public static class LetterReceiverProcessable implements Processable {
        private Long id;

        public LetterReceiverProcessable(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        @Override
        public String toString() {
            return "LetterBatchReceiver="+id;
        }
    }

    /**
     * LETTER type job
     */
    public class LetterReceiverJob implements Job<LetterReceiverProcessable> {
        private long letterBatchId;

        public LetterReceiverJob(long letterBatchId) {
            this.letterBatchId = letterBatchId;
        }

        public long getLetterBatchId() {
            return letterBatchId;
        }

        @Override
        public void process(LetterReceiverProcessable letterReceiverProcessable) throws Exception {
            letterService.processLetterReceiver(letterReceiverProcessable.getId());
        }

        @Override
        public void handleFailure(Exception e, LetterReceiverProcessable processable) {
            letterService.saveBatchErrorForReceiver(processable.getId(), e.getMessage());
        }

        @Override
        public Optional<JobDescription<?>> jobFinished() {
            Optional<LetterBatchProcess> nextToDo = letterService
                    .updateBatchProcessingFinished(letterBatchId, LetterBatchProcess.LETTER);
            if (nextToDo.isPresent()) {
                // TODO
//                return new JobDescription<>()
            }
            return Optional.absent();
        }

        @Override
        public String toString() {
            return "LetterBatch="+this.letterBatchId+" LETTER processsing";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof LetterReceiverJob)) {
                return false;
            }
            LetterReceiverJob that = (LetterReceiverJob) o;
            if (letterBatchId != that.letterBatchId) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return (int) (letterBatchId ^ (letterBatchId >>> 32));
        }
    }

    /**
     * Queue per job
     */
    protected class BatchJob<T extends Processable> implements Callable<Boolean> {
        private final int threads;
        private final JobDescription<T> jobDescription;
        private volatile ConcurrentLinkedBlockingQueue<T> unprocessed;
        private volatile AtomicBoolean okState = new AtomicBoolean(true);

        public BatchJob(JobDescription<T> jobDescription, int threads) {
            this.jobDescription = jobDescription;
            this.unprocessed = new ConcurrentLinkedBlockingQueue<T>(jobDescription.getProcessables().size());
            this.unprocessed.addAll(jobDescription.getProcessables());
            this.threads = threads;
        }

        @Override
        public Boolean call() throws Exception {
            final CountDownLatch latch = new CountDownLatch(threads);
            for (int i = 0; i < threads; i++) {
                letterReceiverExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        T nextProcessable = unprocessed.poll();
                        while (okState.get() && nextProcessable != null) {
                            // To ensure that no id is processed twice, you could verify the output of:
                            // logger.debug("Thread : " + Thread.currentThread().getId() + " processing: " + nextId);
                            try {
                                if (okState.get()) {
                                    jobDescription.getJob().process(nextProcessable);
                                }
                            } catch (Exception e) {
                                logger.error("Error processing processable " + nextProcessable + " in job "
                                        + jobDescription.getJob(), e);
                                jobDescription.getJob().handleFailure(e, nextProcessable);
                                okState.set(false);
                            }
                            nextProcessable = unprocessed.poll();
                        }
                        latch.countDown();
                    }
                });
            }
            // Sync, wait for all processors finnish:
            latch.await();

            if (okState.get()) {
                logger.info("Job ended successfully: {}", jobDescription.getJob());
                Optional<JobDescription<?>> nextJob = jobDescription.getJob().jobFinished();
                if (nextJob.isPresent()) {
                    JobDescription<?> job = nextJob.get();
                    logger.info("Next processing: {}", job);
                    @SuppressWarnings({"unchecked","rawtypes"})
                    BatchJob<?> batchJob = new BatchJob(job, this.threads);
                    batchJobExecutorService.submit(batchJob);
                }
            } else {
                logger.error("Job " + jobDescription.getJob() + " procsessing ended due to an error.");
            }

            if (!jobsBeingProcessed.remove(jobDescription.getJob())) {
                logger.warn("Did not remove processing state of job={}", jobDescription.getJob());
            }

            return okState.get();
        }
    }

    public void setLetterBatchJobThreadCount(int letterBatchJobThreadCount) {
        this.letterBatchJobThreadCount = letterBatchJobThreadCount;
    }

    public int getLetterBatchJobThreadCount() {
        return letterBatchJobThreadCount;
    }
}
