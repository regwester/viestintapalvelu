package fi.vm.sade.viestintapalvelu.letter;

import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;
import org.jgroups.util.ConcurrentLinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LetterBatchPDFProcessor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final ExecutorService executorService;

    private final LetterService letterService;

    public static final int THREADS = 4;

    @Autowired
    public LetterBatchPDFProcessor(ExecutorService executorService, LetterService letterService) {
        this.executorService = executorService;
        this.letterService = letterService;
    }

    public Future<Boolean> processLetterBatch(long letterBatchId) {
        letterService.updateBatchProcessingStarted(letterBatchId, LetterBatchProcess.LETTER);
        BatchJob job = new BatchJob(letterBatchId, letterService.findLetterReceiverIdsByBatch(letterBatchId), THREADS);
        return executorService.submit(job);
    }

    /**
     * Queue per job (not global for the whole component):
     */
    protected class BatchJob implements Callable<Boolean> {
        private final long letterBatchId;
        private final int threads;
        private volatile ConcurrentLinkedBlockingQueue<Long> unprocessedLetterReceivers;
        private volatile AtomicBoolean okState = new AtomicBoolean(true);

        public BatchJob(long letterBatchId, List<Long> ids, int threads) {
            this.letterBatchId = letterBatchId;
            this.unprocessedLetterReceivers = new ConcurrentLinkedBlockingQueue<Long>(ids.size());
            this.unprocessedLetterReceivers.addAll(ids);
            this.threads = threads;
        }

        @Override
        public Boolean call() throws Exception {
            for (int i = 0; i < threads; i++) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long letterReceiverId = unprocessedLetterReceivers.poll();
                        while (okState.get() && letterReceiverId != null) {
                            // To ensure that no id is processed twice, you could verify the output of:
                            // logger.debug("Thread : " + Thread.currentThread().getId() + " processing: " + nextId);
                            try {
                                if (okState.get()) {
                                    letterService.processLetterReceiver(letterReceiverId);
                                }
                            } catch (Exception e) {
                                logger.error("Error processing LetterReceiver " + letterReceiverId + " in batch " + letterBatchId, e);
                                letterService.saveBatchErrorForReceiver(letterReceiverId, e.getMessage());
                                okState.set(false);
                            }
                            letterReceiverId = unprocessedLetterReceivers.poll();
                        }
                    }
                });
            }
            // Sync, wait for all processors:
            while (okState.get() && unprocessedLetterReceivers.peek() != null) {
                try {
                    Thread.sleep(50l);
                } catch (InterruptedException e) {
                    logger.error("Interrupted", e);
                    return false;
                }
            }
            if (okState.get()) {
                letterService.updateBatchProcessingFinished(letterBatchId, LetterBatchProcess.LETTER);
            } else {
                logger.error("Letter batch " + letterBatchId + " procsessing ended due to an error.");
            }
            return okState.get();
        }
    }

}
