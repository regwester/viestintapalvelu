package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailMessage {
    private final static Logger log = LoggerFactory.getLogger(EmailMessage.class);

    private String callingProcess = "";
    private String from; // Email FROM
    private String sender; // Email Name
    private String replyTo; // Email REPLYTO
    private String senderOid; // The one who is doing the actual sending
    private String organizationOid;
    private String subject;
    private String body;
    private String footer;
    private boolean isHtml = false;
    private String charset = EmailConstants.UTF8;
    List<EmailAttachment> attachments = new LinkedList<EmailAttachment>();
    List<AttachmentResponse> attachInfo = new LinkedList<AttachmentResponse>();
    private boolean isValid = true;

    /**
     * Template name
     */
    private String templateName;

    /**
     * Language code
     */
    private String languageCode;

    public EmailMessage() {
    }

    public EmailMessage(String callingProcess, String from, String replyTo, String subject, String body) {
        this.callingProcess = callingProcess;
        this.from = from;
        this.replyTo = replyTo;
        this.subject = subject;
        this.body = body;
    }

    public EmailMessage(String callingProcess, String from, String replyTo, String subject, String templateName, String languageCode) {
        this.callingProcess = callingProcess;
        this.from = from;
        this.replyTo = replyTo;
        this.subject = subject;
        this.templateName = templateName;
        this.languageCode = languageCode;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCallingProcess() {
        return callingProcess;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getBody() {
        return body;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String languageCode) {
        this.footer = generateFooter(EmailConstants.EMAIL_FOOTER, languageCode);
        addFooterToBody();
    }

    private void addFooterToBody() {
        this.body = this.body + "\n" + this.footer;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public String getSenderOid() {
        return senderOid;
    }

    public void setSenderOid(String senderOid) {
        this.senderOid = senderOid;
    }

    public String getOrganizationOid() {
        return organizationOid;
    }

    public void setOrganizationOid(String organizationOid) {
        this.organizationOid = organizationOid;
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

    public void addEmailAttachement(EmailAttachment attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<EmailAttachment>();
        }
        this.attachments.add(attachment);
    }

    public void setAttachments(List<EmailAttachment> attachments) {
        this.attachments = attachments;
    }

    public List<? extends EmailAttachment> getAttachments() {
        return attachments;
    }

    public void addAttachInfo(AttachmentResponse attachInfo) {
        if (this.attachInfo == null) {
            this.attachInfo = new LinkedList<AttachmentResponse>();
        }
        this.attachInfo.add(attachInfo);
    }

    public List<AttachmentResponse> getAttachInfo() {
        return attachInfo;
    }

    public void setAttachInfo(List<AttachmentResponse> attachInfo) {
        this.attachInfo = attachInfo;
    }

    public void setCallingProcess(String callingProcess) {
        this.callingProcess = callingProcess;
    }

    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName
     *            the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return the languageCode
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * @param languageCode
     *            the languageCode to set
     */
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    private String generateFooter(String emailFooter, String lang) {
        String footer = "";

        if ((lang == null) || ("".equals(lang)) || ("FI".equalsIgnoreCase(lang))) {
            lang = "FI";

        } else if ("SE".equalsIgnoreCase(lang) || "SV".equalsIgnoreCase(lang)) {
            // should be SV
            lang = "SE";
        } else {
            lang = "EN";
        }

        String footerFileName = emailFooter.replace("{LANG}", lang.toUpperCase());

        try {
            footer = readFooter(footerFileName);

        } catch (FileNotFoundException e) {
            log.error("Failed to find footer file:  " + footerFileName + ", " + e.getMessage());
        } catch (IOException e) {
            log.error("Failed to insert footer - it is not valid " + footerFileName + ", " + e.getMessage());
        }

        return footer;
    }

    private String readFooter(String footer) throws FileNotFoundException, IOException {
        InputStream in = getClass().getResourceAsStream(footer);
        if (in == null) {
            throw new FileNotFoundException("Template " + footer + " not found");
        }
        return new String(IOUtils.toByteArray(in), "UTF-8");
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setInvalid() {
        this.isValid = false;
    }

    @Override
    public String toString() {
        return "EmailMessage [callingProcess=" + callingProcess + ", from=" + from + ", sender=" + sender + ", replyTo=" +  replyTo + ", senderOid=" + senderOid + ", subject="
                + subject + ", body=" + body + ", footer=" + footer + ", isHtml=" + isHtml + ", charset=" + charset + ", attachments=" + attachments
                + ", attachInfo=" + attachInfo + "]";
    }

}
