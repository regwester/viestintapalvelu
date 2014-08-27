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

import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedRecipientQueryDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly = true)
public class ReportedMessageDAOTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private ReportedMessageDAO reportedMessageDAO;
    @Autowired
    private ReportedRecipientDAO reportedRecipientDAO;

    @Test
    public void testReportedMessageInsertWasSuccessful() {
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

        ReportedMessageQueryDTO reportedMessageQuery = new ReportedMessageQueryDTO();
        reportedMessageQuery.setOrganizationOid("1.2.246.562.10.00000000001");
        ReportedRecipientQueryDTO reportedRecipientQuery = new ReportedRecipientQueryDTO();
        reportedRecipientQuery.setRecipientOid("1.2.246.562.24.34397748041");
        reportedMessageQuery.setReportedRecipientQueryDTO(reportedRecipientQuery);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        pagingAndSorting.setSortedBy("sendingStarted");

        List<ReportedMessage> searchedReportedMessages =
                reportedMessageDAO.findBySearchCriteria(reportedMessageQuery, pagingAndSorting);

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

        ReportedMessageQueryDTO reportedMessageQuery = new ReportedMessageQueryDTO();
        reportedMessageQuery.setOrganizationOid("1.2.246.562.10.00000000001");
        ReportedRecipientQueryDTO reportedRecipientQuery = new ReportedRecipientQueryDTO();
        reportedMessageQuery.setSearchArgument("Testi Oppilas");
        reportedRecipientQuery.setRecipientName("Testi Oppilas");
        reportedMessageQuery.setReportedRecipientQueryDTO(reportedRecipientQuery);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        pagingAndSorting.setSortedBy("sendingStarted");

        List<ReportedMessage> searchedReportedMessages =
                reportedMessageDAO.findBySearchCriteria(reportedMessageQuery, pagingAndSorting);

        assertNotNull(searchedReportedMessages);
        assertTrue(1 <= searchedReportedMessages.size());
    }

    @Test
    public void testReportedMessageFoundBySearchArgument() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedRecipient reportedRecipient =
                RaportointipalveluTestData.getReportedRecipient(reportedMessage);
        Set<ReportedRecipient> recipients = new HashSet<ReportedRecipient>();
        recipients.add(reportedRecipient);
        reportedMessage.setReportedRecipients(recipients);
        reportedMessageDAO.insert(reportedMessage);

        ReportedMessageQueryDTO reportedMessageQuery = new ReportedMessageQueryDTO();
        reportedMessageQuery.setOrganizationOid("1.2.246.562.10.00000000001");
        ReportedRecipientQueryDTO reportedRecipientQuery = new ReportedRecipientQueryDTO();
        reportedMessageQuery.setSearchArgument("Koekutsu");
        reportedRecipientQuery.setRecipientName("Koekutsu");
        reportedMessageQuery.setReportedRecipientQueryDTO(reportedRecipientQuery);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        pagingAndSorting.setSortedBy("sendingStarted");

        List<ReportedMessage> searchedReportedMessages =
                reportedMessageDAO.findBySearchCriteria(reportedMessageQuery, pagingAndSorting);

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

        ReportedMessageQueryDTO reportedMessageQuery = new ReportedMessageQueryDTO();
        reportedMessageQuery.setOrganizationOid("1.2.246.562.10.00000000001");
        ReportedRecipientQueryDTO reportedRecipientQuery = new ReportedRecipientQueryDTO();
        reportedRecipientQuery.setRecipientEmail("ei.loydy@sposti.fi");
        reportedMessageQuery.setReportedRecipientQueryDTO(reportedRecipientQuery);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        pagingAndSorting.setSortedBy("sendingStarted");

        List<ReportedMessage> searchedReportedMessages =
                reportedMessageDAO.findBySearchCriteria(reportedMessageQuery, pagingAndSorting);

        assertNotNull(searchedReportedMessages);
        assertTrue(searchedReportedMessages.size() == 0);
    }

    @Test
    public void testReportedMessageFoundBySenderOidAndProcessWhenProcessIsNotNull() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedRecipient reportedRecipient =
                RaportointipalveluTestData.getReportedRecipient(reportedMessage);
        Set<ReportedRecipient> recipients = new HashSet<ReportedRecipient>();
        recipients.add(reportedRecipient);
        reportedMessage.setReportedRecipients(recipients);
        reportedMessageDAO.insert(reportedMessage);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        List<ReportedMessage> searchedReportedMessages =
                reportedMessageDAO.findBySenderOidAndProcess("1.2.246.562.24.42645159413", "Hakuprosessi", pagingAndSorting);

        assertNotNull(searchedReportedMessages);
        assertEquals(searchedReportedMessages.size(), 1);

        ReportedMessage result = searchedReportedMessages.get(0);
        assertEquals("1.2.246.562.24.42645159413", result.getSenderOid());
        assertEquals("Hakuprosessi", result.getProcess());
    }

    @Test
    public void testReportedMessageFoundBySenderOidAndProcessWhenProcessIsNull() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedRecipient reportedRecipient =
                RaportointipalveluTestData.getReportedRecipient(reportedMessage);
        Set<ReportedRecipient> recipients = new HashSet<ReportedRecipient>();
        recipients.add(reportedRecipient);
        reportedMessage.setReportedRecipients(recipients);
        reportedMessageDAO.insert(reportedMessage);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        List<ReportedMessage> searchedReportedMessages =
                reportedMessageDAO.findBySenderOid("1.2.246.562.24.42645159413", pagingAndSorting);

        assertNotNull(searchedReportedMessages);
        assertEquals(searchedReportedMessages.size(), 1);

        ReportedMessage result = searchedReportedMessages.get(0);
        assertEquals("1.2.246.562.24.42645159413", result.getSenderOid());
        assertEquals("Hakuprosessi", result.getProcess());
    }

    @Test
    public void testReportedMessageNotFoundBySenderOidAndProcessWhenProcessIsNotNull() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedRecipient reportedRecipient =
                RaportointipalveluTestData.getReportedRecipient(reportedMessage);
        Set<ReportedRecipient> recipients = new HashSet<ReportedRecipient>();
        recipients.add(reportedRecipient);
        reportedMessage.setReportedRecipients(recipients);
        reportedMessageDAO.insert(reportedMessage);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        List<ReportedMessage> searchedReportedMessages =
                reportedMessageDAO.findBySenderOidAndProcess("1.2.246.562.24.42645159413", "Osoitetietojarjestelma", pagingAndSorting);

        assertNotNull(searchedReportedMessages);
        assertEquals(searchedReportedMessages.size(), 0);
    }

    @Test
    public void testReportedMessageNotFoundBySenderOidAndProcessWhenProcessIsPartial() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedRecipient reportedRecipient =
                RaportointipalveluTestData.getReportedRecipient(reportedMessage);
        Set<ReportedRecipient> recipients = new HashSet<ReportedRecipient>();
        recipients.add(reportedRecipient);
        reportedMessage.setReportedRecipients(recipients);
        reportedMessageDAO.insert(reportedMessage);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        List<ReportedMessage> searchedReportedMessages =
                reportedMessageDAO.findBySenderOidAndProcess("1.2.246.562.24.42645159413", "Haku", pagingAndSorting);

        assertNotNull(searchedReportedMessages);
        assertEquals(searchedReportedMessages.size(), 0);
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
        Long messageID = new Long(2013121452);
        ReportedMessage reportedMessage = reportedMessageDAO.read(messageID);

        assertNull(reportedMessage);
    }

    @Test
    public void testNumberOfRecordsMatchesBySearchingOid() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessageDAO.insert(reportedMessage);

        Long lkm = reportedMessageDAO.findNumberOfReportedMessages("1.2.246.562.10.00000000001");

        assertNotNull(lkm);
        assertNotEquals(new Long(0), lkm);
    }

    @Test
    public void testNumberOfRecordsMatchesBySearchingArgument() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessageDAO.insert(reportedMessage);

        ReportedMessageQueryDTO query = new ReportedMessageQueryDTO();

        ReportedRecipientQueryDTO reportedRecipientQuery = new ReportedRecipientQueryDTO();
        reportedRecipientQuery.setRecipientName("Koekutsu");

        query.setReportedRecipientQueryDTO(reportedRecipientQuery);
        query.setSearchArgument("Koekutsu");
        query.setOrganizationOid("1.2.246.562.10.00000000001");

        Long lkm = reportedMessageDAO.findNumberOfReportedMessage(query);

        assertNotNull(lkm);
        assertNotEquals(new Long(0), lkm);
    }
}
