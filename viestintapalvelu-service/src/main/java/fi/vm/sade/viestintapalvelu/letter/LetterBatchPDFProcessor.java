package fi.vm.sade.viestintapalvelu.letter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;

@Component
public class LetterBatchPDFProcessor {
    
    private final ExecutorService executorService;
    
    private final LetterService letterService;
    
    private volatile List<Long> unprocessedLetterReceivers;
    
    private final static Integer THREADS = 4;
    
    private final static Integer AMOUNT_OF_LETTERS_HANDLED_ONCE = 100;
    
    @Autowired
    public LetterBatchPDFProcessor(ExecutorService executorService, LetterService letterService) {
        this.executorService = executorService;
        this.letterService = letterService;
    }
    
    public void processLetterBatch(final long letterBatchId) {
       
        letterService.updateBatchProcessingStarted(letterBatchId, LetterBatchProcess.LETTER);
        unprocessedLetterReceivers = letterService.findLetterReceiverIdsByBatch(letterBatchId);

        for (int i = 0; i < THREADS; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while(unprocessedLettersRemain()) {
                        List<Long> receiverIds = reserveIds();
                        for (Long id : receiverIds) {
                            letterService.processLetterReceiver(id);
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            });
        }
        letterService.updateBatchProcessingFinished(letterBatchId, LetterService.LetterBatchProcess.LETTER);
        
    }
    
    private boolean unprocessedLettersRemain() {
        return !unprocessedLetterReceivers.isEmpty();
    }
    
    private synchronized List<Long> reserveIds() {
        List<Long> reservedIds = new ArrayList<Long>();
        while (reservedIds.size() < AMOUNT_OF_LETTERS_HANDLED_ONCE && unprocessedLettersRemain()) {
            reservedIds.add(unprocessedLetterReceivers.get(0));
            unprocessedLetterReceivers.remove(0);
        }
        return reservedIds;
    }

}
