package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EmailMessageDTOConverter.class)
@ContextConfiguration("/test-bundle-context.xml")
public class EmailMessageDTOConverterTest {
	private EmailMessageDTOConverter emailMessageDTOConverter;
	
	@Before
	public void setup() {
	    this.emailMessageDTOConverter = new EmailMessageDTOConverter();
	}
	
	@Test
	public void testEmailMessageDTOConversion() {
		List<ReportedMessage> mockedReportedMessages = new ArrayList<ReportedMessage>();
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		reportedMessage.setId(new Long(1));
		reportedMessage.setVersion(new Long(0));
		reportedMessage.setHtmlMessage("");
		
		Set<ReportedRecipient> reportedRecipients = new HashSet<ReportedRecipient>();
		ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient();
		reportedRecipient.setReportedMessage(reportedMessage);
		reportedRecipients.add(reportedRecipient);

		reportedMessage.setReportedRecipients(reportedRecipients);
		mockedReportedMessages.add(reportedMessage);
		
		List<EmailMessageDTO> emailMessageDTOs = emailMessageDTOConverter.convert(mockedReportedMessages);
		
		assertNotNull(emailMessageDTOs);
		assertTrue(emailMessageDTOs.size() == 1);
		assertTrue(emailMessageDTOs.get(0).isHtml() == false);
	}

	@Test
	public void testEmailMessageDTOConversionWithAttachments() {
		ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
		reportedMessage.setHtmlMessage("html");
		
		Set<ReportedRecipient> reportedRecipients = new HashSet<ReportedRecipient>();
		reportedRecipients.add(RaportointipalveluTestData.getReportedRecipient());
		reportedMessage.setReportedRecipients(reportedRecipients);
		
		List<ReportedAttachment> reportedAttachments = new ArrayList<ReportedAttachment>();
		reportedAttachments.add(RaportointipalveluTestData.getReportedAttachment());
				
		EmailMessageDTO emailMessageDTO = emailMessageDTOConverter.convert(reportedMessage, reportedAttachments);
		
		assertNotNull(emailMessageDTO);
		assertEquals(reportedMessage.getId(), emailMessageDTO.getMessageID());
		assertEquals(reportedMessage.getMessage(), emailMessageDTO.getBody());
		assertTrue(emailMessageDTO.isHtml());
		assertTrue(emailMessageDTO.getAttachments().size() > 0);
		assertNotNull(emailMessageDTO.getAttachments().get(0).getName());
	}
}
