package fi.vm.sade.viestintapalvelu.recovery;

import java.util.ConcurrentModificationException;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.letter.LetterBatchProcessor;
import fi.vm.sade.viestintapalvelu.letter.LetterService;

@Singleton
@Component
public class LetterPDFRecoverer implements Recoverer {
    private static final Logger logger = LoggerFactory.getLogger(LetterPDFRecoverer.class);
    
    @Autowired
    private LetterBatchProcessor letterPDFProcessor;
    
    @Autowired
    private LetterService letterService;
    
    @Override
    public Runnable getTask() {
        return new Runnable() {

            @Override
            public void run() {
                for (Long letterBatchId : letterService.findUnfinishedLetterBatches()) {
                    processLetterBatch(letterBatchId);
                };
                
            }

            private void processLetterBatch(Long letterBatchId) {
                try {
                    letterPDFProcessor.processLetterBatch(letterBatchId);
                } catch (ConcurrentModificationException e) {
                    logger.warn("Attempted to recover processing of LetterBatch " + letterBatchId 
                            + " that was already being processed", e);
                } catch (Exception e) {
                    logger.error("Unable to recover processing of letterbatch id=" + letterBatchId, e);
                }
            }
            
        };
    }
}
