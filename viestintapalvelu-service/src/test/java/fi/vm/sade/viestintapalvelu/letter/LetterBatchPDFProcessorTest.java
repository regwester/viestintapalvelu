package fi.vm.sade.viestintapalvelu.letter;

import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LetterBatchPDFProcessorTest {
    
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
        processor.processLetterBatch(1l);
        verify(service, times(1)).updateBatchProcessingStarted(1l, LetterBatchProcess.LETTER);
    }
    
}
