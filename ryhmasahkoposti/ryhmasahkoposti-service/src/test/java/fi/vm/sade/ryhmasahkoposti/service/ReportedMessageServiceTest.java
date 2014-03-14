package fi.vm.sade.ryhmasahkoposti.service;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedRecipientQueryDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.service.impl.ReportedMessageServiceImpl;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class ReportedMessageServiceTest {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	@Mock
	ReportedMessageDAO mockedReportedMessageDAO;
	
	private ReportedMessageService reportedMessageService;

	@Before
	public void setup() {
		reportedMessageService = new ReportedMessageServiceImpl(mockedReportedMessageDAO);
	}
	
	@Test
	public void testReportedMessageSaveIsSuccesful() throws IOException {		
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		
		ReportedMessage savedReportedMessage = RaportointipalveluTestData.getReportedMessage();
		savedReportedMessage.setId(new Long(1));
		savedReportedMessage.setVersion(new Long(0));
		
		when(mockedReportedMessageDAO.insert(reportedMessage)).thenReturn(savedReportedMessage);
		savedReportedMessage = reportedMessageService.saveReportedMessage(reportedMessage);
		
		assertNotNull(savedReportedMessage.getId());
		assertNotNull(savedReportedMessage.getVersion());
	}

	@Test
	public void testGetReportedMessages() throws IOException {
		List<ReportedMessage> reportedMessages = new ArrayList<ReportedMessage>();
		reportedMessages.add(RaportointipalveluTestData.getReportedMessage());
		
		when(mockedReportedMessageDAO.findAll(any(ReportedMessageQueryDTO.class), 
		    any(PagingAndSortingDTO.class))).thenReturn(reportedMessages);
		
		ReportedMessageQueryDTO query = new ReportedMessageQueryDTO();
		
        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        pagingAndSorting.setSortedBy("sendingStarted");
        
		reportedMessages = reportedMessageService.getReportedMessages(query, pagingAndSorting);
		
		assertNotNull(reportedMessages);
		assertNotEquals(0, reportedMessages.size());
	}

	@Test
	public void testFindBySearchCriteriaIsSuccesful() throws IOException {
		List<ReportedMessage> mockedReportedMessages = new ArrayList<ReportedMessage>();
		mockedReportedMessages.add(RaportointipalveluTestData.getReportedMessage());
					
		ReportedMessageQueryDTO reportedMessageQuery = new ReportedMessageQueryDTO();
        ReportedRecipientQueryDTO reportedRecipientQuery = new ReportedRecipientQueryDTO();
        reportedRecipientQuery.setRecipientEmail("vastaan.ottaja@sposti.fi");
        reportedMessageQuery.setReportedRecipientQueryDTO(reportedRecipientQuery);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        pagingAndSorting.setSortedBy("sendingStarted");
        
		when(mockedReportedMessageDAO.findBySearchCriteria(
		    reportedMessageQuery, pagingAndSorting)).thenReturn(mockedReportedMessages);
        
		List<ReportedMessage> reportedMessages = 
		    reportedMessageService.getReportedMessages(reportedMessageQuery, pagingAndSorting);
		
		assertNotNull(reportedMessages);
		assertNotEquals(0, reportedMessages.size());		
	}
	
	@Test
	public void testGetReportedMessageByPrimaryKey() throws IOException {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		reportedMessage.setId(new Long(1));
		
		when(mockedReportedMessageDAO.read(any(Long.class))).thenReturn(reportedMessage);
		
		reportedMessage = reportedMessageService.getReportedMessage(new Long(1));
		
		assertNotNull(reportedMessage);
	}

	@Test
	public void testGetReportedMessageNotFoundByPrimaryKey() {		
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		reportedMessage.setId(new Long(1));
		
		when(mockedReportedMessageDAO.read(new Long(1))).thenReturn(reportedMessage);
		
		reportedMessage = reportedMessageService.getReportedMessage(new Long(10));
		
		assertNull(reportedMessage);
	}

	@Test
	public void testUpdateReportedMessage() throws IOException {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		
		doAnswer(new Answer<Object>() {
		    public Object answer(InvocationOnMock invocation) {
		        @SuppressWarnings("unused")
				Object[] args = invocation.getArguments();
		        return null;
		    }})
		.when(mockedReportedMessageDAO).update(reportedMessage);
	 
		reportedMessage.setSendingEnded(new Date());
		reportedMessageService.updateReportedMessage(reportedMessage);
	}
}
