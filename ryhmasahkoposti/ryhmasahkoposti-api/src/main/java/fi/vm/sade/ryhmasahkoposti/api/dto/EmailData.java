package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author migar1
 */
public class EmailData {
	
	List<EmailHeader> headers = new LinkedList<EmailHeader>(); 
	EmailMessage email = new EmailMessage();
		
	public EmailData() {
		super();
	}

	public EmailData(List<EmailHeader> headers, EmailMessage email) {
		super();
		this.headers = headers;
		this.email = email;
	}
	
	public List<EmailHeader> getHeaders() {
		return headers;
	}
	public void setHeaders(List<EmailHeader> headers) {
		this.headers = headers;
	}
	public EmailMessage getEmail() {
		return email;
	}
	public void setEmail(EmailMessage email) {
		this.email = email;
	}
	
	
}
