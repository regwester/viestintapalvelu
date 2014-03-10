package fi.vm.sade.ryhmasahkoposti.api.dto;


public class EmailResponse {
	private EmailRecipient header;	
//	private String recipient;
    private String status;
    private String subject;
	private String nbrOfAttachements;
	
	public EmailResponse() {
//		recipient  = "";
		status = "";
		subject = "";
	}
			
	public EmailResponse(/*String recipient, */String status, String subject) {
		super();
		this.header = new EmailRecipient("");		
//        this.recipient = recipient;
		this.status = status;
		this.subject = subject;
		this.nbrOfAttachements = "0";
	}

	public EmailResponse(EmailRecipient header, /*String recipient, */String status, String subject, String nbrOfAttachements) {
		super();
		this.header = header;		
//        this.recipient = recipient;
		this.status = status;
		this.subject = subject;
		this.nbrOfAttachements = nbrOfAttachements;
	}

    public EmailRecipient getHeader() {
		return header;
	}

//	public String getRecipient() {
//		return recipient;
//	}
//
	public String getStatus() {
		return status;
	}

	public String getSubject() {
		return subject;
	}

	public String getNbrOfAttachements() {
		return nbrOfAttachements;
	}

	@Override
	public String toString() {
		return "EmailResponse [status=" + status
				+ ", subject=" + subject + ", nbrOfAttachements="
				+ nbrOfAttachements + "]";
	}	
}
