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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author migar1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailRecipient implements AttachmentContainer {
    private String oid = "";
    private String oidType = "";
    private String email = "";
    private String languageCode = "FI";
    private String name;
    /**
     * List of recipient replacements
     */
    private List<ReportedRecipientReplacementDTO> recipientReplacements;
    private List<EmailAttachment> attachments = new LinkedList<>();
    private List<AttachmentResponse> attachInfo = new LinkedList<>();

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setOidType(String oidType) {
        this.oidType = oidType;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

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

    public EmailRecipient(String oid, String oidType, String email, String languageCode, List<ReportedRecipientReplacementDTO> recipientReplacements) {
        this(oid, oidType, email, languageCode);
        this.recipientReplacements = recipientReplacements;
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

    public List<ReportedRecipientReplacementDTO> getRecipientReplacements() {
        return recipientReplacements;
    }

    public void setRecipientReplacements(List<ReportedRecipientReplacementDTO> recipientReplacements) {
        this.recipientReplacements = recipientReplacements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmailAttachment> getAttachments() {
        return attachments;
    }

    @Override
    public void addAttachInfo(AttachmentResponse attachInfo) {
        if (this.attachInfo == null) {
            this.attachInfo = new ArrayList<>();
        }
        this.attachInfo.add(attachInfo);
    }

    public void setAttachments(List<EmailAttachment> attachments) {
        this.attachments = attachments;
    }

    public List<AttachmentResponse> getAttachInfo() {
        return attachInfo;
    }

    public void setAttachInfo(List<AttachmentResponse> attachInfo) {
        this.attachInfo = attachInfo;
    }

    @Override
    public String toString() {
        return "EmailRecipient [oid=" + oid + ", oidType=" + oidType + ", email=" + email + ", languageCode=" + languageCode + ", name=" + name
                + ", recipientReplacements=" + recipientReplacements + "]";
    }
}
