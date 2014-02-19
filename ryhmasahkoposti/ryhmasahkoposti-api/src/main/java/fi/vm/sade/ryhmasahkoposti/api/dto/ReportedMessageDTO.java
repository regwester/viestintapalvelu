package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.List;

public class ReportedMessageDTO extends EmailMessageDTO {
	private List<EmailRecipientDTO> emailRecipients;
	private SendingStatusDTO sendingStatus;
	private String statusReport;
	private String sendingReport;

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
