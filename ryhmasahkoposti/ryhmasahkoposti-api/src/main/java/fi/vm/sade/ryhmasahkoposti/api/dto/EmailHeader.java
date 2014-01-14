package fi.vm.sade.ryhmasahkoposti.api.dto;

/**
 *
 * @author migar1
 */
public class EmailHeader {
	private String callingProcess = "";
	private String oid = "";
	private String oidType = "";
	private String email = "";
	private String languageCode = "FI";
	private String deliveryCode = "";
	private String sendStatus = "";
	
	public EmailHeader() {
		super();
	}

	public EmailHeader(String oid) {
		super();
		this.oid = oid;
	}
	
	public EmailHeader(String oid, String email) {
		super();
		this.oid = oid;
		this.email = email;
	}

	public EmailHeader(String callingProcess, String oid, String oidType, String email, String languageCode) {
		super();
		this.callingProcess = callingProcess;
		this.oid = oid;
		this.oidType = oidType;
		this.email = email;
		this.languageCode = languageCode;
	}

	public String getCallingProcess() {
		return callingProcess;
	}

	public String getOid() {
		return oid;
	}
	
	public String getOidType() {
		return oidType;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}
	
	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
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

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	@Override
	public String toString() {
		return "EmailHeader [callingProcess=" + callingProcess + ", oid=" + oid
				+ ", oidType=" + oidType + ", email=" + email
				+ ", languageCode=" + languageCode + ", deliveryCode="
				+ deliveryCode + ", sendStatus=" + sendStatus + "]";
	}

}
