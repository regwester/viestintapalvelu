package fi.vm.sade.viestintapalvelu.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterBatchProcessingError;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class LetterBatchDAOTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LetterBatchDAO letterBatchDAO;

    @Test
    public void testFindLetterBatchByNameOrgTag() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatchDAO.insert(letterBatch);
        
        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(
            "test-templateName", "FI", "1.2.246.562.10.00000000001", Optional.of("test-tag"),
                Optional.<String>absent());
        
        assertNotNull(foundLetterBatch);
        assertTrue(foundLetterBatch.getId() > 0);
        assertNotNull(foundLetterBatch.getLetterReceivers());
        assertTrue(foundLetterBatch.getLetterReceivers().size() > 0);
        assertNotNull(foundLetterBatch.getLetterReplacements());
        assertTrue(foundLetterBatch.getLetterReplacements().size() > 0);
        assertEquals("Status is 'processing' by default in the test data generator", LetterBatch.Status.processing, foundLetterBatch.getBatchStatus());
    }

    @Test
    public void testFindLetterBatchByNameOrgTagAndApplicationPeriod() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setApplicationPeriod("period");
        letterBatchDAO.insert(letterBatch);

        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(
                "test-templateName", "FI", "1.2.246.562.10.00000000001", Optional.of("test-tag"),
                Optional.of("period"));

        assertNotNull(foundLetterBatch);
        assertTrue(foundLetterBatch.getId() > 0);
        assertNotNull(foundLetterBatch.getLetterReceivers());
        assertTrue(foundLetterBatch.getLetterReceivers().size() > 0);
        assertNotNull(foundLetterBatch.getLetterReplacements());
        assertTrue(foundLetterBatch.getLetterReplacements().size() > 0);
    }

    @Test
    public void testFindLetterBatchByNameOrgTagAndApplicationPeriodNotFoundByTag() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setTag("other-tag");
        letterBatch.setApplicationPeriod("period");
        letterBatchDAO.insert(letterBatch);

        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(
                "test-templateName", "FI", "1.2.246.562.10.00000000001", Optional.of("test-tag"),
                Optional.of("period"));

        assertNull(foundLetterBatch);
    }

    @Test
    public void testFindLetterBatchByNameOrgTagAndApplicationPeriodWithoutTag() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setApplicationPeriod("period");
        letterBatchDAO.insert(letterBatch);

        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(
                "test-templateName", "FI", "1.2.246.562.10.00000000001", Optional.<String>absent(),
                Optional.of("period"));

        assertNotNull(foundLetterBatch);
        assertTrue(foundLetterBatch.getId() > 0);
        assertNotNull(foundLetterBatch.getLetterReceivers());
        assertTrue(foundLetterBatch.getLetterReceivers().size() > 0);
        assertNotNull(foundLetterBatch.getLetterReplacements());
        assertTrue(foundLetterBatch.getLetterReplacements().size() > 0);
    }

    @Test
    public void testFindLetterBatchByNameOrgTagAndApplicationPeriodNotFound() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setApplicationPeriod("period");
        letterBatchDAO.insert(letterBatch);

        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(
                "test-templateName", "FI", "1.2.246.562.10.00000000001", Optional.of("test-tag"),
                Optional.of("other period"));

        assertNull(foundLetterBatch);
    }

    @Test
    public void testFindLetterBatchByNameOrg() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatchDAO.insert(letterBatch);
        
        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrg(
            "test-templateName", "FI", "1.2.246.562.10.00000000001");
        
        assertNotNull(foundLetterBatch);
        assertTrue(foundLetterBatch.getId() > 0);
        assertNotNull(foundLetterBatch.getLetterReceivers());
        assertTrue(foundLetterBatch.getLetterReceivers().size() > 0);
        assertNotNull(foundLetterBatch.getLetterReplacements());
        assertTrue(foundLetterBatch.getLetterReplacements().size() > 0);
        assertEquals("Status is 'processing' by default in the test data generator",
                LetterBatch.Status.processing, foundLetterBatch.getBatchStatus());
    }

    @Test(expected = PersistenceException.class)
    public void insertNullError() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setBatchStatus(LetterBatch.Status.error);
        List<LetterBatchProcessingError> errors = new ArrayList<LetterBatchProcessingError>();
        LetterBatchProcessingError error = new LetterBatchProcessingError();
        error.setErrorCause("Testing failure case");
        error.setLetterBatch(letterBatch);
        errors.add(error);
        letterBatch.setProcessingErrors(errors);
        letterBatchDAO.insert(letterBatch).getId();
    }

    @Test
    public void getBatchStatus() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setBatchStatus(LetterBatch.Status.processing);
        letterBatch.setProcessingErrors(null);
        long idA = letterBatchDAO.insert(letterBatch).getId();

        letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setBatchStatus(LetterBatch.Status.ready);
        long idB = letterBatchDAO.insert(letterBatch).getId();

        LetterBatchStatusDto statusA = letterBatchDAO.getLetterBatchStatus(idA),
                statusB = letterBatchDAO.getLetterBatchStatus(idB);
        assertNotNull(statusA);
        assertNotNull(statusB);
        assertEquals(LetterBatch.Status.processing, statusA.getStatus());
        assertEquals(LetterBatch.Status.ready, statusB.getStatus());
        assertNotNull(statusA.getSent());
        assertNotNull(statusA.getTotal());
        assertNotNull(statusA.getEmailsProcessed());
        assertEquals(Integer.valueOf(1), statusA.getSent());
        assertEquals(Integer.valueOf(0), statusA.getEmailsProcessed());
        assertEquals(Integer.valueOf(1), statusA.getTotal());
        assertEquals(Integer.valueOf(1), statusB.getSent());
        assertEquals(Integer.valueOf(0), statusB.getEmailsProcessed());
        assertEquals(Integer.valueOf(1), statusB.getTotal());

        letterBatch.getLetterReceivers().iterator().next().setEmailAddress("foo@example.com");
        letterBatchDAO.update(letterBatch);
        statusB = letterBatchDAO.getLetterBatchStatus(idB);
        assertEquals(Integer.valueOf(1), statusB.getEmailsProcessed());

        letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setBatchStatus(LetterBatch.Status.error);
        List<LetterBatchProcessingError> errors = new ArrayList<LetterBatchProcessingError>();
        LetterBatchProcessingError error = new LetterBatchProcessingError();
        error.setErrorCause("Testing failure case");
        error.setLetterBatch(letterBatch);
        error.setErrorTime(new Date());
        error.setLetterReceivers(letterBatch.getLetterReceivers().iterator().next());
        errors.add(error);
        letterBatch.setProcessingErrors(errors);
        long idC = letterBatchDAO.insert(letterBatch).getId();

        letterBatch = letterBatchDAO.read(idA);
        assertEquals(null, letterBatch.getProcessingErrors());

        letterBatch = letterBatchDAO.read(idB);
        assertEquals(null, letterBatch.getProcessingErrors());

        letterBatch = letterBatchDAO.read(idC);
        assertEquals(1, letterBatch.getProcessingErrors().size());
        logger.info(letterBatch.getProcessingErrors().toString());
        assertEquals("Testing failure case", letterBatch.getProcessingErrors().get(0).getErrorCause());
    }
    
    @Test
    public void returnsEmptyListWhenAllLettersAreProcessed() {
        assertTrue(letterBatchDAO.findUnprocessedLetterReceiverIdsByBatch(givenLetterBatchWithLetter("afeaf".getBytes())).isEmpty());
    }

    @Test
    public void returnsUnprocessedLetters() {
        assertEquals(1, letterBatchDAO.findUnprocessedLetterReceiverIdsByBatch(givenLetterBatchWithLetter(null)).size());
    }

    private long givenLetterBatchWithLetter(byte[] letter) {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setBatchStatus(LetterBatch.Status.processing);
        letterBatch.getLetterReceivers().iterator().next().getLetterReceiverLetter().setLetter(letter);
        return letterBatchDAO.insert(letterBatch).getId();
    }

}
