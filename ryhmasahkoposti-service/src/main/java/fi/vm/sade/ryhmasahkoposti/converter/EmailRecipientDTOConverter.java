package fi.vm.sade.ryhmasahkoposti.converter;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
