/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.viestintapalvelu.letter.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import fi.vm.sade.ryhmasahkoposti.api.dto.*;
import fi.vm.sade.viestintapalvelu.attachment.impl.AttachmentUri;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.email.TemplateEmailField;
import fi.vm.sade.viestintapalvelu.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.viestintapalvelu.externalinterface.component.EmailComponent;
import fi.vm.sade.viestintapalvelu.letter.LetterEmailService;
import fi.vm.sade.viestintapalvelu.letter.LetterService;
import fi.vm.sade.viestintapalvelu.letter.dto.EmailSendDataDto;
import fi.vm.sade.viestintapalvelu.model.*;
import fi.vm.sade.viestintapalvelu.template.Replacement;
import fi.vm.sade.viestintapalvelu.template.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateService;

/**
 * User: ratamaa
 * Date: 30.9.2014
 * Time: 12:24
 */
@Service
public class LetterEmailServiceImpl implements LetterEmailService {
    public static final String ADDITIONAL_ATTACHMENT_URIS_EMAIL_RECEIVER_PARAMETER = "additionalAttachmentUris";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LetterBatchDAO letterBatchDAO;

    @Autowired
    private EmailComponent emailComponent;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private LetterService letterService;

    @Autowired
    private ObjectMapperProvider objectMapperProvider;

    @Override
    @Transactional
    public void sendEmail(long letterBatchId) {
        LetterBatch letterBatch = letterBatchDAO.read(letterBatchId);
        if (letterBatch == null) {
            throw new NotFoundException("LetterBatch not found by id="+letterBatchId);
        }
        if (letterBatch.getBatchStatus() == null || letterBatch.getBatchStatus() != LetterBatch.Status.ready) {
            throw new IllegalStateException("Can not send email to LetterBatch="+letterBatch.getTemplateId()
                    +" in status="+ letterBatch.getBatchStatus()+". Expecting ready status.");
        }
        if (letterBatch.getEmailHandlingStarted() != null) {
            throw new IllegalStateException("Email handling already stated at "
                    +letterBatch.getEmailHandlingStarted()+" for LetterBatch="+letterBatch.getTemplateId());
        }
        if (letterBatch.getTemplateId() == null) {
            throw new IllegalStateException("LetterBatch="+letterBatch.getTemplateId()+" does not have a templateId!");
        }
        Template template = templateService.findById(letterBatch.getTemplateId());
        if (template == null) {
            throw new NotFoundException("Template not found by id="+letterBatch.getTemplateId());
        }

        letterService.updateBatchProcessingStarted(letterBatchId, LetterService.LetterBatchProcess.EMAIL);

        EmailSendDataDto emailSendData = new EmailSendDataDto();
        buildEmails(letterBatch, template, emailSendData);
        sendEmails(emailSendData);

        letterService.updateBatchProcessingFinished(letterBatchId, LetterService.LetterBatchProcess.EMAIL);
    }

