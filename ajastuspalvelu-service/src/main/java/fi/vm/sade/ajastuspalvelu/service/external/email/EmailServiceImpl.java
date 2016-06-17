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

package fi.vm.sade.ajastuspalvelu.service.external.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.annotation.Resource;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;

import fi.vm.sade.ajastuspalvelu.service.external.api.TemplateResource;
import fi.vm.sade.ajastuspalvelu.service.external.email.dto.EmailDetailsDto;
import fi.vm.sade.ajastuspalvelu.service.external.email.dto.EmailReceiver;
import fi.vm.sade.ryhmasahkoposti.api.dto.*;
import fi.vm.sade.ryhmasahkoposti.api.resource.EmailResource;
import fi.vm.sade.viestintapalvelu.common.exception.ExternalInterfaceException;
import fi.vm.sade.viestintapalvelu.externalinterface.common.ObjectMapperProvider;

import static com.google.common.base.Optional.fromNullable;

/**
 * User: ratamaa
 * Date: 23.10.2014
 * Time: 15:39
 */
@Service
public class EmailServiceImpl implements EmailService {
    public static final String AJASTUSPROSESSI = "ajastusprosessi";
    public static final String GET_CONTENT_TRUE_VALUE = "YES";
    public static final String EMAIL_TEMPLATE_TYPE = "email";

    @Resource
    private EmailResource emailResourceClient;

    @Resource
    private TemplateResource templateResourceClient;

    @Autowired
    private ObjectMapperProvider objectMapperProvider;

    @Override
    public EmailSendId sendEmail(EmailDetailsDto details) throws Exception {
        EmailData emailData = new EmailData();
        for (Map.Entry<String,Object> kv : details.getReplacements().entrySet()) {
            ReplacementDTO replacement = new ReplacementDTO();
            replacement.setName(kv.getKey());
            replacement.setDefaultValue(kv.getValue() == null ? null : kv.getValue().toString());
            emailData.getReplacements().add(replacement);
        }
        emailData.getEmail().setCharset("UTF-8");
        emailData.getEmail().setHtml(true);
        emailData.getEmail().setTemplateName(details.getTemplateName());
        emailData.getEmail().setLanguageCode("FI");
        emailData.getEmail().setHakuOid(details.getHakuOid());
        emailData.getEmail().setCallingProcess(AJASTUSPROSESSI);
        emailData.getEmail().setSender(toString(details.getReplacements().get("sender")));
        emailData.getEmail().setSenderOid(toString(details.getReplacements().get("senderOid")));
        emailData.getEmail().setOrganizationOid(toString(details.getReplacements().get("organizationOid")));
        List<SourceRegister> registers = new ArrayList<>();
        if (details.getReplacements().get("sourceRegister") != null
                 && details.getReplacements().get("sourceRegister") instanceof List) {
            for (Object val : (List) details.getReplacements().get("sourceRegister")) {
                if (val instanceof String) {
                    registers.add(new SourceRegister(val.toString()));
                } else if (val instanceof Map) {
                    String name = toString(((Map) val).get("name"));
                    if (name != null) {
                        registers.add(new SourceRegister(name));
                    }
                }
            }
        }
        if (!registers.isEmpty()) {
            emailData.getEmail().setSourceRegister(registers);
        } else {
            emailData.getEmail().setSourceRegister(Collections.singletonList(new SourceRegister("opintopolku")));
        }
        TemplateDTO templateDTO = getTemplate(emailData);

        /// TODO: XXX
        /**
         * Viestintäpalvelusta tulevissa myös email-tyyppisissä kirjeissä on määritelty otsikko muttei välttämättä
         * subject-replacementtia. Yleisistä replacement-kentistä ryhmäsähköposti kuitenkin jättää pois sellaiset, joita
         * ei ole pohjassa määritelty (yleiseltä tasolta, ei vastaanottajakohtaisista). Näin ollen kierrätetään tämä
         * yleinen replacement subject-kentän kautta ja käytetään otsikko-kenttää jos muita ei ole määritelty.
         * Niin nyt näin toistaiseksi...
         */
        Optional<String> subjectReplacement = replacement("subject", templateDTO),
                otsikkoReplacement = replacement("otsikko", templateDTO);
        if (details.getReplacements().get("subject") != null) {
            emailData.getEmail().setSubject(toString(details.getReplacements().get("subject")));
        } else if (!subjectReplacement.isPresent() && otsikkoReplacement.isPresent()) {
            emailData.getEmail().setSubject(otsikkoReplacement.get());
        }

        emailData.getEmail().setTemplateId(""+templateDTO.getId());

        for (EmailReceiver receiver : details.getReceivers()) {
            if (receiver.getEmail() == null) {
                continue;
            }

            EmailRecipient recipient = new EmailRecipient();
            recipient.setEmail(receiver.getEmail());
            recipient.setLanguageCode("FI");
            recipient.setOid(receiver.getOid());
            recipient.setOidType(receiver.getOidType());
            recipient.setRecipientReplacements(new ArrayList<ReportedRecipientReplacementDTO>());
            if (receiver.getReplacements() != null) {
                for (Map.Entry<String, Object> kv : receiver.getReplacements().entrySet()) {
                    ReportedRecipientReplacementDTO replacement = new ReportedRecipientReplacementDTO();
                    replacement.setName(kv.getKey());
                    replacement.setValue(kv.getValue());
                    recipient.getRecipientReplacements().add(replacement);
                }
            }
            emailData.getRecipient().add(recipient);
        }

        Response response = emailResourceClient.sendEmail(emailData);
        if (response.getStatus() == HttpStatus.OK.value()) {
            return objectMapperProvider.getContext(EmailServiceImpl.class).reader(EmailSendId.class)
                    .readValue((InputStream) response.getEntity());
        }
        throw new ExternalInterfaceException("Ryhhmasahkoposti-service sendMail returned code " + response.getStatus(), new Throwable());
    }

    private String toString(Object replacement) {
        return replacement == null ? null : replacement.toString();
    }

    private Optional<String> replacement(String name, TemplateDTO templateDTO) {
        for (ReplacementDTO replacementDTO : templateDTO.getReplacements()) {
            if (name.equalsIgnoreCase(replacementDTO.getName())) {
                return fromNullable(replacementDTO.getDefaultValue());
            }
        }
        return Optional.absent();
    }

    private TemplateDTO getTemplate(EmailData emailData) throws IOException, DocumentException {
        TemplateDTO templateDTO = templateResourceClient.getTemplate(emailData.getEmail().getTemplateName(),
                emailData.getEmail().getLanguageCode(), EMAIL_TEMPLATE_TYPE,
                emailData.getEmail().getHakuOid(), GET_CONTENT_TRUE_VALUE);
        if (templateDTO == null) {
            throw new NotFoundException("Template "+emailData.getEmail().getTemplateName() + " not found.");
        }
        return templateDTO;
    }
}
