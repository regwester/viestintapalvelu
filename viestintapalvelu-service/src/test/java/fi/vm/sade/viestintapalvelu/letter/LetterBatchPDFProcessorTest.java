package fi.vm.sade.viestintapalvelu.letter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LetterBatchPDFProcessorTest {
    
    private static final long LETTERBATCH_ID = 1l;

    @Mock
    private LetterService service;
    
    @Mock
    private LetterBuilder builder;
    
    private LetterBatchPDFProcessor processor;
    
    @Before
    public void init() {
        processor = new LetterBatchPDFProcessor(Executors.newCachedThreadPool(), builder, service);
    }
    
    @Test
    public void updatesProcessStartedOnLetterBatch() {        
        processor.processLetterBatch(LETTERBATCH_ID);
        verify(service, times(1)).updateBatchProcessingStarted(1l, LetterBatchProcess.LETTER);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void buildsPDFForAllReceivers() throws Exception {
        final int amountOfReceivers = 121;
        when(service.fetchById(LETTERBATCH_ID)).thenReturn(givenLetterBatchWithReceivers(amountOfReceivers));
        processor.processLetterBatch(LETTERBATCH_ID);
        verify(builder, timeout(100).times(amountOfReceivers)).constructPDFForLetterReceiverLetter(any(LetterReceivers.class), any(LetterBatch.class), any(Map.class), any(Map.class));
        verify(service, timeout(100).times(amountOfReceivers)).updateLetter(any(LetterReceiverLetter.class));
    }
    
    @Test
    public void updatesProcessFinishedOnLetterBatch() {
        final int amountOfReceivers = 157;
        when(service.fetchById(LETTERBATCH_ID)).thenReturn(givenLetterBatchWithReceivers(amountOfReceivers));
        processor.processLetterBatch(LETTERBATCH_ID);
        verify(service, timeout(100)).updateBatchProcessingFinished(LETTERBATCH_ID, LetterBatchProcess.LETTER);
    }

    private LetterBatch givenLetterBatchWithReceivers(final int amountOfReceivers) {
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(LETTERBATCH_ID);
        Set<LetterReceivers> receivers = batch.getLetterReceivers();
        for (int i = 0 ; i < amountOfReceivers; i++) {
            receivers.addAll(DocumentProviderTestData.getLetterReceivers(Long.valueOf(i), batch));
        }
        batch.setLetterReceivers(receivers);
        return batch;
    }
    
}