    private void buildEmails(LetterBatch letterBatch, Template template, EmailSendDataDto emailSendData) {
        ObjectMapper mapper = objectMapperProvider.getContext(getClass());
        for (LetterReceivers letterReceiver : letterBatch.getLetterReceivers()) {
            LetterReceiverLetter letter = letterReceiver.getLetterReceiverLetter();
            if (letter == null || letter.getLetter() == null) {
                logger.error("Logical error: trying to send email to non-processed LetterReceivers={}!", letterReceiver.getId());
                continue;
            }

            String languageCode = Optional.fromNullable(letterReceiver.getWantedLanguage())
                    .or(template.getLanguage());

            if (!languageCode.equals(template.getLanguage())) {
                // Get the template in user specific language
                Template languageTemplate = templateService.getTemplateByName( new TemplateCriteriaImpl()
                        .withName(template.getName())
                        .withLanguage(languageCode)
                        .withApplicationPeriod(letterBatch.getApplicationPeriod()), true);
                if (languageTemplate != null) {
                    template = languageTemplate;
                }
            }

            EmailData emailData = emailSendData.getEmailByLanguageCode(languageCode);
            if (emailData == null) {
                emailData = buildEmailData(template, new EmailData(), languageCode, letterBatch.getApplicationPeriod());
                emailSendData.setEmailByLanguageCode(languageCode, emailData);
            }

            EmailRecipient recipient = null;
            if (shouldReceiveEmail(letterReceiver)) {
                try {
                    recipient = buildRecipient(letterReceiver, mapper);
                    emailData.getRecipient().add(recipient);
                } catch (Exception e) {
                    logger.info("Could not handle email sending for LetterReceivers="
                            + letterReceiver.getId() + " reason {}", e);
                    continue;
                }
            } else {
                logger.debug("Skipped email sending to LetterReceivers={}", letterReceiver.getId());
                continue;
            }

            List<AttachmentUri> attachmentUris = new ArrayList<AttachmentUri>();
            for (LetterReceiverLetterAttachment attachment : letter.getAttachments()) {
                attachmentUris.add(AttachmentUri.getLetterReceiverLetterAttachment(attachment.getId()));
            }
            if (!attachmentUris.isEmpty()) {
                recipient.getRecipientReplacements().add(new ReportedRecipientReplacementDTO(
                        ADDITIONAL_ATTACHMENT_URIS_EMAIL_RECEIVER_PARAMETER, attachmentUris));
            }
        }
    }

    private EmailData buildEmailData(Template letterTemplate, EmailData emailData, String languageCode,
                                     String applicationPeriod) {
        EmailMessage message = emailData.getEmail();
        message.setLanguageCode(languageCode);
        message.setTemplateName(letterTemplate.getName());
        message.setHakuOid(applicationPeriod);
        message.setTemplateId("" + letterTemplate.getId());
        message.setHtml(true);

        for (Replacement replacement : letterTemplate.getReplacements()) {
            if (TemplateEmailField.BODY.getFieldName().equals(replacement.getName())) {
                message.setBody(replacement.getDefaultValue());
            } else if (TemplateEmailField.SUBJECT.getFieldName().equals(replacement.getName())) {
                message.setSubject(replacement.getDefaultValue());
            }
            ReplacementDTO replacementDto = new ReplacementDTO();
            replacementDto.setDefaultValue(replacement.getDefaultValue());
            replacementDto.setName(replacement.getName());
            replacementDto.setMandatory(replacement.isMandatory());
            replacementDto.setTimestamp(replacement.getTimestamp());
            emailData.getReplacements().add(replacementDto);
        }
        return emailData;
    }

    private boolean shouldReceiveEmail(LetterReceivers receiver) {
        return (receiver.getLetterReceiverEmail() == null
                && receiver.getLetterReceiverAddress() != null);
    }

    private EmailRecipient buildRecipient(LetterReceivers letter, ObjectMapper mapper) throws IOException {
        String emailAddress = letter.getEmailAddress();
        if (emailAddress == null || emailAddress.isEmpty()) {
            throw new IllegalArgumentException("Vastaanottajat puuttuu");
        }
        EmailRecipient recipient = new EmailRecipient();
        recipient.setEmail(letter.getEmailAddress());
        recipient.setLanguageCode(letter.getWantedLanguage());
        if (letter.getLetterReceiverAddress() != null
                && letter.getLetterReceiverAddress().getFirstName() != null
                && letter.getLetterReceiverAddress().getLastName() != null) {
            recipient.setName(letter.getLetterReceiverAddress().getFirstName() + " "
                    + letter.getLetterReceiverAddress().getLastName());
        }
        // TODO: oidType, oid?

        recipient.setRecipientReplacements(new ArrayList<ReportedRecipientReplacementDTO>());
        for (LetterReceiverReplacement replacement :  letter.getLetterReceiverReplacement()) {
            recipient.getRecipientReplacements().add(new ReportedRecipientReplacementDTO(replacement.getName(),
                    replacement.getEffectiveValue(mapper)));
        }
        return recipient;
    }

    private void sendEmails(EmailSendDataDto emailSendData) {
        for (EmailData email : emailSendData.getEmails()) {
            if (!email.getRecipient().isEmpty()) {
                emailComponent.sendEmail(email);
            }
        }
    }

}
