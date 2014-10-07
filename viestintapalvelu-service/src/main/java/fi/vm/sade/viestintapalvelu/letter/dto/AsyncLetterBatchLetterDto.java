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

package fi.vm.sade.viestintapalvelu.letter.dto;

import java.io.Serializable;
import java.util.Map;

import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.letter.LetterContent;

/**
 * User: ratamaa
 * Date: 18.9.2014
 * Time: 14:46
 */
public class AsyncLetterBatchLetterDto implements Serializable, LetterDetails {
    @ApiModelProperty(value = "Osoitetiedot", required = true)
    private AddressLabel addressLabel;

    @ApiModelProperty(value = "Kirjeen vastaanottajan sähköpostiosoite. Kirjeestä lähetetään sähköposti, mikäli sähköpostiosoite on annettu.", required = false)
    private String emailAddress;

    @ApiModelProperty(value = "Kirjepohjan vastaanottaja kielikoodi. Mikäli annettua kielikoodia vastaava kirjepohja löytyy, käytetään sitä."
            + " kielikoodi ISO 639-1")
    private String languageCode;

    @ApiModelProperty(value = "Kirjeen vastaanottajakohtaiset personointikentät", required = false, notes = "")
    private Map<String, Object> templateReplacements;

    @Override
    public AddressLabel getAddressLabel() {
        return addressLabel;
    }

    public void setAddressLabel(AddressLabel addressLabel) {
        this.addressLabel = addressLabel;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public Map<String, Object> getTemplateReplacements() {
        return templateReplacements;
    }

    public void setTemplateReplacements(Map<String, Object> templateReplacements) {
        this.templateReplacements = templateReplacements;
    }
}
