package fi.vm.sade.ryhmasahkoposti.api.dto;

/**
 *
 * @author migar1
 */
public class EmailRecipient {
	private String oid = "";
	private String oidType = "";
	private String email = "";
	private String languageCode = "FI";
	
	public EmailRecipient() {
		super();
	}

	public EmailRecipient(String oid) {
		super();
		this.oid = oid;
	}
	
	public EmailRecipient(String oid, String email) {
		super();
		this.oid = oid;
		this.email = email;
	}

	public EmailRecipient(String oid, String oidType, String email, String languageCode) {
		super();
		this.oid = oid;
		this.oidType = oidType;
		this.email = email;
		this.languageCode = languageCode;
	}

	public String getOid() {
		return oid;
	}
	
	public String getOidType() {
		return oidType;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "EmailRecipient [oid=" + oid
				+ ", oidType=" + oidType + ", email=" + email
				+ ", languageCode=" + languageCode + "]";
	}
}
