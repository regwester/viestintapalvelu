package fi.vm.sade.ryhmasahkoposti.testdata;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.fileupload.FileItem;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

public class RaportointipalveluTestData {

	public static EmailData getEmailData() {
		EmailData emailData = new EmailData();		
		return emailData;
	}
	
	public static EmailMessage getEmailMessage() {
		EmailMessage emailMessage = new EmailMessage();
		
		emailMessage.setBody("Tämä on koekutsu");
		emailMessage.setCallingProcess("Hakuprosessi");
		emailMessage.setCharset("utf-8");
		emailMessage.setFooter("");
		emailMessage.setHtml(false);
		emailMessage.setReplyToAddress("vastaus.oppilaitos@sposti.fi");
		emailMessage.setOwnerEmail("");
		emailMessage.setSenderEmail("lahettaja.oppilaitos@sposti.fi");
		emailMessage.setSenderOid("1.2.246.562.24.42645159413");
		emailMessage.setSenderOidType("virkailija");
		emailMessage.setSubject("Koekutsu");
		
		return emailMessage;
	}
	
	public static EmailAttachment getEmailAttachment() {
		EmailAttachment emailAttachment = new EmailAttachment();
		
		byte[] attachment = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		emailAttachment.setData(attachment);
		emailAttachment.setName("koekutsu.doc");
		emailAttachment.setContentType("application/pdf");		
		
		return emailAttachment;
	}
	
	public static EmailRecipient getEmailRecipient() {
		EmailRecipient emailRecipient = new EmailRecipient();
		
		emailRecipient.setOid("102030405100");
		emailRecipient.setOidType("oppilas");
		emailRecipient.setEmail("vastaan.ottaja@sposti.fi");
		emailRecipient.setLanguageCode("FI");
		
		return emailRecipient;
	}

	public static EmailRecipientDTO getEmailRecipientDTO() {
		EmailRecipientDTO emailRecipientDTO = new EmailRecipientDTO();
		
		emailRecipientDTO.setOid("1.2.246.562.24.34397748041");
		emailRecipientDTO.setOidType("oppilas");
		emailRecipientDTO.setEmail("vastaan.ottaja@sposti.fi");
		emailRecipientDTO.setLanguageCode("FI");
		
		return emailRecipientDTO;
	
	}

	public static AttachmentResponse getAttachmentResponse(Long id, FileItem fileItem) {
		AttachmentResponse attachmentResponse = new AttachmentResponse();
		
		attachmentResponse.setUuid(id.toString());
		attachmentResponse.setFileName(fileItem.getName());
		attachmentResponse.setContentType(fileItem.getContentType());
		
		return attachmentResponse;
	}
	
	public static ReportedMessage getReportedMessage() {
		ReportedMessage reportedMessage = new ReportedMessage();
		
		reportedMessage.setSubject("Kokekutsu");
		reportedMessage.setProcess("Hakuprosessi");
		reportedMessage.setSenderOid("1.2.246.562.24.42645159413");
		reportedMessage.setSenderOidType("virkailija");
		reportedMessage.setSenderEmail("testi.virkailija@sposti.fi");
		reportedMessage.setReplyToOid("1.2.246.562.24.42645159413");
		reportedMessage.setReplyToOidType("virkailija");
		reportedMessage.setReplyToEmail("testi.virkailija@sposti.fi");
		reportedMessage.setMessage("Tämä on koekutsu");
		reportedMessage.setHtmlMessage("");
		reportedMessage.setCharacterSet("utf-8");
		reportedMessage.setSendingStarted(new Date());
		reportedMessage.setTimestamp(new Date());

		return reportedMessage;
	}
	
	public static ReportedMessageAttachment getReportedMessageAttachment() {		
		ReportedMessageAttachment reportedMessageAttachment = new ReportedMessageAttachment();
		
		reportedMessageAttachment.setId(new Long(400));
		reportedMessageAttachment.setReportedAttachmentID(new Long(100));
		reportedMessageAttachment.setVersion(new Long(0));
		reportedMessageAttachment.setTimestamp(new Date());
		
		return reportedMessageAttachment;
	}

	public static ReportedRecipient getReportedRecipient(ReportedMessage reportedMessage) {
		ReportedRecipient raportoitavaVastaanottaja = new ReportedRecipient();
		
		raportoitavaVastaanottaja.setReportedMessage(reportedMessage);
		raportoitavaVastaanottaja.setRecipientOid("1.2.246.562.24.34397748041");
		raportoitavaVastaanottaja.setRecipientOidType("oppilas");
		raportoitavaVastaanottaja.setSocialSecurityID("");
		raportoitavaVastaanottaja.setRecipientEmail("testi.vastaanottaja@sposti.fi");
		raportoitavaVastaanottaja.setLanguageCode("FI");
		raportoitavaVastaanottaja.setSearchName("Testi Oppilas");
		raportoitavaVastaanottaja.setSendingStarted(new Date());
		raportoitavaVastaanottaja.setSendingEnded(new Date());
		raportoitavaVastaanottaja.setFailureReason("");
		raportoitavaVastaanottaja.setTimestamp(new Date());
		
		return raportoitavaVastaanottaja;
	}

	public static ReportedRecipient getReportedRecipient() {
		ReportedRecipient reportedRecipient = new ReportedRecipient();
		
		reportedRecipient.setRecipientOid("1.2.246.562.24.34397748041");
		reportedRecipient.setRecipientOidType("oppilas");
		reportedRecipient.setSocialSecurityID("");
		reportedRecipient.setRecipientEmail("testi.vastaanottaja@sposti.fi");
		reportedRecipient.setLanguageCode("FI");
		reportedRecipient.setSearchName("Testi Oppilas");
		reportedRecipient.setSendingStarted(null);
		reportedRecipient.setSendingEnded(null);
		reportedRecipient.setFailureReason("");
		reportedRecipient.setTimestamp(new Date());
		
		return reportedRecipient;
	}
	
	public static ReportedAttachment getReportedAttachment() {
		ReportedAttachment raportoitavaLiite = new ReportedAttachment();
		
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		raportoitavaLiite.setAttachmentName("koekutsu.doc");
		raportoitavaLiite.setContentType("application/pdf");
		raportoitavaLiite.setAttachment(sisalto);
		raportoitavaLiite.setTimkestamp(new Date());
		
		return raportoitavaLiite; 		
	}
	
	public static Long getViestiID() {
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		return timestamp.getTime();
	}
}
