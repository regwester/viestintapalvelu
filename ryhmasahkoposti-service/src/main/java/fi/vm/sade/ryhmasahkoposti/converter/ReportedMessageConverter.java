package fi.vm.sade.ryhmasahkoposti.converter;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.ryhmasahkoposti.api.constants.GroupEmailConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;

@Component
public class ReportedMessageConverter {
    private CurrentUserComponent currentUserComponent;

    @Autowired
    public ReportedMessageConverter(CurrentUserComponent currentUserComponent) {
        this.currentUserComponent = currentUserComponent;
    }

    public ReportedMessage convert(EmailMessage emailMessage) throws IOException {
        ReportedMessage reportedMessage = new ReportedMessage();
        Henkilo henkilo = currentUserComponent.getCurrentUser();
        String senderName = getPersonName(henkilo);

        reportedMessage.setProcess(emailMessage.getCallingProcess());
        reportedMessage.setSenderOid(henkilo.getOidHenkilo());
        reportedMessage.setSenderName(senderName);
        reportedMessage.setSenderOrganizationOid(emailMessage.getOrganizationOid());
        reportedMessage.setSubject(emailMessage.getSubject());
        reportedMessage.setSenderEmail(emailMessage.getFrom());
        reportedMessage.setSenderDisplayText(emailMessage.getSender());
        reportedMessage.setReplyToEmail(emailMessage.getReplyTo());
        reportedMessage.setMessage(emailMessage.getBody());
        reportedMessage.setType(ReportedMessage.TYPE_EMAIL);
        if (emailMessage.isHtml()) {
            reportedMessage.setHtmlMessage(GroupEmailConstants.HTML_MESSAGE);
        } else {
            reportedMessage.setHtmlMessage(GroupEmailConstants.NOT_HTML_MESSAGE);
        }
        reportedMessage.setCharacterSet(emailMessage.getCharset());
        reportedMessage.setSendingStarted(new Date());
        reportedMessage.setTimestamp(new Date());
        return reportedMessage;
    }

    private String getPersonName(Henkilo henkilo) {
        StringBuilder sb = new StringBuilder();

        sb.append(henkilo.getSukunimi().trim());
        sb.append(" ");
        sb.append(henkilo.getKutsumanimi().trim());

        return sb.toString();
    }
}
