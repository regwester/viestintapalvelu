package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author migar1
 */
public class EmailData {
	
	List<EmailRecipient> recipient = new LinkedList<EmailRecipient>(); 
	EmailMessage email = new EmailMessage();
		
	public EmailData() {
		super();
	}

	public EmailData(List<EmailRecipient> recipient, EmailMessage email) {
		super();
		this.recipient = recipient;
		this.email = email;
	}
	
	public List<EmailRecipient> getRecipient() {
		return recipient;
	}
	public void setRecipient(List<EmailRecipient> recipient) {
		this.recipient = recipient;
	}
	public EmailMessage getEmail() {
		return email;
	}
	public void setEmail(EmailMessage email) {
		this.email = email;
	}
	
	
}
