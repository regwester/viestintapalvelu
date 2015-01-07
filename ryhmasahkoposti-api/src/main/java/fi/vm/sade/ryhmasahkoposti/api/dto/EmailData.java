/**
 * Copyright (c) 2012 The Finnish Board of Education - Opetushallitus
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailData {

    private List<EmailRecipient> recipient = new LinkedList<EmailRecipient>();
    private List<ReplacementDTO> replacements = new LinkedList<ReplacementDTO>();
    private EmailMessage email = new EmailMessage();

    public EmailData() {
        super();
    }

    public EmailData(List<EmailRecipient> recipient, EmailMessage email) {
        super();
        this.recipient = recipient;
        this.email = email;
    }

    public EmailData(List<EmailRecipient> recipient, List<ReplacementDTO> replacements, EmailMessage email) {
        super();
        this.recipient = recipient;
        this.replacements = replacements;
        this.email = email;
    }

    /**
     * @return the replacements
     */
    public List<ReplacementDTO> getReplacements() {
        return replacements;
    }

    /**
     * @param replacements the replacements to set
     */
    public void setReplacements(List<ReplacementDTO> replacements) {
        this.replacements = replacements;
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

    public void setSenderOid(String senderOid) {
        this.email.setSenderOid(senderOid);
    }

    @Override
    public String toString() {
        return "EmailData [recipient=" + recipient + ", replacements="
                + replacements + ", email=" + email + "]";
    }
}
