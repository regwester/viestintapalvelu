/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailMessage implements AttachmentContainer {
    private final static Logger log = LoggerFactory.getLogger(EmailMessage.class);

    private String callingProcess = "";
    private String from; // Email FROM
    private String sender; // Email Personal name
    private String replyTo; // Email REPLYTO
    private String senderOid; // The one who is doing the actual sending
    private String organizationOid;
    private String subject;
    private String body;
    private String letterHash;
    private boolean isHtml = false;
    private String charset = EmailConstants.UTF8;
    private List<EmailAttachment> attachments = new LinkedList<EmailAttachment>();
    private List<AttachmentResponse> attachInfo = new LinkedList<AttachmentResponse>();
    private boolean isValid = true;
    private String templateName;
    private String templateId;
    private String languageCode;
    private List<SourceRegister> sourceRegister;
    private String hakuOid; 
    
    public EmailMessage() {
    }

    public EmailMessage(String callingProcess, String from, String replyTo, String subject, String body) {
        this.callingProcess = callingProcess;
        this.from = from;
        this.replyTo = replyTo;
        this.subject = subject;
        this.body = body;
    }

    public EmailMessage(String callingProcess, String from, String replyTo, String subject, String templateName, 
        String languageCode, List<SourceRegister> sourceRegister) {
        this.callingProcess = callingProcess;
        this.from = from;
        this.replyTo = replyTo;
        this.subject = subject;
        this.templateName = templateName;
        this.languageCode = languageCode;
        this.sourceRegister = sourceRegister;
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

    @Override
    public List<EmailAttachment> getAttachments() {
        return attachments;
    }

    @Override
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

    public String getHakuOid() {
        return hakuOid;
    }

    public void setHakuOid(String hakuOid) {
        this.hakuOid = hakuOid;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public boolean isValid() {
        return this.isValid;
    }

    public void setInvalid() {
        this.isValid = false;
    }
    
    public List<SourceRegister> getSourceRegister() {
        return this.sourceRegister;
    }

    public void setSourceRegister(List<SourceRegister> sourceRegister) {
        this.sourceRegister = sourceRegister;
    }

    @Override
    public String toString() {
        return "EmailMessage [callingProcess=" + callingProcess + ", from=" + from + ", sender=" + sender + ", replyTo=" + replyTo + ", senderOid=" + senderOid
                + ", organizationOid=" + organizationOid + ", subject=" + subject + ", body=" + body + ", isHtml=" + isHtml + ", charset=" + charset
                + ", attachments=" + attachments + ", attachInfo=" + attachInfo + ", isValid=" + isValid + ", templateName=" + templateName + ", templateId="
                + templateId + ", languageCode=" + languageCode + ", sourceRegister=" + sourceRegister + ", hakuOid=" + hakuOid + "]";
    }

    public String getLetterHash() {
        return letterHash;
    }

    public void setLetterHash(String letterHash) {
        this.letterHash = letterHash;
    }
}
