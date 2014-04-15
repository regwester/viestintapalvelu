package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.common.util.MessageUtil;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MessageUtil.class)
public class ReportedMessageDTOConverterTest {
    private ReportedMessageDTOConverter reportedMessageDTOConverter;
    
    @Before
    public void setup() {
        this.reportedMessageDTOConverter = new ReportedMessageDTOConverter();
    }

	@Test
	public void testConvertListOfReportedMessage() {
		List<ReportedMessage> mockedReportedMessages = new ArrayList<ReportedMessage>();
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		reportedMessage.setId(new Long(1));
		reportedMessage.setVersion(new Long(0));
		
		Set<ReportedRecipient> reportedRecipients = new HashSet<ReportedRecipient>();
		ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient();
		reportedRecipient.setReportedMessage(reportedMessage);
		reportedRecipients.add(reportedRecipient);

		reportedMessage.setReportedRecipients(reportedRecipients);
		mockedReportedMessages.add(reportedMessage);
		
		List<ReportedMessageDTO> reportedMessageDTOs = reportedMessageDTOConverter.convert(mockedReportedMessages);
		
		assertNotNull(reportedMessageDTOs);
		assertTrue(reportedMessageDTOs.size() == 1);
	}

	@Test
	public void testConvertListOfReportedMessageAndNumberOfFailed() {
		PowerMockito.mockStatic(MessageUtil.class);
		PowerMockito.when(MessageUtil.getMessage(
			"ryhmasahkoposti.lahetys_epaonnistui", new Object[]{new Long(2)})).thenReturn("2 lahetystä epäonnistui");

		List<ReportedMessage> mockedReportedMessages = new ArrayList<ReportedMessage>();
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		reportedMessage.setId(new Long(1));
		reportedMessage.setVersion(new Long(0));
		
		Set<ReportedRecipient> reportedRecipients = new HashSet<ReportedRecipient>();
		ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient();
		reportedRecipient.setReportedMessage(reportedMessage);
		reportedRecipients.add(reportedRecipient);

		reportedMessage.setReportedRecipients(reportedRecipients);
		mockedReportedMessages.add(reportedMessage);
		
		SendingStatusDTO sendingStatusDTO = RaportointipalveluTestData.getSendingStatusDTO();
		Map<Long, SendingStatusDTO> sendingStatuses = new HashMap<Long, SendingStatusDTO>();
		sendingStatuses.put(new Long(1), sendingStatusDTO);
		
		List<ReportedMessageDTO> reportedMessageDTOs = 
			reportedMessageDTOConverter.convert(mockedReportedMessages, sendingStatuses);
		
		assertNotNull(reportedMessageDTOs);
		assertTrue(reportedMessageDTOs.size() == 1);
		assertNotNull(reportedMessageDTOs.get(0).getStatusReport());
		assertTrue(!reportedMessageDTOs.get(0).getStatusReport().isEmpty());
	}

	@Test
	public void testConvertReportedMessageListOfReportedAttachment() {
		PowerMockito.mockStatic(MessageUtil.class);
		PowerMockito.when(MessageUtil.getMessage(
			"ryhmasahkoposti.lahetys_epaonnistui", new Object[]{new Long(2)})).thenReturn("2 lahetystä epäonnistui");

		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		
		Set<ReportedRecipient> reportedRecipients = new HashSet<ReportedRecipient>();
		reportedRecipients.add(RaportointipalveluTestData.getReportedRecipient());
		reportedMessage.setReportedRecipients(reportedRecipients);
		
		List<ReportedAttachment> reportedAttachments = new ArrayList<ReportedAttachment>();
		reportedAttachments.add(RaportointipalveluTestData.getReportedAttachment());
		
		SendingStatusDTO sendingStatusDTO = RaportointipalveluTestData.getSendingStatusDTO();
				
		ReportedMessageDTO reportedMessageDTO = 
			reportedMessageDTOConverter.convert(reportedMessage, reportedAttachments, sendingStatusDTO);
		
		assertNotNull(reportedMessageDTO);
		assertEquals(reportedMessage.getId(), reportedMessageDTO.getMessageID());
		assertEquals(reportedMessage.getMessage(), reportedMessageDTO.getBody());
		assertTrue(reportedMessageDTO.getAttachments().size() > 0);
		assertNotNull(reportedMessageDTO.getAttachments().get(0).getName());
	}

}
