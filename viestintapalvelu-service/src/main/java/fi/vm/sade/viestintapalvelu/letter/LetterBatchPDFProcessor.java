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
    LetterBatchPDFProcessor(ExecutorService executorService, LetterBuilder builder, LetterService letterService) {
        this.executorService = executorService;
        this.letterBuilder = builder;
        this.letterService = letterService;
    }
    
    public void processLetterBatch(final Long letterBatchId) {
        letterService.updateBatchProcessingStarted(letterBatchId, LetterBatchProcess.LETTER);
        final LetterBatch batch = letterService.fetchById(letterBatchId);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (batch != null) {
                    System.out.println("batch.getLetterReceivers().size() = " + batch.getLetterReceivers().size());
                    for (LetterReceivers receiver : batch.getLetterReceivers()) {
                        try {
                            letterBuilder.constructPDFForLetterReceiverLetter(receiver, batch, formReplacementMap(receiver), formReplacementMap(batch));
                            letterService.updateLetter(receiver.getLetterReceiverLetter());
                        } catch (Exception e) {
                            //TODO: handle
                        }
                    }
                }
                letterService.updateBatchProcessingFinished(letterBatchId, LetterBatchProcess.LETTER);

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
