package fi.vm.sade.viestintapalvelu.recovery;

import java.util.ConcurrentModificationException;
import java.util.List;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(LetterPDFRecoverer.class);
    
    @Autowired
    private LetterBatchProcessor letterPDFProcessor;
    
    @Autowired
    private LetterService letterService;
    
    @Override
    public Runnable getTask() {
        return new Runnable() {

            @Override
            public void run() {
                List<Long> unfinishedLetterBatchIds = letterService.findUnfinishedLetterBatches();
                LOGGER.info("Recovery process for unfinished letterbatches starting for " + unfinishedLetterBatchIds.size() + " letterbatches");
                for (Long letterBatchId : unfinishedLetterBatchIds) {
                    processLetterBatch(letterBatchId);
                };
                
            }

            private void processLetterBatch(Long letterBatchId) {
                try {
                    letterPDFProcessor.processLetterBatch(letterBatchId);
                } catch (ConcurrentModificationException e) {
                    LOGGER.warn("Attempted to recover processing of LetterBatch " + letterBatchId 
                            + " that was already being processed", e);
                } catch (Exception e) {
                    LOGGER.error("Unable to recover processing of letterbatch id=" + letterBatchId, e);
                }
            }
            
        };
    }
}
