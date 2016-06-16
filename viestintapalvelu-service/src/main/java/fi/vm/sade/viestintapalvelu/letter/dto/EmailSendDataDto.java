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
package fi.vm.sade.viestintapalvelu.letter.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;

/**
 * User: ratamaa
 * Date: 29.9.2014
 * Time: 13:23
 */
public class EmailSendDataDto {
    private Map<String,EmailData> emailByLanguageCode = new TreeMap<>();

    public EmailData getEmailByLanguageCode(String languageCode) {
        return emailByLanguageCode.get(languageCode.toUpperCase());
    }

    public void setEmailByLanguageCode(String languageCode, EmailData emailData) {
        this.emailByLanguageCode.put(languageCode.toUpperCase(), emailData);
    }

    public List<EmailData> getEmails() {
        return new ArrayList<>(this.emailByLanguageCode.values());
    }
}
