package fi.vm.sade.ryhmasahkoposti.converter;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.constants.GroupEmailConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;

@Component
public class ReportedMessageConverter {
	public static ReportedMessage convert(EmailMessage emailMessage) throws IOException {
		ReportedMessage reportedMessage = new ReportedMessage();
		
		reportedMessage.setSubject(emailMessage.getSubject());
		reportedMessage.setProcess(emailMessage.getCallingProcess());
		reportedMessage.setSenderOid(emailMessage.getSenderOid());
		reportedMessage.setSenderOidType(emailMessage.getSenderOidType());
		reportedMessage.setSenderEmail(emailMessage.getSenderEmail());
		reportedMessage.setReplyToOid("");
		reportedMessage.setReplyToOidType("");
		reportedMessage.setReplyToEmail(emailMessage.getOwnerEmail());
		reportedMessage.setSubject(emailMessage.getSubject());
		reportedMessage.setMessage(emailMessage.getBody());
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
}
