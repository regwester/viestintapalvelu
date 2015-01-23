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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

@Component
public class ReportedRecipientConverter {

    public ReportedRecipient convert(EmailRecipient emailRecipient) {
        ReportedRecipient reportedRecipient = new ReportedRecipient();

        reportedRecipient.setRecipientOid(emailRecipient.getOid());
        reportedRecipient.setRecipientOidType(emailRecipient.getOidType());
        reportedRecipient.setSocialSecurityID("");
        reportedRecipient.setRecipientEmail(emailRecipient.getEmail());
        reportedRecipient.setLanguageCode(emailRecipient.getLanguageCode());
        reportedRecipient.setSearchName(Optional.fromNullable(emailRecipient.getName()).or(""));
        reportedRecipient.setDetailsRetrieved(false);
        reportedRecipient.setSendingStarted(null);
        reportedRecipient.setSendingEnded(null);
        reportedRecipient.setSendingSuccessful(null);
        reportedRecipient.setFailureReason(null);
        reportedRecipient.setTimestamp(new Date());

        return reportedRecipient;
    }

    public Set<ReportedRecipient> convert(ReportedMessage reportedMessage, List<EmailRecipient> emailRecipients) {
        Set<ReportedRecipient> reportedRecipients = new HashSet<ReportedRecipient>();

        for (EmailRecipient emailRecipient : emailRecipients) {
            ReportedRecipient reportedRecipient = convert(emailRecipient);
            reportedRecipient.setReportedMessage(reportedMessage);
            reportedRecipients.add(reportedRecipient);
        }

        return reportedRecipients;
    }

    public ReportedRecipient convert(ReportedMessage reportedMessage, EmailRecipient emailRecipient) {
        ReportedRecipient reportedRecipient = convert(emailRecipient);
        reportedRecipient.setReportedMessage(reportedMessage);
        return reportedRecipient;
    }
}
