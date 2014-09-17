package fi.vm.sade.viestintapalvelu.letter;

import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.LetterReplacement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Component
public class LetterBatchPDFProcessor {
    
    private final ExecutorService executorService;
    
    private final LetterBuilder letterBuilder;

    private final LetterService letterService;

    @Autowired
    public LetterBatchPDFProcessor(ExecutorService executorService, LetterBuilder builder, LetterService letterService) {
        this.executorService = executorService;
        this.letterBuilder = builder;
        this.letterService = letterService;
    }
    
    public void processLetterBatch(final long letterBatchId) {
        letterService.updateBatchProcessingStarted(letterBatchId, LetterBatchProcess.LETTER);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                letterService.runBatch(letterBatchId);
                letterService.updateBatchProcessingFinished(letterBatchId, LetterService.LetterBatchProcess.LETTER);
            }
        });
    }
}
