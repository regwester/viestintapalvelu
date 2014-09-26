package fi.vm.sade.viestintapalvelu.dao;

import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterBatchProcessingError;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class LetterBatchDAOTest {
    @Autowired
    private LetterBatchDAO letterBatchDAO;

    @Test
    public void testFindLetterBatchByNameOrgTag() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatchDAO.insert(letterBatch);
        
        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(
            "test-templateName", "FI", "1.2.246.562.10.00000000001", "test-tag");
        
        assertNotNull(foundLetterBatch);
        assertTrue(foundLetterBatch.getId() > 0);
        assertNotNull(foundLetterBatch.getLetterReceivers());
        assertTrue(foundLetterBatch.getLetterReceivers().size() > 0);
        assertNotNull(foundLetterBatch.getLetterReplacements());
        assertTrue(foundLetterBatch.getLetterReplacements().size() > 0);
        assertEquals("Status is 'processing' by default in the test data generator", LetterBatch.Status.processing, foundLetterBatch.getBatchStatus());
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
        assertEquals("Status is 'processing' by default in the test data generator", LetterBatch.Status.processing, foundLetterBatch.getBatchStatus());
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
        long idC = letterBatchDAO.insert(letterBatch).getId();
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
        System.out.println(letterBatch.getProcessingErrors().toString());
        assertEquals("Testing failure case", letterBatch.getProcessingErrors().get(0).getErrorCause());

    }


}
