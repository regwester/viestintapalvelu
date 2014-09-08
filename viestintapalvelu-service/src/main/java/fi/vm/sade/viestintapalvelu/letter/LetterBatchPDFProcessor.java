package fi.vm.sade.viestintapalvelu.letter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;

import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.LetterReplacement;


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
                    try {
                        builder.constructPDFForLetterReceiverLetter(receiver, batch, formReplacementMap(receiver), formReplacementMap(batch));
                        service.updateLetter(receiver.getLetterReceiverLetter());
                    } catch (Exception e) {
                        //TODO: handle
                    }
                }
                service.updateBatchProcessingFinished(letterBatchId, LetterBatchProcess.LETTER);
                
            }

            private Map<String, Object> formReplacementMap(LetterBatch batch) {
                Map<String, Object> replacements = new HashMap<String, Object>();
                for (LetterReplacement repl : batch.getLetterReplacements()) {
                    replacements.put(repl.getName(), repl.getDefaultValue());
                }
                return replacements;
            }

            private Map<String, Object> formReplacementMap(LetterReceivers receiver) {
                Map<String, Object> replacements = new HashMap<String, Object>();
                for (LetterReceiverReplacement repl : receiver.getLetterReceiverReplacement()) {
                    replacements.put(repl.getName(), repl.getDefaultValue());
                }
                return replacements;
            }
            
        });
    }
}
