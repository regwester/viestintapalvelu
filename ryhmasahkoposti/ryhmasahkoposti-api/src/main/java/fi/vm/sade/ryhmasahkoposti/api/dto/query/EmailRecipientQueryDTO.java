package fi.vm.sade.ryhmasahkoposti.api.dto.query;

public class EmailRecipientQueryDTO {
	private String recipientOid;
	private String recipientSocialSecurityID;
	private String recipientEmail;
	private String recipientName;
	
	public String getRecipientOid() {
		return recipientOid;
	}
	
	public void setRecipientOid(String recipientOid) {
		this.recipientOid = recipientOid;
	}
	
	public String getRecipientSocialSecurityID() {
		return recipientSocialSecurityID;
	}

	public void setRecipientSocialSecurityID(String recipientSocialSecurityID) {
		this.recipientSocialSecurityID = recipientSocialSecurityID;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}
	
	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}
	
	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
}
