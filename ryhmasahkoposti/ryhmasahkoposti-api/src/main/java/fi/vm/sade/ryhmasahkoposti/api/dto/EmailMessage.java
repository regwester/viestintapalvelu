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

	private String callingProcess = "";
	private String ownerEmail;
	private String senderEmail; // from
	private String replyToAddress;
	private String senderOid;
	private String senderOidType;	
	private String subject;
	private String body;
	private String footer;
	private boolean isHtml = false;
	private String charset = EmailConstants.UTF8;
	List<EmailAttachment> attachments = new LinkedList<EmailAttachment>(); 
	List<AttachmentResponse> attachInfo = new LinkedList<AttachmentResponse>();

	public EmailMessage() {}

	public EmailMessage(String callingProcess, String ownerEmail, String senderEmail, String senderOid, String senderOidType, String subject, String body) {
		this.callingProcess = callingProcess; 
		this.ownerEmail = ownerEmail;
		this.senderEmail = senderEmail;
		this.senderOid = senderOid; 		
		this.senderOidType = senderOidType; 		
		this.subject = subject;
		this.body = body;
	}
	
	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#setBody(java.lang.String)
     */
	public void setBody(String body) {
		if (body != null) {
			String lc = body.toLowerCase();
			isHtml = lc.contains("<br/>") || lc.contains("<p>");
		}
		this.body = body;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#getCallingProcess()
     */
	public String getCallingProcess() {
		return callingProcess;
	}

	public String getReplyToAddress() {
		return replyToAddress;
	}

	public void setReplyToAddress(String replyToAddress) {
		this.replyToAddress = replyToAddress;
	}

	
	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#getBody()
     */
	public String getBody() {
		return body;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#getFooter()
     */
	public String getFooter() {
		return footer;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#setFooter(java.lang.String)
     */
    public void setFooter(String languageCode) {
		this.footer = generateFooter(EmailConstants.EMAIL_FOOTER, languageCode);		
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#setSubject(java.lang.String)
     */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#getSubject()
     */
	public String getSubject() {
		return subject;
	}
	
	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#getSenderEmail()
     */
	public String getSenderEmail() {
		return senderEmail;
	}
		
	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#getSenderOid()
     */
	public String getSenderOid() {
		return senderOid;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#getSenderOidType()
     */
	public String getSenderOidType() {
		return senderOidType;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#setOwnerEmail(java.lang.String)
     */
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#getOwnerEmail()
     */
	public String getOwnerEmail() {
		return ownerEmail;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#isHtml()
     */
	public boolean isHtml() {
		return isHtml;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#setHtml(boolean)
     */
	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#getCharset()
     */
	public String getCharset() {
		return charset;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#setCharset(java.lang.String)
     */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#addEmailAttachement(fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment)
     */
	public void addEmailAttachement(EmailAttachment attachement) {
		if (this.attachments == null) {
			this.attachments = new ArrayList<EmailAttachment>();
		}
		this.attachments.add(attachement);
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#setAttachments(java.util.List)
     */
	public void setAttachments(List<EmailAttachment> attachments) {
		this.attachments = attachments;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#getAttachments()
     */
	public List<EmailAttachment> getAttachments() {
		return attachments;
	}
	
	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#addAttachInfo(fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse)
     */
	public void addAttachInfo(AttachmentResponse attachInfo) {
		if (this.attachInfo == null) {
			this.attachInfo = new LinkedList<AttachmentResponse>();
		}
		this.attachInfo.add(attachInfo);
	}
	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#getAttachInfo()
     */
	public List<AttachmentResponse> getAttachInfo() {
		return attachInfo;
	}

	/* (non-Javadoc)
     * @see fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageInterface#setAttachInfo(java.util.List)
     */
	public void setAttachInfo(List<AttachmentResponse> attachInfo) {
		this.attachInfo = attachInfo;
	}
    public void setCallingProcess(String callingProcess) {
		this.callingProcess = callingProcess;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public void setSenderOid(String senderOid) {
		this.senderOid = senderOid;
	}

	public void setSenderOidType(String senderOidType) {
		this.senderOidType = senderOidType;
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
		return "EmailMessage [callingProcess=" + callingProcess
				+ ", ownerEmail=" + ownerEmail + ", senderEmail=" + senderEmail
				+ ", senderOid=" + senderOid + ", senderOidType="
				+ senderOidType + ", subject=" + subject + ", body=" + body
				+ ", footer=" + footer + ", isHtml=" + isHtml + ", charset="
				+ charset + ", attachments=" + attachments + "]";
	}
}
