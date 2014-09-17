package fi.vm.sade.viestintapalvelu.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.letter.LetterBuilder;
import fi.vm.sade.viestintapalvelu.letter.LetterContent;
import fi.vm.sade.viestintapalvelu.letter.LetterService;
import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;
import fi.vm.sade.viestintapalvelu.letter.impl.LetterServiceImpl;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("/test-application-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class LetterServiceTest {
    @Mock
    private LetterBatchDAO mockedLetterBatchDAO;
    @Mock
    private LetterReceiverLetterDAO mockedLetterReceiverLetterDAO;
    @Mock
    private CurrentUserComponent mockedCurrentUserComponent;
    @Mock
    private TemplateDAO templateDAO;
    private LetterService letterService;
    @Mock
    private LetterBuilder letterBuilder;

    @Before
    public void setup() {
        this.letterService = new LetterServiceImpl(mockedLetterBatchDAO, mockedLetterReceiverLetterDAO,
            mockedCurrentUserComponent, templateDAO, letterBuilder);
    }
    
    @Test
    public void testCreateLetter() {
        fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch();
        
        when(mockedCurrentUserComponent.getCurrentUser()).thenReturn(DocumentProviderTestData.getHenkilo());
        when(mockedLetterBatchDAO.insert(any(LetterBatch.class))).thenAnswer(new Answer<LetterBatch>() {
            @Override
            public LetterBatch answer(InvocationOnMock invocation) throws Throwable {
              return (LetterBatch) invocation.getArguments()[0];
            }
        });
        when(templateDAO.findTemplateByName(any(String.class), any(String.class)))
            .thenReturn(DocumentProviderTestData.getTemplate(1l));
        LetterBatch createdLetterBatch = letterService.createLetter(letterBatch);
        
        assertNotNull(createdLetterBatch);
        assertNotNull(createdLetterBatch.getLetterReceivers());
        assertTrue(createdLetterBatch.getLetterReceivers().size() > 0);
        assertNotNull(createdLetterBatch.getLetterReplacements());
        assertTrue(createdLetterBatch.getLetterReplacements().size() > 0);
        assertNotNull(createdLetterBatch.getTemplateId());
        assertEquals(1, createdLetterBatch.getUsedTemplates().size());
    }

    @Test
    public void testFindById() {
        List<LetterBatch> mockedLetterBatchList = new ArrayList<LetterBatch>();
        mockedLetterBatchList.add(DocumentProviderTestData.getLetterBatch(new Long(1)));
        when(mockedLetterBatchDAO.findBy(any(String.class), any(Object.class))).thenReturn(mockedLetterBatchList);
        
        fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatchFindById = letterService.findById(new Long(1));
        
        assertNotNull(letterBatchFindById);
        assertNotNull(letterBatchFindById.getTemplateId());
    }

    @Test
    public void testFindLetterBatchByNameOrgTag() {
        when(mockedLetterBatchDAO.findLetterBatchByNameOrgTag(any(String.class), eq("FI"), any(String.class), 
            any(String.class))).thenReturn(DocumentProviderTestData.getLetterBatch(new Long(1)));
        
        fi.vm.sade.viestintapalvelu.letter.LetterBatch foundLetterBatch = 
            letterService.findLetterBatchByNameOrgTag("test-template", "FI", "1.2.246.562.10.00000000001", "test-tag");
        
        assertNotNull(foundLetterBatch);
        assertTrue(foundLetterBatch.getTemplateId() == 1);
        assertTrue(foundLetterBatch.getTemplateName().equals("test-templateName"));
        assertTrue(foundLetterBatch.getLetters() == null);
        assertNotNull(foundLetterBatch.getTemplateReplacements());
        assertTrue(foundLetterBatch.getTemplateReplacements().size() > 0);
    }

    @Test
    public void testFindReplacementByNameOrgTag() {
        when(mockedLetterBatchDAO.findLetterBatchByNameOrg(any(String.class), eq("FI"), 
            any(String.class))).thenReturn(DocumentProviderTestData.getLetterBatch(new Long(1)));
        
        List<fi.vm.sade.viestintapalvelu.template.Replacement> replacements = 
            letterService.findReplacementByNameOrgTag("test-templateName", "FI", "1.2.246.562.10.00000000001", null);
        
        assertNotNull(replacements);
        assertTrue(replacements.size() > 0);
        assertTrue(replacements.get(0).getName().equals("test-replacement-name"));
        assertTrue(!replacements.get(0).isMandatory());
        assertTrue(replacements.get(0).getDefaultValue().isEmpty());
    }

    @Test
    public void testGetLetter() {
        List<LetterBatch> mockedLetterBatchList = new ArrayList<LetterBatch>();
        mockedLetterBatchList.add(DocumentProviderTestData.getLetterBatch(new Long(1)));
        
        List<LetterReceivers> mockedLetterReceiversList = 
            new ArrayList<LetterReceivers>(mockedLetterBatchList.get(0).getLetterReceivers());
        
        List<LetterReceiverLetter> mockedLetterReceiverLetterList = new ArrayList<LetterReceiverLetter>();
        mockedLetterReceiverLetterList.add(mockedLetterReceiversList.get(0).getLetterReceiverLetter());
        
        when(mockedLetterReceiverLetterDAO.findBy(any(String.class), any(Object.class))).thenReturn(
            mockedLetterReceiverLetterList);
        
        LetterContent letterContent = letterService.getLetter(new Long(1));
        
        assertNotNull(letterContent);
        assertTrue(letterContent.getContentType().equalsIgnoreCase("application/msword"));
        assertNotNull(letterContent.getContent());
        assertTrue(new String(letterContent.getContent()).equalsIgnoreCase("letter"));
    }
    
    @Test
    public void setsHandlingStartedWhenStartingProcessIsCalled() {
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(1l);
        when(mockedLetterBatchDAO.read(any(Long.class))).thenReturn(batch);
        letterService.updateBatchProcessingStarted(1l, LetterBatchProcess.LETTER);
        assertNotNull(batch.getHandlingStarted());
    }
    
    @Test
    public void setsHandlingFinishedWhenFinishingProcessIsCalled() {
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(1l);
        when(mockedLetterBatchDAO.read(any(Long.class))).thenReturn(batch);
        letterService.updateBatchProcessingFinished(1l, LetterBatchProcess.LETTER);
        assertNotNull(batch.getHandlingFinished());
    }
    
    @Test
    public void setsEmailHandlingStartedWhenStartingProcessIsCalled() {
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(1l);
        when(mockedLetterBatchDAO.read(any(Long.class))).thenReturn(batch);
        letterService.updateBatchProcessingStarted(1l, LetterBatchProcess.EMAIL);
        assertNotNull(batch.getEmailHandlingStarted());
    }
    
    @Test
    public void setsEmailHandlingFinishedWhenFinishingProcessIsCalled() {
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(1l);
        when(mockedLetterBatchDAO.read(any(Long.class))).thenReturn(batch);
        letterService.updateBatchProcessingFinished(1l, LetterBatchProcess.EMAIL);
        assertNotNull(batch.getEmailHandlingFinished());
    }
    
    @Test
    public void usesDAOWhenFetchingLetterBatch() {
        letterService.fetchById(1l);
        verify(mockedLetterBatchDAO).read(1l);
    }
    
    @Test
    public void usesDAOWhenUpdatingLetterReceiverLetter() {
        LetterReceiverLetter letter = new LetterReceiverLetter();
        letterService.updateLetter(letter);
        verify(mockedLetterReceiverLetterDAO).update(letter);
    }
}
