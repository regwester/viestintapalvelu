package fi.vm.sade.viestintapalvelu.recovery;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fi.vm.sade.viestintapalvelu.letter.LetterBatchPDFProcessor;
import fi.vm.sade.viestintapalvelu.letter.LetterService;

@RunWith(MockitoJUnitRunner.class)
public class LetterPDFRecovererTest {

    @Mock
    private LetterBatchPDFProcessor processor;
    
    @Mock
    private LetterService service;
    
    @InjectMocks
    private LetterPDFRecoverer recoverer = new LetterPDFRecoverer();
    
    @Test
    public void retrievesUnfinishedLetterBatches() throws InterruptedException {
        recoverer.getTask().run();
        verify(service).findUnfinishedLetterBatches();
    }
    
    @Test
    public void passesUnfinishedLetterBatchesForProcessing() throws InterruptedException {
        Long batchId = 3l;
        when(service.findUnfinishedLetterBatches()).thenReturn(Arrays.asList(batchId));
        recoverer.getTask().run();
        verify(processor).processLetterBatch(batchId);
    }
    
    @Test
    public void catchesAnyExceptionThrownByProcessor() throws InterruptedException {
        when(service.findUnfinishedLetterBatches()).thenReturn(Arrays.asList((Long)7l));
        when(processor.processLetterBatch(any(Long.class))).thenThrow(new IllegalArgumentException());
        recoverer.getTask().run();
    }
    
}
