package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

public class ReportedMessageDTOConverterTest {

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
		
		List<ReportedMessageDTO> reportedMessageDTOs = ReportedMessageDTOConverter.convert(mockedReportedMessages);
		
		assertNotNull(reportedMessageDTOs);
		assertTrue(reportedMessageDTOs.size() == 1);
	}

	@Test
	public void testConvertReportedMessageListOfReportedAttachment() {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		
		Set<ReportedRecipient> reportedRecipients = new HashSet<ReportedRecipient>();
		reportedRecipients.add(RaportointipalveluTestData.getReportedRecipient());
		reportedMessage.setReportedRecipients(reportedRecipients);
		
		List<ReportedAttachment> reportedAttachments = new ArrayList<ReportedAttachment>();
		reportedAttachments.add(RaportointipalveluTestData.getReportedAttachment());
				
		ReportedMessageDTO reportedMessageDTO = ReportedMessageDTOConverter.convert(reportedMessage, reportedAttachments);
		
		assertNotNull(reportedMessageDTO);
		assertEquals(reportedMessage.getId(), reportedMessageDTO.getMessageID());
		assertEquals(reportedMessage.getMessage(), reportedMessageDTO.getBody());
		assertTrue(reportedMessageDTO.getAttachments().size() > 0);
		assertNotNull(reportedMessageDTO.getAttachments().get(0).getName());
	}

}
