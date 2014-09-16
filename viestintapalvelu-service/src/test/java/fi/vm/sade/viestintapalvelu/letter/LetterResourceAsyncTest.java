package fi.vm.sade.viestintapalvelu.letter;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.model.IPosti;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateService;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional
public class LetterResourceAsyncTest {

    private static final Long TEMPLATE_ID = 1l;
    private static final long LETTERBATCH_ID = 234;
    private static final long LETTERRECEIVERS_ID = 123;

    //mocked object variable names have to match to those in LetterResource
    @Mock
    LetterService letterService;

    @InjectMocks
    LetterResource letterResource = new LetterResource();

    @Autowired
    LetterBatchDAO letterBatchDAO;

    @Autowired
    TemplateService templateService;

    @Before
    public void injectMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processesesLetterPDF() {

        //todo figure out how to mock/stub CurrentUserComponent



        Template template = DocumentProviderTestData.getTemplate(TEMPLATE_ID);
        templateService.storeTemplate(template); //save template to test DB first, otherwise batch saving won't work


        final LetterBatch inputBatch =  getMockBatch();
        fi.vm.sade.viestintapalvelu.model.LetterBatch mockBatchModel = getMockedBatchModels(100000);

        Henkilo henk = mock(Henkilo.class);
        when(henk.getId()).thenReturn(12345L);
        System.out.println(letterBatchDAO.findAll().size());
        fi.vm.sade.viestintapalvelu.model.LetterBatch letterBatch = letterBatchDAO.insert(mockBatchModel);
        when(letterService.createLetter(eq(inputBatch))).thenReturn(letterBatch);

        Long start = System.currentTimeMillis();
        Response response = letterResource.asyncLetter(inputBatch);
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void processesXLettersUnderMinute() {
        
    }

    private fi.vm.sade.viestintapalvelu.model.LetterBatch getMockedBatchModels(int count) {
        fi.vm.sade.viestintapalvelu.model.LetterBatch batch = DocumentProviderTestData.getLetterBatch(1l);

        DocumentProviderTestData.getLetterBatch(LETTERBATCH_ID);

        Set<LetterReceivers> receivers = DocumentProviderTestData.getLetterReceivers(LETTERRECEIVERS_ID, batch);

        batch.setLetterReceivers(receivers);
        batch.setId(LETTERBATCH_ID);
        batch.setVersion(1l);
        batch.setTemplateId(TEMPLATE_ID);

        List<IPosti> posti = DocumentProviderTestData.getIPosti(LETTERBATCH_ID, batch);
        batch.getIposti().addAll(posti);

        assertTrue(batch.getLetterReplacements().size() > 0);
        assertTrue(batch.getLetterReceivers().size() > 0);
        assertTrue(batch.getIposti().size() > 0);

        return batch;
    }

    private fi.vm.sade.viestintapalvelu.letter.LetterBatch getMockBatch(){
        fi.vm.sade.viestintapalvelu.letter.LetterBatch batch = DocumentProviderTestData.getLetterBatch();
        return batch;
    }
    
}
