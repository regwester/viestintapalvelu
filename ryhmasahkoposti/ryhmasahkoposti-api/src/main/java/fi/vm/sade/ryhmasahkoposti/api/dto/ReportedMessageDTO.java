package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.List;

public class ReportedMessageDTO extends EmailMessageDTO {
	private List<EmailRecipientDTO> emailRecipients;
	private String sendingReport;

	public List<EmailRecipientDTO> getEmailRecipients() {
		return emailRecipients;
	}
	
	public void setEmailRecipients(List<EmailRecipientDTO> emailRecipients) {
		this.emailRecipients = emailRecipients;
	}
	
	public String getSendingReport() {
		return sendingReport;
	}

	public void setSendingReport(String sendingReport) {
		this.sendingReport = sendingReport;
	}
}
