package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.converter.EmailMessageDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.EmailMessageQueryDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.EmailRecipientDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedAttachmentConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedMessageConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedMessageDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedRecipientConverter;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;
import fi.vm.sade.ryhmasahkoposti.service.ReportedAttachmentService;
import fi.vm.sade.ryhmasahkoposti.service.ReportedMessageAttachmentService;
import fi.vm.sade.ryhmasahkoposti.service.ReportedMessageService;
import fi.vm.sade.ryhmasahkoposti.service.ReportedRecipientService;

@Service
@Transactional(readOnly=true)
public class GroupEmailReportingServiceImpl implements GroupEmailReportingService {
	private ReportedMessageService reportedMessageService;
	private ReportedRecipientService reportedRecipientService;
	private ReportedAttachmentService reportedAttachmentService;
	private ReportedMessageAttachmentService reportedMessageAttachmentService;

	@Autowired
	public GroupEmailReportingServiceImpl(ReportedMessageService reportedMessageService,
		ReportedRecipientService reportedRecipientService, ReportedAttachmentService reportedAttachmentService, 
		ReportedMessageAttachmentService reportedMessageAttachmentService) {
		this.reportedMessageService = reportedMessageService;
		this.reportedRecipientService = reportedRecipientService;
		this.reportedAttachmentService = reportedAttachmentService;
		this.reportedMessageAttachmentService = reportedMessageAttachmentService;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Long addSendingGroupEmail(EmailData emailData) throws IOException {
		ReportedMessage reportedMessage = ReportedMessageConverter.convert(emailData.getEmail());
			
		ReportedMessage savedReportedMessage = 
			reportedMessageService.saveReportedMessage(reportedMessage);
	
		List<ReportedAttachment> reportedAttachments = reportedAttachmentService.getReportedAttachments(
			emailData.getEmail().getAttachInfo());		
		reportedMessageAttachmentService.saveReportedMessageAttachments(savedReportedMessage, reportedAttachments);
	
		Set<ReportedRecipient> reportedRecipients = ReportedRecipientConverter.convert(
			savedReportedMessage, emailData.getRecipient());
		reportedRecipientService.saveReportedRecipients(reportedRecipients);		
		
		return savedReportedMessage.getId();
	}

	@Override
	public EmailMessageDTO getMessage(Long messageID) {
		ReportedMessage reportedMessage = reportedMessageService.getReportedMessage(messageID);
		List<ReportedAttachment> reportedAttachments = 
			reportedAttachmentService.getReportedAttachments(reportedMessage.getReportedMessageAttachments());
		return EmailMessageDTOConverter.convert(reportedMessage, reportedAttachments);
	}

	@Override
	public ReportedMessageDTO getReportedMessage(Long messageID) {
		ReportedMessage reportedMessage = reportedMessageService.getReportedMessage(messageID);
				
		SendingStatusDTO sendingStatus = reportedRecipientService.getSendingStatusOfRecipients(messageID);
		
		List<ReportedAttachment> reportedAttachments = 
			reportedAttachmentService.getReportedAttachments(reportedMessage.getReportedMessageAttachments());
		
		ReportedMessageDTO reportedMessageDTO = ReportedMessageDTOConverter.convert(reportedMessage, reportedAttachments);
		reportedMessageDTO.setEndTime(sendingStatus.getSendingEnded());
		
		return reportedMessageDTO;
	}
	
	@Override
	public List<ReportedMessageDTO> getReportedMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReportedMessageDTO> getReportedMessages(String searchArgument) {
		EmailMessageQueryDTO query = EmailMessageQueryDTOConverter.convert(searchArgument);
		List<ReportedMessage> reportedMessages = reportedMessageService.getReportedMessages(query);		
		return ReportedMessageDTOConverter.convert(reportedMessages);
	}

	@Override
	public SendingStatusDTO getSendingStatus(Long messageID) {	
		ReportedMessage reportedMessage = reportedMessageService.getReportedMessage(messageID);
				
		SendingStatusDTO sendingStatus = reportedRecipientService.getSendingStatusOfRecipients(messageID);
		sendingStatus.setMessageID(messageID);
		sendingStatus.setSendingStarted(reportedMessage.getSendingStarted());

		return sendingStatus;
	}
	
	@Override
	public List<EmailRecipientDTO> getUnhandledMessageRecipients(int listSize) {
		List<ReportedRecipient> reportedRecipients = reportedRecipientService.getUnhandledReportedRecipients(listSize);
		return EmailRecipientDTOConverter.convert(reportedRecipients);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean recipientHandledFailure(EmailRecipientDTO recipient, String result) {
		ReportedRecipient reportedRecipient = 
			reportedRecipientService.getReportedRecipient(recipient.getRecipientID());
		reportedRecipient.setFailureReason(result);
		reportedRecipient.setSendingEnded(new Date());
		reportedRecipientService.updateReportedRecipient(reportedRecipient);
		return true;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean recipientHandledSuccess(EmailRecipientDTO recipient, String result) {
		ReportedRecipient reportedRecipient = 
			reportedRecipientService.getReportedRecipient(recipient.getRecipientID());
		reportedRecipient.setSendingSuccesful(result);
		reportedRecipient.setSendingEnded(new Date());
		
		reportedRecipientService.updateReportedRecipient(reportedRecipient);
		
		return true;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Long saveAttachment(FileItem fileItem) throws IOException {
		ReportedAttachment reportedAttachment = ReportedAttachmentConverter.convert(fileItem);
		return reportedAttachmentService.saveReportedAttachment(reportedAttachment);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean startSending(EmailRecipientDTO recipient) {
		ReportedRecipient reportedRecipient = 
			reportedRecipientService.getReportedRecipient(recipient.getRecipientID());

		if (reportedRecipient.getSendingStarted() != null) {
			return false;
		}

		reportedRecipient.setSendingStarted(new Date());
		reportedRecipientService.updateReportedRecipient(reportedRecipient);
		
		return true;
	}
}
