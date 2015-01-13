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
package fi.vm.sade.viestintapalvelu.letter;

import java.util.Map;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterDetails;
import fi.vm.sade.viestintapalvelu.model.Template;

@ApiModel(value = "Kirjemallipohjaan sisällytettävät kirjekohtaiset tiedot")
public class Letter implements LetterDetails {

    @ApiModelProperty(value = "Osoitetiedot", required = true)
    private AddressLabel addressLabel;

    @ApiModelProperty(value = "Kirjeen vastaanottajan sähköpostiosoite. Kirjeestä lähetetään sähköposti, mikäli sähköpostiosoite on annettu.", required = false)
    private String emailAddress;

    @ApiModelProperty(value = "Kirjepohjan vastaanottaja kielikoodi. Mikäli annettua kielikoodia vastaava kirjepohja löytyy, käytetään sitä."
            + " kielikoodi ISO 639-1")
    private String languageCode;

    @ApiModelProperty(value = "Kirjeen vastaanottajakohtaiset personointikentät", required = false, notes = "")
    private Map<String, Object> templateReplacements;

    @ApiModelProperty(value = "Kirjeen sisältö")
    private LetterContent letterContent;

    public Letter() {
    }

    public Letter(AddressLabel addressLabel, Map<String, Object> customLetterContents) {
        this.addressLabel = addressLabel;
        this.templateReplacements = customLetterContents;
    }

    public Letter(AddressLabel addressLabel, Template template, Map<String, Object> customLetterContents) {
        this.addressLabel = addressLabel;
        this.templateReplacements = customLetterContents;
    }

    public Letter(AddressLabel addressLabel, String templateName, String languageCode, Map<String, Object> replacements) {
        this.addressLabel = addressLabel;
        this.templateReplacements = replacements;
    }

    public Letter(AddressLabel addressLabel, String templateName, String languageCode, Map<String, Object> replacements, String emailAddress) {
        this.addressLabel = addressLabel;
        this.templateReplacements = replacements;
        this.emailAddress = emailAddress;
    }

    public AddressLabel getAddressLabel() {
        return addressLabel;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Map<String, Object> getCustomLetterContents() {
        return this.templateReplacements;
    }

    public Map<String, Object> getTemplateReplacements() {
        return templateReplacements;
    }

    public void setTemplateReplacements(Map<String, Object> templateReplacements) {
        this.templateReplacements = templateReplacements;
    }

    public void setAddressLabel(AddressLabel addressLabel) {
        this.addressLabel = addressLabel;
    }

    public LetterContent getLetterContent() {
        return letterContent;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setLetterContent(LetterContent letterContent) {
        this.letterContent = letterContent;
    }

    @Override
    public String toString() {
        return "Letter [addressLabel=" + addressLabel + ", templateReplacements=" + templateReplacements + "]";
    }

}
