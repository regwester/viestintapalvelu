package fi.vm.sade.ryhmasahkoposti.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageReplacement;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly = true)
public class ReportedMessageReplacementDAOTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private ReportedMessageDAO reportedMessageDAO;

    @Autowired
    private ReportedMessageReplacementDAO reportedMessageReplacementDAO;

    @Test
    public void testReportedMessageReplacementInsertWasSuccessful() {

        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

        ReportedMessageReplacement reportedMessageReplacement =
                RaportointipalveluTestData.getReportedMessageReplacement(savedReportedMessage);

        ReportedMessageReplacement savedReportedMessageReplacement = reportedMessageReplacementDAO.insert(reportedMessageReplacement);

        assertNotNull(savedReportedMessageReplacement);
        assertNotNull(savedReportedMessageReplacement.getId());
        assertNotNull(savedReportedMessageReplacement.getVersion());
    }

    @Test
    public void testAllReportedMessageReplacementFound() {

        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

        ReportedMessageReplacement reportedMessageReplacement =
                RaportointipalveluTestData.getReportedMessageReplacement(savedReportedMessage);
        ReportedMessageReplacement savedReportedMessageReplacement = reportedMessageReplacementDAO.insert(reportedMessageReplacement);

        List<ReportedMessageReplacement> reportedMessageReplacements = reportedMessageReplacementDAO.findAll();

        assertNotNull(reportedMessageReplacements);
        assertNotEquals(0, reportedMessageReplacements.size());
        assertEquals(savedReportedMessageReplacement, reportedMessageReplacements.get(0));
    }

    @Test
    public void testReportedMessageReplacementsFoundByID() {

        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

        ReportedMessageReplacement reportedMessageReplacement =
                RaportointipalveluTestData.getReportedMessageReplacement(savedReportedMessage);
        ReportedMessageReplacement savedReportedMessageReplacement = reportedMessageReplacementDAO.insert(reportedMessageReplacement);

        Long id = savedReportedMessageReplacement.getId();
        ReportedMessageReplacement searchedReportedMessageReplacement = reportedMessageReplacementDAO.read(id);

        assertNotNull(searchedReportedMessageReplacement);
        assertEquals(savedReportedMessageReplacement.getId(), searchedReportedMessageReplacement.getId());
        assertEquals(savedReportedMessageReplacement, searchedReportedMessageReplacement);
    }

    @Test
    public void testReportedMessageReplacementFoundByMessageId() {

        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

        ReportedMessageReplacement reportedMessageReplacement =
                RaportointipalveluTestData.getReportedMessageReplacement(savedReportedMessage);
        ReportedMessageReplacement savedReportedMessageReplacement = reportedMessageReplacementDAO.insert(reportedMessageReplacement);

        List<ReportedMessageReplacement> reportedMessageReplacements =
                reportedMessageReplacementDAO.findReportedMessageReplacements(savedReportedMessage);

        assertNotNull(reportedMessageReplacements);
        assertNotEquals(0, reportedMessageReplacements.size());
        assertEquals(savedReportedMessageReplacement, reportedMessageReplacements.get(0));
    }

    @Test
    public void testReportedMessageUpdateIsSuccessful() {

        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

        ReportedMessageReplacement reportedMessageReplacement =
                RaportointipalveluTestData.getReportedMessageReplacement(savedReportedMessage);
        ReportedMessageReplacement savedReportedMessageReplacement = reportedMessageReplacementDAO.insert(reportedMessageReplacement);

        assertEquals(new Long(0), savedReportedMessageReplacement.getVersion());

        savedReportedMessageReplacement.setName("new-name");
        reportedMessageReplacementDAO.update(savedReportedMessageReplacement);

        assertEquals(new Long(1), savedReportedMessageReplacement.getVersion());
        assertEquals("new-name", savedReportedMessageReplacement.getName());
    }

    @Test
    public void testReportedMessageDeletedIsSuccessful() {

        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

        ReportedMessageReplacement reportedMessageReplacement =
                RaportointipalveluTestData.getReportedMessageReplacement(savedReportedMessage);
        ReportedMessageReplacement savedReportedMessageReplacement = reportedMessageReplacementDAO.insert(reportedMessageReplacement);

        long id = savedReportedMessageReplacement.getId();
        reportedMessageReplacementDAO.remove(savedReportedMessageReplacement);

        ReportedMessageReplacement notFoundReportedMessageReplacement = reportedMessageReplacementDAO.read(id);
        assertNull(notFoundReportedMessageReplacement);
    }

    @Test
    public void testReportedMessageNotFoundByID() {
        Long id = new Long(2013121452);
        ReportedMessageReplacement reportedMessageReplacement = reportedMessageReplacementDAO.read(id);

        assertNull(reportedMessageReplacement);
    }

}
