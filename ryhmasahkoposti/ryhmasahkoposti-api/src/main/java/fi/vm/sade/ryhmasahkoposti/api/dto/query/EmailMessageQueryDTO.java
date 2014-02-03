package fi.vm.sade.ryhmasahkoposti.api.dto.query;

public class EmailMessageQueryDTO {
	private String searchArgument;
	private EmailRecipientQueryDTO emailRecipientQuery;
	
	public String getSearchArgument() {
		return searchArgument;
	}

	public void setSearchArgument(String searchArgument) {
		this.searchArgument = searchArgument;
	}

	public EmailRecipientQueryDTO getEmailRecipientQueryDTO() {
		return emailRecipientQuery;
	}

	public void setEmailRecipientQueryDTO(EmailRecipientQueryDTO emailRecipientQuery) {
		this.emailRecipientQuery = emailRecipientQuery;
	}	
}
