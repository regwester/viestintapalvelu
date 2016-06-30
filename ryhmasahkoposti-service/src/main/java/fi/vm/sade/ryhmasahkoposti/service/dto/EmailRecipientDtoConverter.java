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
package fi.vm.sade.ryhmasahkoposti.service.dto;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.*;

/**
 * User: ratamaa Date: 26.9.2014 Time: 15:14
 */
@Component
public class EmailRecipientDtoConverter {

    public EmailRecipientMessage convert(EmailRecipientDTO from, EmailRecipientMessage to, EmailMessageDTO original) {
        to.setMessageReplacements(original.getMessageReplacements());
        to.setAttachInfo(original.getAttachInfo());
        to.setAttachments(original.getAttachments());
        to.setBody(original.getBody());
        to.setCallingProcess(original.getCallingProcess());
        to.setCharset(original.getCharset());
        to.setFrom(original.getFrom());
        to.setHtml(original.isHtml());
        to.setInfected(original.isInfected());
        to.setLanguageCode(original.getLanguageCode());
        to.setOrganizationOid(original.getOrganizationOid());
        to.setReplyTo(original.getReplyTo());
        to.setSender(original.getSender());
        to.setSenderOid(original.getSenderOid());
        to.setSourceRegister(original.getSourceRegister());
        to.setSubject(original.getSubject());
        to.setTemplateName(original.getTemplateName());
        to.setTemplateId(original.getTemplateId());
        to.setType(original.getType());
        to.setVirusChecked(original.isVirusChecked());
        to.setRecipient(from);
        return to;
    }

    public EmailRecipientMessage convertPreview(EmailData from, String toAddress, EmailRecipientMessage to) {
        EmailMessage original = from.getEmail();
        to.setMessageReplacements(from.getReplacements());
        to.setAttachInfo(original.getAttachInfo());
        to.setAttachments(original.getAttachments());
        to.setBody(original.getBody());
        to.setCallingProcess(original.getCallingProcess());
        to.setCharset(original.getCharset());
        to.setFrom(original.getFrom());
        to.setHtml(original.isHtml());
        to.setInfected(false);
        to.setLanguageCode(original.getLanguageCode());
            to.setOrganizationOid(original.getOrganizationOid());
        to.setReplyTo(original.getReplyTo());
        to.setSender(original.getSender());
        to.setSenderOid(original.getSenderOid());
        to.setSourceRegister(original.getSourceRegister());
        to.setSubject(original.getSubject());
        to.setTemplateName(original.getTemplateName());
        to.setTemplateId(original.getTemplateId());
        to.setType((original.getTemplateName() != null || original.getTemplateId() != null) ? "T" : "E");
        to.setVirusChecked(false);
        EmailRecipientDTO recipient = new EmailRecipientDTO();
        EmailRecipient firstRecipient = from.getRecipient().isEmpty() ? null : from.getRecipient().get(0);
        if (firstRecipient != null) {
            recipient.setAttachments(firstRecipient.getAttachments());
            if (firstRecipient.getRecipientReplacements() != null) {
                recipient.setRecipientReplacements(new ArrayList<>(
                        firstRecipient.getRecipientReplacements()));
            }
            recipient.setName(firstRecipient.getName());
        }
        recipient.setEmail(toAddress);
        to.setRecipient(recipient);
        return to;
    }

}
