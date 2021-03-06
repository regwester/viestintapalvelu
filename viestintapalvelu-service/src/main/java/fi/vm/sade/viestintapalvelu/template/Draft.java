/*
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
 */
package fi.vm.sade.viestintapalvelu.template;

import java.util.Date;
import java.util.Map;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class Draft {

    private static final long serialVersionUID = 1;

    @ApiModelProperty(value = "Luonnoksen Id")
    private Long draftId;

    @ApiModelProperty(value = "Kirjepohjan tunniste/nimi")
    private String templateName;

    @ApiModelProperty(value = "Kirjepohjan kielikoodi ISO 639-1, default = 'FI'")
    private String languageCode;

    @ApiModelProperty(value = "Tallentajan Oid")
    private String storingOid;

    @ApiModelProperty(value = "Organisaatio Oid")
    private String organizationOid;

    @ApiModelProperty(value = "Haku")
    private String applicationPeriod;

    @ApiModelProperty(value = "Hakukohde id")
    private String fetchTarget;

    @ApiModelProperty(value = "Vapaa teksti tunniste")
    private String tag;

    @ApiModelProperty(value = "Luonnoksen personointikentät", required = false, notes = "")
    private Map<String, Object> replacements;

    @ApiModelProperty(value = "Luonnoksen tallennusaika")
    private Date timestamp;

    public Long getDraftId() {
        return draftId;
    }

    public void setDraftId(Long draftId) {
        this.draftId = draftId;
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

    public String getOrganizationOid() {
        return organizationOid;
    }

    public void setOrganizationOid(String organizationOid) {
        this.organizationOid = organizationOid;
    }

    public String getApplicationPeriod() {
        return applicationPeriod;
    }

    public void setApplicationPeriod(String applicationPeriod) {
        this.applicationPeriod = applicationPeriod;
    }

    public String getFetchTarget() {
        return fetchTarget;
    }

    public void setFetchTarget(String fetchTarget) {
        this.fetchTarget = fetchTarget;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, Object> getReplacements() {
        return replacements;
    }

    public void setReplacements(Map<String, Object> replacements) {
        this.replacements = replacements;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
