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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fi.vm.sade.ajastuspalvelu.service.external.email.dto.EmailDetailsDto;
import fi.vm.sade.ajastuspalvelu.service.external.email.dto.EmailReceiver;
import fi.vm.sade.ryhmasahkoposti.api.dto.*;
import fi.vm.sade.ryhmasahkoposti.api.resource.EmailResource;
import fi.vm.sade.viestintapalvelu.common.exception.ExternalInterfaceException;

/**
 * User: ratamaa
 * Date: 23.10.2014
 * Time: 15:39
 */
@Service
public class EmailServiceImpl implements EmailService {
    public static final String AJASTUSPROSESSI = "ajastusprosessi";

    @Resource
    private EmailResource emailResourceClient;

    @Override
    public EmailSendId sendEmail(EmailDetailsDto details) throws Exception {
        EmailData emailData = new EmailData();
        emailData.getEmail().setCharset("UTF-8");
        emailData.getEmail().setHtml(true);
        emailData.getEmail().setTemplateName(details.getTemplateNamke());
        emailData.getEmail().setLanguageCode("FI");
        emailData.getEmail().setCallingProcess(AJASTUSPROSESSI);
        emailData.getEmail().setSourceRegister(Arrays.asList(new SourceRegister("opintopolku")));

        for (EmailReceiver receiver : details.getReceivers()) {
            if (receiver.getEmail() == null) {
                continue;
            }

            EmailRecipient recipient = new EmailRecipient();
            recipient.setEmail("varsinainen_vastaanottaja@example.com");
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
            return (EmailSendId) response.getEntity();
        }
        throw new ExternalInterfaceException("Ryhhmasahkoposti-service sendMail returned code " + response.getStatus());
    }
}
