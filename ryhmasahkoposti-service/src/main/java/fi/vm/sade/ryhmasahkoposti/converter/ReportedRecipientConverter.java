package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.OrganizationComponent;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.PersonComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.validation.OidValidator;

@Component
public class ReportedRecipientConverter {

    public ReportedRecipient convert(EmailRecipient emailRecipient) {
        ReportedRecipient reportedRecipient = new ReportedRecipient();

        reportedRecipient.setRecipientOid(emailRecipient.getOid());
        reportedRecipient.setRecipientOidType(emailRecipient.getOidType());
        reportedRecipient.setSocialSecurityID("");
        reportedRecipient.setRecipientEmail(emailRecipient.getEmail());
        reportedRecipient.setLanguageCode(emailRecipient.getLanguageCode());
        reportedRecipient.setSearchName("");
        reportedRecipient.setDetailsRetrieved(false);
        reportedRecipient.setSendingStarted(null);
        reportedRecipient.setSendingEnded(null);
        reportedRecipient.setSendingSuccesful(null);
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
