package fi.vm.sade.viestintapalvelu.letter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;

@RunWith(MockitoJUnitRunner.class)
public class LetterBuilderTest {
    
    private LetterBuilder builder;
    
    @Mock
    private DocumentBuilder docBuilder;
    
    @Before
    public void init() {
        builder = new LetterBuilder(docBuilder);
    }
    
    @Test
    public void storesPDFAsBytesIntoLetterReceiverLetter() {
        
    }
    
}
