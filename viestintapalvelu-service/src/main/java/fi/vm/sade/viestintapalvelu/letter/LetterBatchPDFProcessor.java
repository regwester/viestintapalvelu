package fi.vm.sade.viestintapalvelu.letter;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;

import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;


public class LetterBatchPDFProcessor {
    
    private final ExecutorService executor;
    
    private final LetterBuilder builder;
    
    private final LetterService service;
    
    @Autowired
    LetterBatchPDFProcessor(ExecutorService executor, LetterBuilder builder, LetterService service) {
        this.executor = executor;
        this.builder = builder;
        this.service = service;
    }
    
    public void processLetterBatch(final Long letterBatchId) {
        service.updateBatchProcessingStarted(letterBatchId, LetterBatchProcess.LETTER);
        final LetterBatch batch = service.fetchById(letterBatchId);
        executor.execute(new Runnable() {

            @Override
            public void run() {
                for (LetterReceivers receiver : batch.getLetterReceivers()) {
                    
                }
            }
            
        });
    }
}
