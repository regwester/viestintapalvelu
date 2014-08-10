package fi.vm.sade.ryhmasahkoposti.service;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailResponse;

public interface EmailService {
	public EmailResponse sendEmail(EmailMessage email);
    public Long getCount(String oid);
}
