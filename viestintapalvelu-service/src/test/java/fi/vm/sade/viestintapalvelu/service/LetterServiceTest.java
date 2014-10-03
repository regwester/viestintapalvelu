package fi.vm.sade.viestintapalvelu.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.dao.*;
import fi.vm.sade.viestintapalvelu.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.letter.LetterBuilder;
import fi.vm.sade.viestintapalvelu.letter.LetterContent;
import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;
import fi.vm.sade.viestintapalvelu.letter.dto.converter.LetterBatchDtoConverter;
import fi.vm.sade.viestintapalvelu.letter.impl.LetterServiceImpl;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterBatchProcessingError;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

import static org.junit.Assert.*;
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
    private LetterReceiversDAO mockedLetterReceiversDao;
    @Mock
    private CurrentUserComponent mockedCurrentUserComponent;
    @Mock
    private TemplateDAO templateDAO;
    private LetterServiceImpl letterService;
    @Mock
    private LetterBuilder letterBuilder;

    @Before
    public void setup() {
        this.letterService = new LetterServiceImpl(mockedLetterBatchDAO, mockedLetterReceiverLetterDAO,
            mockedCurrentUserComponent, templateDAO, new LetterBatchDtoConverter(),
            mockedLetterReceiversDao, new ObjectMapperProvider());
        this.letterService.setLetterBuilder(letterBuilder);
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
            any(Optional.class), any(Optional.class))).thenReturn(DocumentProviderTestData.getLetterBatch(new Long(1)));
        
        fi.vm.sade.viestintapalvelu.letter.LetterBatch foundLetterBatch = 
            letterService.findLetterBatchByNameOrgTag("test-template", "FI", "1.2.246.562.10.00000000001",
                    Optional.of("test-tag"),
                    Optional.<String>fromNullable(null));
        
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
            letterService.findReplacementByNameOrgTag("test-templateName", "FI", "1.2.246.562.10.00000000001",
                    Optional.<String>absent(),
                    Optional.<String>absent());
        
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

    @Test
    public void getBatchStatusSuccess() {

        System.out.println("getBatchStatusFailure");

        LetterBatchStatusDto statusDto = new LetterBatchStatusDto(123l, 0, 0, LetterBatch.Status.ready, 0);
        when(mockedLetterBatchDAO.getLetterBatchStatus(eq(123l))).thenReturn(statusDto);

        LetterBatchStatusDto batchStatus = letterService.getBatchStatus(123l);

        assertEquals("Batch status should be ready when sent and total match", LetterBatch.Status.ready, batchStatus.getStatus());
        assertNull("Batch errors should be null when successful", batchStatus.getErrors());

        statusDto = new LetterBatchStatusDto(1234l, 456, 456, LetterBatch.Status.ready, 456);
        when(mockedLetterBatchDAO.getLetterBatchStatus(eq(1234l))).thenReturn(statusDto);
        batchStatus = letterService.getBatchStatus(1234l);

        System.out.println("batchStatus = " + batchStatus);
        assertEquals("Batch status should be ready when sent and total match", LetterBatch.Status.ready, batchStatus.getStatus());
        assertNull("Batch errors should be null when successful", batchStatus.getErrors());
    }

    @Test
    public void getBatchStatusSending() {

        LetterBatchStatusDto statusDto = new LetterBatchStatusDto(1238l, 45, 123, LetterBatch.Status.processing, 40);
        when(mockedLetterBatchDAO.getLetterBatchStatus(eq(1238l))).thenReturn(statusDto);
        LetterBatchStatusDto batchStatus = letterService.getBatchStatus(1238l);

        assertEquals("Batch status should be sending when the process is still going", LetterBatch.Status.processing, batchStatus.getStatus());
    }

    @Test
    public void getBatchStatusBatchNotFound() {
        final long batchId = 123;

        LetterBatchProcessingError expectedError = new LetterBatchProcessingError();
        expectedError.setErrorCause("Batch not found for id " + batchId);
        expectedError.setErrorTime(new Date()); //can't check real time as the service just uses the current time in this case
        List<LetterBatchProcessingError> errors = new ArrayList<LetterBatchProcessingError>();
        errors.add(expectedError);

        when(mockedLetterBatchDAO.getLetterBatchStatus(eq(batchId))).thenReturn(null);

        LetterBatchStatusDto batchStatus = letterService.getBatchStatus(batchId);
        assertEquals(LetterBatch.Status.error, batchStatus.getStatus());
        assertEquals("Error message didn't match", expectedError.getErrorCause(), batchStatus.getErrors().get(0).getErrorCause());
    }


    @Test
    public void getBatchStatusFailure() {

        final Date failDate = new Date();

        LetterBatch mockBatch = DocumentProviderTestData.getLetterBatch(1235l);
        assertNotNull(mockBatch.getLetterReceivers());
        LetterReceivers first = mockBatch.getLetterReceivers().iterator().next();

        List<LetterBatchProcessingError> processingErrors = new ArrayList<LetterBatchProcessingError>();
        LetterBatchProcessingError error = new LetterBatchProcessingError();
        error.setLetterBatch(mockBatch);
        error.setErrorCause("Testing error");
        error.setErrorTime(failDate);
        error.setLetterReceivers(first);
        processingErrors.add(error);
        mockBatch.setProcessingErrors(processingErrors);


        LetterBatchStatusDto mockDto = new LetterBatchStatusDto(1235l, 235, 456, LetterBatch.Status.error, 235);

        when(mockedLetterBatchDAO.getLetterBatchStatus(eq(1235l))).thenReturn(mockDto);
        when(mockedLetterBatchDAO.read(eq(1235l))).thenReturn(mockBatch);

        LetterBatchStatusDto statusDto = letterService.getBatchStatus(1235l);
        assertEquals("Batch processing should be indicated with an 'error' status", LetterBatch.Status.error, statusDto.getStatus());
        assertEquals(1, statusDto.getErrors().size());
        assertEquals("Testing error", statusDto.getErrors().get(0).getErrorCause());
        assertNotNull("Letter receivers must not be null", statusDto.getErrors().get(0).getLetterReceivers());

    }

    @Test
    public void saveBatchProcessingError() {
        long batchId = 101l;
        long letterReceiverId = 999l;
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(batchId);
        Set<LetterReceivers> letterReceivers = DocumentProviderTestData.getLetterReceivers(letterReceiverId, batch);
        LetterReceivers receivers = letterReceivers.iterator().next();
        batch.setLetterReceivers(letterReceivers);
        when(mockedLetterReceiversDao.read(eq(letterReceiverId))).thenReturn(receivers);

        String msg = "test message";
        letterService.saveBatchErrorForReceiver(999l, msg);
        assertNotNull(batch.getProcessingErrors());
        assertEquals(1,batch.getProcessingErrors().size());
    }
    
    @Test
    public void usesLetterBatchDaoForFetchingUnfinishedLetterBatches() {
        letterService.findUnfinishedLetterBatches();
        verify(mockedLetterBatchDAO).findUnfinishedLetterBatches();
    }
}
