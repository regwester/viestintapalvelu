package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachmentDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.common.util.MessageUtil;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

@Component
public class ReportedMessageDTOConverter {
	
	public static List<ReportedMessageDTO> convert(List<ReportedMessage> reportedMessages) {
		List<ReportedMessageDTO> reportedMessageDTOs = new ArrayList<ReportedMessageDTO>();
		
		for (ReportedMessage reportedMessage : reportedMessages) {
			ReportedMessageDTO reportedMessageDTO = new ReportedMessageDTO();			
			convert(reportedMessageDTO, reportedMessage);
			reportedMessageDTOs.add(reportedMessageDTO);
		}
		
		return reportedMessageDTOs;
	}

	public static List<ReportedMessageDTO> convert(List<ReportedMessage> reportedMessages, 
		Map<Long, Long> nbrOfSendingFaileds) {
		List<ReportedMessageDTO> reportedMessageDTOs = new ArrayList<ReportedMessageDTO>();
		
		for (ReportedMessage reportedMessage : reportedMessages) {
			ReportedMessageDTO reportedMessageDTO = new ReportedMessageDTO();			
			
			Long numberOfFailed = nbrOfSendingFaileds.get(reportedMessage.getId());
			convert(reportedMessageDTO, reportedMessage);
			setStatusReport(reportedMessageDTO, numberOfFailed);
			
			reportedMessageDTOs.add(reportedMessageDTO);
		}
		
		return reportedMessageDTOs;
	}
	
	public static ReportedMessageDTO convert(ReportedMessage reportedMessage, 
		List<ReportedAttachment> reportedAttachments, SendingStatusDTO sendingStatusDTO) {
		ReportedMessageDTO reportedMessageDTO = new ReportedMessageDTO();
		
		convert(reportedMessageDTO, reportedMessage);
		reportedMessageDTO.setEmailRecipients(convertEmailRecipientDTO(reportedMessage));
		reportedMessageDTO.setAttachments(convertEmailAttachmentDTO(reportedAttachments));
		reportedMessageDTO.setSendingStatus(sendingStatusDTO);
		
		setSendingReport(reportedMessageDTO, sendingStatusDTO);
		setStatusReport(reportedMessageDTO, sendingStatusDTO.getNumberOfFailedSendings());
				
		return reportedMessageDTO;
	}

	private static void convert(ReportedMessageDTO reportedMessageDTO, ReportedMessage reportedMessage) {
		reportedMessageDTO.setMessageID(reportedMessage.getId());
		reportedMessageDTO.setSubject(reportedMessage.getSubject());
		reportedMessageDTO.setFrom(reportedMessage.getSenderEmail());
		reportedMessageDTO.setStartTime(reportedMessage.getSendingStarted());
		reportedMessageDTO.setEndTime(reportedMessage.getSendingEnded());
		reportedMessageDTO.setCallingProcess(reportedMessage.getProcess());
		reportedMessageDTO.setReplyTo(reportedMessage.getReplyToEmail());
		reportedMessageDTO.setBody(reportedMessage.getMessage());
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

	private static List<EmailAttachment> convertEmailAttachmentDTO(List<ReportedAttachment> reportedAttachments) {
		List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
		
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

	private static void setSendingReport(ReportedMessageDTO reportedMessageDTO, SendingStatusDTO sendingStatusDTO) {
		Long numberOfSuccesfulSendings = new Long(0);
		if (sendingStatusDTO.getNumberOfSuccesfulSendings() != null) {
			numberOfSuccesfulSendings = sendingStatusDTO.getNumberOfSuccesfulSendings();
		}
		
		Long numberOfFailedSendings = new Long(0);
		if (sendingStatusDTO.getNumberOfFailedSendings() != null) {
			numberOfFailedSendings = sendingStatusDTO.getNumberOfFailedSendings();
		}

		Object[] parameters = {numberOfSuccesfulSendings, numberOfFailedSendings};
		reportedMessageDTO.setSendingReport(
			MessageUtil.getMessage("ryhmasahkoposti.lahetys_raportti", parameters));
	}

	private static void setStatusReport(ReportedMessageDTO reportedMessageDTO, Long numberOfFailed) {
		if (numberOfFailed != null && numberOfFailed.compareTo(new Long(0)) > 0) {
			Object[] parameters = {numberOfFailed};
			reportedMessageDTO.setStatusReport(
				MessageUtil.getMessage("ryhmasahkoposti.lahetys_epaonnistui", parameters));
			return;
		}
		
		if (reportedMessageDTO.getStartTime() != null && reportedMessageDTO.getEndTime() == null) {
			reportedMessageDTO.setStatusReport(MessageUtil.getMessage("ryhmasahkoposti.lahetys_kesken"));
			return;
		}
		
		if (reportedMessageDTO.getStartTime() != null && reportedMessageDTO.getEndTime() != null) {
			reportedMessageDTO.setStatusReport(MessageUtil.getMessage("ryhmasahkoposti.lahetys_onnistui"));
		}
		  
		return;
	}
}
