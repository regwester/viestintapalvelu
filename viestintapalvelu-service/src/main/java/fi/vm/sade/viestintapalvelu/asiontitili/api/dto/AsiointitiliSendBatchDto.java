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

package fi.vm.sade.viestintapalvelu.asiontitili.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * User: ratamaa
 * Date: 27.10.2014
 * Time: 9:43
 */
@ApiModel(value = "Kerralla muodostettavien asiointitililähetysten joukko")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsiointitiliSendBatchDto implements Serializable, LinkOrAttachmentContainer {
    private static final long serialVersionUID = 7681272971686013474L;

    @NotNull @Size(min=1)
    @ApiModelProperty(value = "Kerralla muodostettavien kirjeiden joukko, (1-n)", required = true)
    private List<AsiointitiliMessageDto> messages = new ArrayList<AsiointitiliMessageDto>();

    @NotNull
    @ApiModelProperty(value = "Kirjepohjan tunniste/nimi. Kirjepohjasta tulee olla asiointitili-tyyppinen toteutus",
            required = true)
    private String templateName;

    @NotNull
    @ApiModelProperty(value = "Lähettäjän nimi, joka näkyy viesteissä", required = true)
    private String senderName = "Opetushallitus";

    @NotNull
    @ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'. Oletuskirjepohja valitaan tämän perusteella," +
            " ylikirjoitettavissa vastaanottajakohtaisesti.", required = true)
    private String languageCode = "FI";

    @ApiModelProperty(value = "Haku-OID, johon lähetys liittyy. Käytetään valittaessa kirjepohjaa", required = false)
    private String applicationPeriod;

    @NotNull
    @ApiModelProperty(value = "Viestin yleiset personointikentät.", required = false, notes = "")
    private Map<String, Object> templateReplacements = new HashMap<String, Object>();

    @ApiModelProperty(value = "Viestin ylikirjoitettu otsikko. Oletuksena käytetään kirjepohjan sisältökenttää asiointitili_header.")
    private String overriddenHeader;

    @ApiModelProperty(value = "Viestin ylikirjoitettu sisältö. Oletuksena käytetään kirjepohjan sisältökenttää asiointitili_content.")
    private String overriddenSmsContent;

    @ApiModelProperty(value = "SMS-viestin ylikirjoitettu sisältö. Oletuksena käytetään kirjepohjan sisältökenttää sms_content.")
    private String overriddenContent;

    @ApiModelProperty(value = "Läheetäänkö SMS-viesti vastaanottajille." +
            "Tällöin käytössä templaten sisältö sms_content", required = false)
    private boolean sendSms = false;

    @ApiModelProperty(value = "Kaikille vastaanottajille yhteiset linkit", required = false)
    private List<AsiointitiliLinkDto> links = new ArrayList<AsiointitiliLinkDto>();

    @ApiModelProperty(value = "Kaikille vastaanottajille yhteiset liitteet", required = false)
    private List<AsiointitiliAttachmentDto> attachments = new ArrayList<AsiointitiliAttachmentDto>();

    @NotNull
    @ApiModelProperty(value = "Tallentajan henkilö-oid", required = true)
    private String storingOid;

    public List<AsiointitiliMessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<AsiointitiliMessageDto> messages) {
        this.messages = messages;
    }

    public Map<String, Object> getTemplateReplacements() {
        return templateReplacements;
    }

    public void setTemplateReplacements(Map<String, Object> templateReplacements) {
        this.templateReplacements = templateReplacements;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getStoringOid() {
        return storingOid;
    }

    public void setStoringOid(String storingOid) {
        this.storingOid = storingOid;
    }

    public String getApplicationPeriod() {
        return applicationPeriod;
    }

    public void setApplicationPeriod(String applicationPeriod) {
        this.applicationPeriod = applicationPeriod;
    }

    public boolean isSendSms() {
        return sendSms;
    }

    public void setSendSms(boolean sendSms) {
        this.sendSms = sendSms;
    }

    public String getOverriddenHeader() {
        return overriddenHeader;
    }

    public void setOverriddenHeader(String overriddenHeader) {
        this.overriddenHeader = overriddenHeader;
    }

    public String getOverriddenSmsContent() {
        return overriddenSmsContent;
    }

    public void setOverriddenSmsContent(String overriddenSmsContent) {
        this.overriddenSmsContent = overriddenSmsContent;
    }

    public String getOverriddenContent() {
        return overriddenContent;
    }

    public void setOverriddenContent(String overriddenContent) {
        this.overriddenContent = overriddenContent;
    }

    @Override
    public List<AsiointitiliLinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<AsiointitiliLinkDto> links) {
        this.links = links;
    }

    @Override
    public List<AsiointitiliAttachmentDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AsiointitiliAttachmentDto> attachments) {
        this.attachments = attachments;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
