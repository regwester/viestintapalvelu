package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachmentDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;

@Component
public class EmailMessageDTOConverter {
	public static List<EmailMessageDTO> convert(List<ReportedMessage> reportedMessages) {
		List<EmailMessageDTO> emailMessageDTOs = new ArrayList<EmailMessageDTO>();
		
		for (ReportedMessage reportedMessage : reportedMessages) {
			EmailMessageDTO emailMessageDTO = convertToEmailMessageDTO(reportedMessage);
			emailMessageDTO.setRecipientDTOs(convertEmailRecipientDTO(reportedMessage));
			emailMessageDTOs.add(emailMessageDTO);
		}
		
		return emailMessageDTOs;
	}

	public static EmailMessageDTO convert(ReportedMessage reportedMessage, List<ReportedAttachment> reportedAttachments) {
		EmailMessageDTO emailMessageDTO = convertToEmailMessageDTO(reportedMessage);
		emailMessageDTO.setAttachmentDTOs(convertEmailAttachmentDTO(reportedAttachments));
		emailMessageDTO.setRecipients(convertEmailRecipientDTO(reportedMessage));
		
		return emailMessageDTO;
	}
	
	private static List<EmailRecipientDTO> convertEmailRecipientDTO(ReportedMessage reportedMessage) {
		List<EmailRecipientDTO> recipients = new ArrayList<EmailRecipientDTO>();
		
		for (ReportedRecipient reportedRecipient : reportedMessage.getReportedRecipients()) {
			EmailRecipientDTO emailRecipientDTO = new EmailRecipientDTO();
			
			emailRecipientDTO.setRecipientID(reportedRecipient.getId());
			emailRecipientDTO.setSendSuccessfull(reportedRecipient.getSendingSuccesful());
			emailRecipientDTO.setOid(reportedRecipient.getRecipientOid());
			emailRecipientDTO.setEmail(reportedRecipient.getRecipientEmail());
			
			recipients.add(emailRecipientDTO);
		}
		
		return recipients;
	}

	private static List<EmailAttachmentDTO> convertEmailAttachmentDTO(List<ReportedAttachment> reportedAttachments) {
		List<EmailAttachmentDTO> attachments = new ArrayList<EmailAttachmentDTO>();
		
		for (ReportedAttachment reportedAttachment : reportedAttachments) {
			EmailAttachmentDTO attachmentDTO = new EmailAttachmentDTO();
			attachmentDTO.setAttachmentID(reportedAttachment.getId());
			attachmentDTO.setName(reportedAttachment.getAttachmentName());
			attachmentDTO.setData(reportedAttachment.getAttachment());
			attachmentDTO.setContentType(reportedAttachment.getContentType());
			attachments.add(attachmentDTO);
		}
			
		return attachments;
	}

	private static EmailMessageDTO convertToEmailMessageDTO(ReportedMessage reportedMessage) {
		EmailMessageDTO emailMessageDTO = new EmailMessageDTO();
		
		emailMessageDTO.setMessageID(reportedMessage.getId());
		emailMessageDTO.setSubject(reportedMessage.getSubject());
		emailMessageDTO.setSenderEmail(reportedMessage.getSenderEmail());
		emailMessageDTO.setStartTime(reportedMessage.getSendingStarted());
		emailMessageDTO.setEndTime(reportedMessage.getSendingEnded());
		emailMessageDTO.setCallingProcess(reportedMessage.getProcess());
		emailMessageDTO.setReplyToAddress(reportedMessage.getReplyToEmail());
		emailMessageDTO.setBody(reportedMessage.getMessage());
		
		return emailMessageDTO;
	}
}
