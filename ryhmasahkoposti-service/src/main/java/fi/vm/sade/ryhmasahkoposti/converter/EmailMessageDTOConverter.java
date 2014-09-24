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
        List<EmailMessageDTO> emailMessageDTOs = new ArrayList<EmailMessageDTO>();

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

        List<ReplacementDTO> replacements = new ArrayList<ReplacementDTO>();
        if (reportedMessageReplacements == null)
            return replacements;

        for (ReportedMessageReplacement reportedMessageReplacement : reportedMessageReplacements) {

            ReplacementDTO replacementDTO = new ReplacementDTO();
            replacementDTO.setName(reportedMessageReplacement.getName());
            replacementDTO.setDefaultValue(reportedMessageReplacement.getDefaultValue());
            replacementDTO.setId(reportedMessageReplacement.getId());
            replacements.add(replacementDTO);
        }

        return replacements;
    }

    private List<EmailAttachment> convertEmailAttachmentDTO(List<ReportedAttachment> reportedAttachments) {
        List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
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

        if (reportedMessage.getHtmlMessage() == null || reportedMessage.getHtmlMessage().isEmpty()
                || reportedMessage.getHtmlMessage().equalsIgnoreCase("html")) {
            emailMessageDTO.setHtml(true);
        } else {
            emailMessageDTO.setHtml(false);
        }

        return emailMessageDTO;
    }
}
