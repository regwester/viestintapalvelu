package fi.vm.sade.viestintapalvelu.letter;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.MergedPdfDocument;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.template.TemplateService;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LetterBuilderTest {
    
    private LetterBuilder builder;
    
    @Mock
    private DocumentBuilder docBuilder;
    
    @Mock
    private TemplateService templateService;
    
    @Before
    public void init() throws Exception {
        builder = new LetterBuilder(docBuilder);
        Field field = builder.getClass().getDeclaredField("templateService");
        field.setAccessible(true);
        field.set(builder, templateService);
    }
    
    @Test
    public void storesPDFAsBytesIntoLetterReceiverLetter() throws Exception {
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(1l);
        LetterReceivers receiver = batch.getLetterReceivers().iterator().next();
        when(templateService.findById(any(Long.class))).thenReturn(DocumentProviderTestData.getTemplate());
        when(docBuilder.merge(any(PdfDocument.class))).thenReturn(new MergedPdfDocument());
        builder.constructPDFForLetterReceiverLetter(receiver, batch, new HashMap<String, Object>(), new HashMap<String, Object>());
        assertNotNull(receiver.getLetterReceiverLetter().getLetter());
    }
    
}
