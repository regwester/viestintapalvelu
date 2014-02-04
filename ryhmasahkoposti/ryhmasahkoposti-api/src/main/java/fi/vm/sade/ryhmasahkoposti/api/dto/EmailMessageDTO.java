package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.Date;
import java.util.List;

public class EmailMessageDTO extends EmailMessage {
	private Long messageID;
	private Date startTime;
	private Date endTime;
	private List<EmailAttachmentDTO> attachments;
	private List<EmailRecipientDTO> recipients;

	public List<EmailRecipientDTO> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<EmailRecipientDTO> recipients) {
		this.recipients = recipients;
	}


	public Long getMessageID() {
		return messageID;
	}

	public void setMessageID(Long messageID) {
		this.messageID = messageID;
	}

	public List<EmailAttachmentDTO> getAttachmentDTOs() {
		return attachments;
	}

	public void setAttachmentDTOs(List<EmailAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public List<EmailRecipientDTO> getRecipientDTOs() {
		return recipients;
	}

	public void setRecipientDTOs(List<EmailRecipientDTO> recipients) {
		this.recipients = recipients;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
