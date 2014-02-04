package fi.vm.sade.ryhmasahkoposti.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailRecipientQueryDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class ReportedMessageDAOTest {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Autowired
	private ReportedMessageDAO reportedMessageDAO;
	@Autowired
	private ReportedRecipientDAO reportedRecipientDAO;

	@Test
	public void testReportedMessageInsertWasSuccesful() {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);
		
		assertNotNull(savedReportedMessage);
		assertNotNull(savedReportedMessage.getId());
		assertNotNull(savedReportedMessage.getVersion());
	}

	@Test
	public void testAllReportedMessagesFound() {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		reportedMessageDAO.insert(reportedMessage);

		List<ReportedMessage> reportedMessages = reportedMessageDAO.findAll();
		
		assertNotNull(reportedMessages);
		assertNotEquals(0, reportedMessages.size());
	}
	
	@Test
	public void testReportedMessageFoundByID() {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

		Long id = savedReportedMessage.getId();
		ReportedMessage searchedReportedMessage = reportedMessageDAO.read(id);
		
		assertNotNull(searchedReportedMessage);
		assertEquals(savedReportedMessage.getId(), searchedReportedMessage.getId());
	}

    @Test
    public void testReportedMessageFoundByRecipientOID() {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		ReportedRecipient reportedRecipient = 
			RaportointipalveluTestData.getReportedRecipient(reportedMessage);
		Set<ReportedRecipient> reportedRecipients = new HashSet<ReportedRecipient>();
		reportedRecipients.add(reportedRecipient);
		reportedMessage.setReportedRecipients(reportedRecipients);
		reportedMessageDAO.insert(reportedMessage);

		EmailMessageQueryDTO emailMessageQuery = new EmailMessageQueryDTO();
        EmailRecipientQueryDTO emailRecipientQuery = new EmailRecipientQueryDTO();
        emailRecipientQuery.setRecipientOid("1.2.246.562.24.34397748041");
        emailMessageQuery.setEmailRecipientQueryDTO(emailRecipientQuery);
                
        List<ReportedMessage> searchedReportedMessages = 
        	reportedMessageDAO.findBySearchCriteria(emailMessageQuery);

        assertNotNull(searchedReportedMessages);
        assertTrue(1 <= searchedReportedMessages.size());
    }

    @Test
    public void testReportedMessageFoundByRecipientName() {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		ReportedRecipient reportedRecipient = 
			RaportointipalveluTestData.getReportedRecipient(reportedMessage);
		Set<ReportedRecipient> recipients = new HashSet<ReportedRecipient>();
		recipients.add(reportedRecipient);
		reportedMessage.setReportedRecipients(recipients);
		reportedMessageDAO.insert(reportedMessage);

		EmailMessageQueryDTO emailMessageQuery = new EmailMessageQueryDTO();
        EmailRecipientQueryDTO emailRecipientQuery = new EmailRecipientQueryDTO();
        emailRecipientQuery.setRecipientName("Testi Oppilas");
        emailMessageQuery.setEmailRecipientQueryDTO(emailRecipientQuery);
                
        List<ReportedMessage> searchedReportedMessages = reportedMessageDAO.findBySearchCriteria(emailMessageQuery);

        assertNotNull(searchedReportedMessages);
        assertTrue(1 <= searchedReportedMessages.size());
    }

    @Test
    public void testReportedMessageNotFoundBySearchCriteria() {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		ReportedRecipient reportedRecipient = 
			RaportointipalveluTestData.getReportedRecipient(reportedMessage);
		Set<ReportedRecipient> recipients = new HashSet<ReportedRecipient>();
		recipients.add(reportedRecipient);
		reportedMessage.setReportedRecipients(recipients);
		reportedMessageDAO.insert(reportedMessage);

		EmailMessageQueryDTO emailMessageQuery = new EmailMessageQueryDTO();
        EmailRecipientQueryDTO emailRecipientQuery = new EmailRecipientQueryDTO();
        emailRecipientQuery.setRecipientEmail("ei.loydy@sposti.fi");
        emailMessageQuery.setEmailRecipientQueryDTO(emailRecipientQuery);
                
        List<ReportedMessage> searchedReportedMessages = reportedMessageDAO.findBySearchCriteria(emailMessageQuery);

        assertNotNull(searchedReportedMessages);
        assertTrue(searchedReportedMessages.size() == 0);
    }

	@Test
	public void testReportedMessageUpdateIsSuccesful() {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);
		
		assertEquals(new Long(0), savedReportedMessage.getVersion());
		
		savedReportedMessage.setSendingEnded(new Date());		
		reportedMessageDAO.update(savedReportedMessage);
		
		assertEquals(new Long(1), savedReportedMessage.getVersion());
	}

	@Test
	public void testReportedMessageNotFoundByID() {
		Long viestiID = new Long(2013121452);
		ReportedMessage reportedMessage = reportedMessageDAO.read(viestiID);
		
		assertNull(reportedMessage);
	}
	
	@Test
	public void testNumberOfRecordsMatches() {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		reportedMessageDAO.insert(reportedMessage);
		
		Long lkm = reportedMessageDAO.findNumberOfReportedMessage();
		
		assertNotNull(lkm);
		assertNotEquals(new Long(0), lkm);
	}	
}
