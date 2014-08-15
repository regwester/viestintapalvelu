package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.List;

public class ReportedMessageDTO extends EmailMessageDTO {
    private String senderName;
	private List<EmailRecipientDTO> emailRecipients;
	private SendingStatusDTO sendingStatus;
	private String statusReport;
	private String sendingReport;

	public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public List<EmailRecipientDTO> getEmailRecipients() {
		return emailRecipients;
	}
	
	public void setEmailRecipients(List<EmailRecipientDTO> emailRecipients) {
		this.emailRecipients = emailRecipients;
	}
	
	public SendingStatusDTO getSendingStatus() {
		return sendingStatus;
	}

	public void setSendingStatus(SendingStatusDTO sendingStatus) {
		this.sendingStatus = sendingStatus;
	}

	public String getStatusReport() {
		return statusReport;
	}

	public void setStatusReport(String statusReport) {
		this.statusReport = statusReport;
	}

	public String getSendingReport() {
		return sendingReport;
	}

	public void setSendingReport(String sendingReport) {
		this.sendingReport = sendingReport;
	}
}
