package fi.vm.sade.ryhmasahkoposti.service;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;

public interface EmailService {
    public Long getCount(String oid);
    public String getEML(EmailMessage emailMessage, String emailAddress) throws Exception;
}
