package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
public class EmailMessageDTOConverterTest {
	
	@Test
	public void testEmailMessageDTOConversion() {
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
		
		List<EmailMessageDTO> emailMessageDTOs = EmailMessageDTOConverter.convert(mockedReportedMessages);
		
		assertNotNull(emailMessageDTOs);
		assertTrue(emailMessageDTOs.size() == 1);
	}

	@Test
	public void testEmailMessageDTOConversionWithAttachments() {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		
		Set<ReportedRecipient> reportedRecipients = new HashSet<ReportedRecipient>();
		reportedRecipients.add(RaportointipalveluTestData.getReportedRecipient());
		reportedMessage.setReportedRecipients(reportedRecipients);
		
		List<ReportedAttachment> reportedAttachments = new ArrayList<ReportedAttachment>();
		reportedAttachments.add(RaportointipalveluTestData.getReportedAttachment());
				
		EmailMessageDTO emailMessageDTO = EmailMessageDTOConverter.convert(reportedMessage, reportedAttachments);
		
		assertNotNull(emailMessageDTO);
		assertEquals(reportedMessage.getId(), emailMessageDTO.getMessageID());
		assertEquals(reportedMessage.getMessage(), emailMessageDTO.getBody());
		assertTrue(emailMessageDTO.getAttachments().size() > 0);
		assertNotNull(emailMessageDTO.getAttachments().get(0).getName());
	}
}
