package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author migar1
 */
public class EmailData {
	
	List<EmailRecipient> headers = new LinkedList<EmailRecipient>(); 
	EmailMessage email = new EmailMessage();
		
	public EmailData() {
		super();
	}

	public EmailData(List<EmailRecipient> headers, EmailMessage email) {
		super();
		this.headers = headers;
		this.email = email;
	}
	
	public List<EmailRecipient> getHeaders() {
		return headers;
	}
	public void setHeaders(List<EmailRecipient> headers) {
		this.headers = headers;
	}
	public EmailMessage getEmail() {
		return email;
	}
	public void setEmail(EmailMessage email) {
		this.email = email;
	}
	
	
}
