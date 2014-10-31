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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.common.util.ValidHetu;

/**
 * User: ratamaa
 * Date: 27.10.2014
 * Time: 9:44
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Asiointitilille lähetettävän viestin vastaanttajkohtaiset tiedot")
public class AsiointitiliMessageDto implements Serializable, LinkOrAttachmentContainer {
    private static final long serialVersionUID = -6775023053415042937L;

    @NotNull
    @ApiModelProperty(value = "Vastaanotajan henkilö-OID", required = true)
    private String receiverHenkiloOid;

    @ApiModelProperty(value = "Vastaanottajan osoitetiedot", required = false)
    private AddressLabel addressLabel;

    @NotNull @ValidHetu
    @ApiModelProperty(value = "Vastaanotajan henkilötunnus: pakollinen asiointitilillä käytettävä tunniste.", required = true)
    private String receiverHetu;

    @ApiModelProperty(value = "Kirjepohjan vastaanottaja kielikoodi. Mikäli annettua kielikoodia vastaava kirjepohja löytyy, käytetään sitä."
            + " kielikoodi ISO 639-1", required = false)
    private String languageCode;

    @ApiModelProperty(value = "Viestin ylikirjoitettu vastaanottajakohtainen otsikko. Oletuksena käytetään lähetyksen määräämää.")
    private String overriddenHeader;

    @ApiModelProperty(value = "Viestin ylikirjoitettu vastaanottajakohtainen sisältö. Oletuksena käytetään lähetyksen määräämää.")
    private String overriddenSmsContent;

    @ApiModelProperty(value = "SMS-viestin ylikirjoitettu vastaanottajakohtainen sisältö. Oletuksena käytetään lähetyksen määräämää.")
    private String overriddenContent;

    @ApiModelProperty(value = "Kirjepohjan vastaanottajakohtaiset personointikentät", required = false, notes = "")
    private Map<String, Object> templateReplacements = new HashMap<String, Object>();

    @ApiModelProperty(value = "Vastaanottajakohtaiset linkit", required = false)
    private List<AsiointitiliLinkDto> links = new ArrayList<AsiointitiliLinkDto>();

    @ApiModelProperty(value = "Vastaanottajakohtaiset liitteet", required = false)
    private List<AsiointitiliAttachmentDto> attachments = new ArrayList<AsiointitiliAttachmentDto>();

    public String getReceiverHenkiloOid() {
        return receiverHenkiloOid;
    }

    public void setReceiverHenkiloOid(String receiverHenkiloOid) {
        this.receiverHenkiloOid = receiverHenkiloOid;
    }

    public String getReceiverHetu() {
        return receiverHetu;
    }

    public void setReceiverHetu(String receiverHetu) {
        this.receiverHetu = receiverHetu;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Map<String, Object> getTemplateReplacements() {
        return templateReplacements;
    }

    public void setTemplateReplacements(Map<String, Object> templateReplacements) {
        this.templateReplacements = templateReplacements;
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

    public AddressLabel getAddressLabel() {
        return addressLabel;
    }

    public void setAddressLabel(AddressLabel addressLabel) {
        this.addressLabel = addressLabel;
    }
}
