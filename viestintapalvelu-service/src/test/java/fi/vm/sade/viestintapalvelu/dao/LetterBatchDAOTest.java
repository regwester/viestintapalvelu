package fi.vm.sade.viestintapalvelu.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

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
            "test-templateName", "FI", "1.2.246.562.10.00000000001", Optional.of("test-tag"),
                Optional.<String>absent());
        
        assertNotNull(foundLetterBatch);
        assertTrue(foundLetterBatch.getId() > 0);
        assertNotNull(foundLetterBatch.getLetterReceivers());
        assertTrue(foundLetterBatch.getLetterReceivers().size() > 0);
        assertNotNull(foundLetterBatch.getLetterReplacements());
        assertTrue(foundLetterBatch.getLetterReplacements().size() > 0);
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
    }

}
