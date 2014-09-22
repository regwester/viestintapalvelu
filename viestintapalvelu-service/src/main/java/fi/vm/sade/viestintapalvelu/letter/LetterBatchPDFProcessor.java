package fi.vm.sade.viestintapalvelu.letter;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.jgroups.util.ConcurrentLinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;

@Component
public class LetterBatchPDFProcessor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final ExecutorService executorService;

    private final LetterService letterService;

    private final static Integer THREADS = 4;

    @Autowired
    public LetterBatchPDFProcessor(ExecutorService executorService, LetterService letterService) {
        this.executorService = executorService;
        this.letterService = letterService;
    }

    public void processLetterBatch(long letterBatchId) {
        letterService.updateBatchProcessingStarted(letterBatchId, LetterBatchProcess.LETTER);
        BatchJob job = new BatchJob(letterBatchId, letterService.findLetterReceiverIdsByBatch(letterBatchId), THREADS);
        executorService.submit(job);
    }

    /**
     * Queue per job (not global for the whole component):
     */
    protected class BatchJob implements Runnable {
        private final long letterBatchId;
        private final int threads;
        private volatile ConcurrentLinkedBlockingQueue<Long> unprocessedLetterReceivers;

        public BatchJob(long letterBatchId, List<Long> ids, int threads) {
            this.letterBatchId = letterBatchId;
            this.unprocessedLetterReceivers = new ConcurrentLinkedBlockingQueue<Long>(ids.size());
            this.unprocessedLetterReceivers.addAll(ids);
            this.threads = threads;
        }

        @Override
        public void run() {
            for (int i = 0; i < threads; i++) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long nextId = unprocessedLetterReceivers.poll();
                        while (nextId != null) {
                            letterService.processLetterReceiver(nextId);
                            nextId = unprocessedLetterReceivers.poll();
                        }
                    }
                });
            }
            // Sync, wait for all processors:
            while (unprocessedLetterReceivers.peek() != null) {
                try {
                    Thread.sleep(50l);
                } catch (InterruptedException e) {
                    logger.error("Interrupted", e);
                    return;
                }
            }
            letterService.updateBatchProcessingFinished(letterBatchId, LetterBatchProcess.LETTER);
        }
    }

}
