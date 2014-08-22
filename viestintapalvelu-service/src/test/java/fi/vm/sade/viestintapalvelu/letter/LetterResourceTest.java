package fi.vm.sade.viestintapalvelu.letter;

import java.lang.reflect.Field;

import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ContextConfiguration(locations = "/test-application-context.xml")
@RunWith(MockitoJUnitRunner.class)
public class LetterResourceTest {
    
    private static final Long LETTERBATCH_ID = 1l;

    private LetterResource resource;
    
    @Mock
    private LetterService service;
    
    @Before
    public void init() throws Exception {
        resource = new LetterResource();
        Field letterService = resource.getClass().getDeclaredField("letterService");
        letterService.setAccessible(true);
        letterService.set(resource, service);
        when(service.createLetter(any(LetterBatch.class))).thenReturn(DocumentProviderTestData.getLetterBatch(LETTERBATCH_ID));
    }
    
    @Test
    public void usesLetterService() {
        resource.asyncLetter(DocumentProviderTestData.getLetterBatch());
        verify(service, times(1)).createLetter(any(LetterBatch.class));
    }
    
    @Test
    public void returnsLetterBatchIdFromResource() {
        assertEquals(LETTERBATCH_ID, (Long)resource.asyncLetter(DocumentProviderTestData.getLetterBatch()).getEntity());
    }
    
    @Test
    public void returnsBadRequestForInvalidLetterBatch() {
        assertEquals(Status.BAD_REQUEST.getStatusCode(), resource.asyncLetter(null).getStatus());
    }
}
