package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

public class EmailMessage {
    private final static Logger log = Logger.getLogger(fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage.class.getName()); 

    private EmailHeader header = new EmailHeader();
	private String owner;
	private String sender;
	private String subject;
	private String body;
	private String footer;
	private boolean isHtml = false;
	private String charset = EmailConstants.UTF8;
	List<EmailAttachment> attachments = new LinkedList<EmailAttachment>(); 

	public EmailMessage() {

	}

//	public EmailMessage(String sender, String subject, String body) {
//		this.sender = sender;
//		this.subject = subject;
//		this.body = body;
//	}
	
	protected EmailMessage(EmailHeader header, String owner, String sender, String subject, String body) {
		this.header = header;
		this.owner = owner;
		this.sender = sender;
		this.subject = subject;
		this.body = body;
		this.footer = generateFooter(EmailConstants.EMAIL_FOOTER, header.getLanguageCode());
	}
	
	public void setDeliveryCode(String code) {
		header.setDeliveryCode(code);		
	}

	public void setSendStatus(String status) {
		header.setSendStatus(status);		
	}
	
	public void setHeader(EmailHeader header) {
		this.header = header;
	}

	/**
	 * Sets the message body. 
	 * Note: Searches for <br/> or <p> within the body and sets the isHtml if found. Use 'setHtml(false)' to 
	 * force as plain text or force as HTML
	 * 
	 * @param body
	 */
	public void setBody(String body) {
		if (body != null) {
			String lc = body.toLowerCase();
			isHtml = lc.contains("<br/>") || lc.contains("<p>");
		}
		this.body = body;
	}

	public EmailHeader getHeader() {
		return header;
	}

	public String getBody() {
		return body;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String languageCode) {
		this.footer = generateFooter(EmailConstants.EMAIL_FOOTER, languageCode);		
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}
	
	public String getSender() {
		return sender;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}

	public boolean isHtml() {
		return isHtml;
	}

	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void addEmailAttachement(EmailAttachment attachement) {
		if (this.attachments == null) {
			this.attachments = new ArrayList<EmailAttachment>();
		}
		this.attachments.add(attachement);
	}

	public void setAttachments(List<EmailAttachment> attachments) {
		this.attachments = attachments;
	}

	public List<EmailAttachment> getAttachments() {
		return attachments;
	}

	private String generateFooter(String emailFooter, String lang) {
		String footer = "";
		
        if ((lang == null) || ("".equals(lang)) || ("FI".equalsIgnoreCase(lang))) {
        	lang = "FI";
        	
		} else { if ("SE".equalsIgnoreCase(lang)) {
        	lang = "SE";
			
		} else {
        	lang = "EN";
		}}        
        
        String footerFileName = emailFooter.replace("{LANG}", lang.toUpperCase());
        
        try {
			footer =  readFooter( footerFileName );
			
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "Failed to find footer file:  " + footerFileName + ", " + e.getMessage());        	
		} catch (IOException e) {
			log.log(Level.SEVERE, "Failed to insert footer - it is not valid " + footerFileName + ", " + e.getMessage());        	
		}
        
        return footer;
	}
	
    private String readFooter(String footer) throws FileNotFoundException, IOException {
        InputStream in = getClass().getResourceAsStream(footer);
        if (in == null) {
            throw new FileNotFoundException("Template " + footer + " not found");
        }
        return new String(IOUtils.toByteArray(in));
    }

	@Override
	public String toString() {
		return "EmailMessage [header=" + header + ", owner=" + owner
				+ ", sender=" + sender + ", subject=" + subject + ", body="
				+ body + ", footer=" + footer + ", isHtml=" + isHtml
				+ ", charset=" + charset + ", attachments=" + attachments + "]";
	}

}
