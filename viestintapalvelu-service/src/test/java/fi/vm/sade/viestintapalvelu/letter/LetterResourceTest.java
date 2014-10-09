package fi.vm.sade.viestintapalvelu.letter;

import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchDto;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterBatchDetails;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response.Status;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


@ContextConfiguration(locations = "/test-application-context.xml")
@RunWith(MockitoJUnitRunner.class)
public class LetterResourceTest {
    
    private static final Long LETTERBATCH_ID = 1l;

    private LetterResource resource;
    
    @Mock
    private LetterService service;
    
    @Mock
    private LetterBuilder builder;
    
    @Mock
    private LetterBatchProcessor processor;
    
    @Before
    public void init() throws Exception {
        resource = new LetterResource();
        injectObject("letterService", service);
        injectObject("letterBuilder", builder);
        injectObject("letterPDFProcessor", processor);
        when(service.createLetter(any(AsyncLetterBatchDto.class)))
                .thenReturn(DocumentProviderTestData.getLetterBatch(LETTERBATCH_ID));
    }
    
    @Test
    public void usesLetterService() {
        resource.asyncLetter(DocumentProviderTestData.getAsyncLetterBatch());
        verify(service, times(1)).createLetter(any(AsyncLetterBatchDto.class));
    }
    
    @Test
    public void initializesTemplateId() {
        resource.asyncLetter(DocumentProviderTestData.getAsyncLetterBatch());
        verify(builder, times(1)).initTemplateId(any(LetterBatchDetails.class));
    }
    
    @Test
    public void returnsLetterBatchIdFromResource() {
        assertEquals(LetterService.DOKUMENTTI_ID_PREFIX_PDF
                +LETTERBATCH_ID, (String)resource.asyncLetter(DocumentProviderTestData.getAsyncLetterBatch()).getEntity());
    }
    
    @Test
    public void returnsBadRequestForInvalidLetterBatch() {
        assertEquals(Status.BAD_REQUEST.getStatusCode(), resource.asyncLetter(null).getStatus());
    }
    
    @Test
    public void startsProcessingLetters() {
        resource.asyncLetter(DocumentProviderTestData.getAsyncLetterBatch()).getEntity();
        verify(processor).processLetterBatch(any(Integer.class));
    }
    
    @Test
    public void returnsServerErrorWhenExceptionIsThrownDuringAsyncLetter() {
        when(service.createLetter(any(AsyncLetterBatchDto.class))).thenThrow(new NullPointerException());
        assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), resource.asyncLetter(DocumentProviderTestData.getAsyncLetterBatch()).getStatus());
    }

    private void injectObject(String field, Object object) throws NoSuchFieldException, IllegalAccessException {
        Field letterService = resource.getClass().getDeclaredField(field);
        letterService.setAccessible(true);
        letterService.set(resource, object);
    }
}
