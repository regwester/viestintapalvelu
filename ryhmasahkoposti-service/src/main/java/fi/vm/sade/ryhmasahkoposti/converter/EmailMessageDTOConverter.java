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
package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachmentDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageReplacement;

@Component
public class EmailMessageDTOConverter {
    public List<EmailMessageDTO> convert(List<ReportedMessage> reportedMessages) {
        List<EmailMessageDTO> emailMessageDTOs = new ArrayList<>();

        for (ReportedMessage reportedMessage : reportedMessages) {
            EmailMessageDTO emailMessageDTO = convertToEmailMessageDTO(reportedMessage);
            emailMessageDTOs.add(emailMessageDTO);
        }

        return emailMessageDTOs;
    }

    public EmailMessageDTO convert(ReportedMessage reportedMessage, List<ReportedAttachment> reportedAttachments,
                                   List<ReportedMessageReplacement> reportedMessageReplacements) {

        EmailMessageDTO emailMessageDTO = convertToEmailMessageDTO(reportedMessage);
        emailMessageDTO.setAttachments(convertEmailAttachmentDTO(reportedAttachments));
        emailMessageDTO.setMessageReplacements(convertMessageReplacementDTO(reportedMessageReplacements));

        return emailMessageDTO;
    }

    private List<ReplacementDTO> convertMessageReplacementDTO(List<ReportedMessageReplacement> reportedMessageReplacements) {
        List<ReplacementDTO> replacements = new ArrayList<>();
        if (reportedMessageReplacements == null) {
            return replacements;
        }

        for (ReportedMessageReplacement reportedMessageReplacement : reportedMessageReplacements) {
            replacements.add(convert(reportedMessageReplacement, new ReplacementDTO()));
        }

        return replacements;
    }

    public ReplacementDTO convert(ReportedMessageReplacement from, ReplacementDTO to) {
        to.setName(from.getName());
        to.setDefaultValue(from.getDefaultValue());
        to.setId(from.getId());
        return to;
    }

    private List<EmailAttachment> convertEmailAttachmentDTO(List<ReportedAttachment> reportedAttachments) {
        List<EmailAttachment> attachments = new ArrayList<>();
        for (ReportedAttachment reportedAttachment : reportedAttachments) {
            attachments.add(convert(reportedAttachment, new EmailAttachmentDTO()));
        }
        return attachments;
    }

    public EmailAttachment convert(ReportedAttachment from, EmailAttachmentDTO to) {
        to.setAttachmentID(from.getId());
        to.setName(from.getAttachmentName());
        to.setData(from.getAttachment());
        to.setContentType(from.getContentType());
        return to;
    }

    private EmailMessageDTO convertToEmailMessageDTO(ReportedMessage reportedMessage) {
        EmailMessageDTO emailMessageDTO = new EmailMessageDTO();

        emailMessageDTO.setMessageID(reportedMessage.getId());
        emailMessageDTO.setSubject(reportedMessage.getSubject());
        emailMessageDTO.setFrom(reportedMessage.getSenderEmail());
        emailMessageDTO.setSender(reportedMessage.getSenderDisplayText());
        emailMessageDTO.setStartTime(reportedMessage.getSendingStarted());
        emailMessageDTO.setEndTime(reportedMessage.getSendingEnded());
        emailMessageDTO.setCallingProcess(reportedMessage.getProcess());
        emailMessageDTO.setReplyTo(reportedMessage.getReplyToEmail());
        emailMessageDTO.setBody(reportedMessage.getMessage());
        emailMessageDTO.setType(reportedMessage.getType());
        if (reportedMessage.getTemplateId() != null) {
            emailMessageDTO.setTemplateId(""+reportedMessage.getTemplateId());
        }

        if (reportedMessage.getHtmlMessage() == null || reportedMessage.getHtmlMessage().isEmpty()
                || reportedMessage.getHtmlMessage().equalsIgnoreCase("html")) {
            emailMessageDTO.setHtml(true);
        } else {
            emailMessageDTO.setHtml(false);
        }

        return emailMessageDTO;
    }
}
