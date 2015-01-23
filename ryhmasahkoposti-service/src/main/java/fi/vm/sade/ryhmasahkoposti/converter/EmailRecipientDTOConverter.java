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

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

@Component
public class EmailRecipientDTOConverter {

    public EmailRecipientDTO convert(ReportedRecipient reportedRecipient) {
        EmailRecipientDTO emailRecipientDTO = new EmailRecipientDTO();

        emailRecipientDTO.setRecipientID(reportedRecipient.getId());
        emailRecipientDTO.setRecipientVersion(reportedRecipient.getVersion());
        emailRecipientDTO.setFirstName("");
        emailRecipientDTO.setLastName("");
        emailRecipientDTO.setOrganizationName("");
        emailRecipientDTO.setOid(reportedRecipient.getRecipientOid());
        emailRecipientDTO.setEmail(reportedRecipient.getRecipientEmail());
        emailRecipientDTO.setEmailMessageID(reportedRecipient.getReportedMessage().getId());

        return emailRecipientDTO;
    }

    public List<EmailRecipientDTO> convert(List<ReportedRecipient> reportedRecipients) {
        List<EmailRecipientDTO> emailRecipientDTOs = new ArrayList<EmailRecipientDTO>();

        for (ReportedRecipient reportedRecipient : reportedRecipients) {
            emailRecipientDTOs.add(convert(reportedRecipient));
        }
        return emailRecipientDTOs;
    }
}
