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
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipientReplacement;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class ReportedRecipientReplacementDAOTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private ReportedMessageDAO reportedMessageDAO;

    @Autowired
    private ReportedRecipientDAO reportedRecipientDAO;

    @Autowired
    private ReportedRecipientReplacementDAO reportedRecipientReplacementDAO;

    @Test
    public void testReportedRecipientReplacementInsertWasSuccesful() {

	ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
	ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

	ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
	ReportedRecipient savedReportedRecipient = reportedRecipientDAO.insert(reportedRecipient);

	ReportedRecipientReplacement reportedRecipientReplacement = 
		RaportointipalveluTestData.getReportedRecipientReplacement(savedReportedRecipient);

	ReportedRecipientReplacement savedReportedRecipientReplacement = reportedRecipientReplacementDAO.insert(reportedRecipientReplacement);

	assertNotNull(savedReportedRecipientReplacement);
	assertNotNull(savedReportedRecipientReplacement.getId());
	assertNotNull(savedReportedRecipientReplacement.getVersion());
    }

    @Test
    public void testAllReportedRecipientReplacementFound() {

	ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
	ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

	ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
	ReportedRecipient savedReportedRecipient = reportedRecipientDAO.insert(reportedRecipient);

	ReportedRecipientReplacement reportedRecipientReplacement = 
		RaportointipalveluTestData.getReportedRecipientReplacement(savedReportedRecipient);
	ReportedRecipientReplacement savedReportedRecipientReplacement = reportedRecipientReplacementDAO.insert(reportedRecipientReplacement);

	List<ReportedRecipientReplacement> reportedRecipientReplacements = reportedRecipientReplacementDAO.findAll();

	assertNotNull(reportedRecipientReplacements);
	assertNotEquals(0, reportedRecipientReplacements.size());
	assertEquals(savedReportedRecipientReplacement, reportedRecipientReplacements.get(0));
    }

    @Test
    public void testReportedRecipientReplacementsFoundByID() {

	ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
	ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

	ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
	ReportedRecipient savedReportedRecipient = reportedRecipientDAO.insert(reportedRecipient);

	ReportedRecipientReplacement reportedRecipientReplacement = 
		RaportointipalveluTestData.getReportedRecipientReplacement(savedReportedRecipient);
	ReportedRecipientReplacement savedReportedRecipientReplacement = reportedRecipientReplacementDAO.insert(reportedRecipientReplacement);

	Long id = savedReportedRecipientReplacement.getId();
	ReportedRecipientReplacement searchedReportedRecipientReplacement = reportedRecipientReplacementDAO.read(id);

	assertNotNull(searchedReportedRecipientReplacement);
	assertEquals(savedReportedRecipientReplacement.getId(), searchedReportedRecipientReplacement.getId());
	assertEquals(savedReportedRecipientReplacement, searchedReportedRecipientReplacement);
    }

    @Test
    public void testReportedReceipientReplacementFoundByReceipientId() {

	ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
	ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

	ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
	ReportedRecipient savedReportedRecipient = reportedRecipientDAO.insert(reportedRecipient);

	ReportedRecipientReplacement reportedRecipientReplacement = 
		RaportointipalveluTestData.getReportedRecipientReplacement(savedReportedRecipient);
	ReportedRecipientReplacement savedReportedRecipientReplacement = reportedRecipientReplacementDAO.insert(reportedRecipientReplacement);

	List<ReportedRecipientReplacement> reportedRecipientReplacements = 
		reportedRecipientReplacementDAO.findReportedRecipientReplacements(savedReportedRecipient);

	assertNotNull(reportedRecipientReplacements);
	assertNotEquals(0, reportedRecipientReplacements.size());
	assertEquals(savedReportedRecipientReplacement, reportedRecipientReplacements.get(0));
    }

    @Test
    public void testReportedMessageUpdateIsSuccesful() {

	ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
	ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

	ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
	ReportedRecipient savedReportedRecipient = reportedRecipientDAO.insert(reportedRecipient);

	ReportedRecipientReplacement reportedRecipientReplacement = 
		RaportointipalveluTestData.getReportedRecipientReplacement(savedReportedRecipient);
	ReportedRecipientReplacement savedReportedRecipientReplacement = reportedRecipientReplacementDAO.insert(reportedRecipientReplacement);

	assertEquals(new Long(0), savedReportedRecipientReplacement.getVersion());

	savedReportedRecipientReplacement.setName("new-name");		
	reportedRecipientReplacementDAO.update(savedReportedRecipientReplacement);

	assertEquals(new Long(1), savedReportedRecipientReplacement.getVersion());
	assertEquals("new-name", savedReportedRecipientReplacement.getName());
    }

    @Test
    public void testReportedMessageDeletedIsSuccesful() {

	ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
	ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

	ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
	ReportedRecipient savedReportedRecipient = reportedRecipientDAO.insert(reportedRecipient);

	ReportedRecipientReplacement reportedRecipientReplacement = 
		RaportointipalveluTestData.getReportedRecipientReplacement(savedReportedRecipient);
	ReportedRecipientReplacement savedReportedRecipientReplacement = reportedRecipientReplacementDAO.insert(reportedRecipientReplacement);

	long id = savedReportedRecipientReplacement.getId();
	reportedRecipientReplacementDAO.remove(savedReportedRecipientReplacement);

	ReportedRecipientReplacement notFoundReportedRecipientReplacement = reportedRecipientReplacementDAO.read(id);
	assertNull(notFoundReportedRecipientReplacement);
    }

    @Test
    public void testReportedMessageNotFoundByID() {
	Long id = new Long(2013121452);
	ReportedRecipientReplacement reportedRecipientReplacement = reportedRecipientReplacementDAO.read(id);

	assertNull(reportedRecipientReplacement);
    }

}
