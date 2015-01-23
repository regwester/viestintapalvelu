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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.template.Template;

/**
 * User: ratamaa
 * Date: 18.9.2014
 * Time: 14:45
 */
public class AsyncLetterBatchDto implements Serializable, LetterBatchDetails {
    private static final long serialVersionUID = 4947130071223481115L;

    @ApiModelProperty(value = "Kerralla muodostettavien kirjeiden joukko, (1-n)", required = true)
    private List<AsyncLetterBatchLetterDto> letters = new ArrayList<AsyncLetterBatchLetterDto>();

    @ApiModelProperty(value = "Kirjepohja")
    private Template template;

    @ApiModelProperty(value = "Kirjepohjan tunniste")
    private Long templateId;

    @ApiModelProperty(value = "Kirjeen yleiset personointikentät", required = false, notes = "")
    private Map<String, Object> templateReplacements;

    @ApiModelProperty(value = "Kirjepohjan tunniste/nimi")
    private String templateName;

    @ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'")
    private String languageCode;

    @ApiModelProperty(value = "Tallentajan Oid")
    private String storingOid;

    @ApiModelProperty(value = "Organisaatio Oid")
    private String organizationOid;

    @ApiModelProperty(value = "Haku")
    private String applicationPeriod;

    @ApiModelProperty(value = "Hakukohde id")
    private String fetchTarget;

    @ApiModelProperty(value = "Vapaateksti tunniste")
    private String tag;

    @ApiModelProperty(value = "Onko iposti-tyyppinen oletuksena ei iposti", required = false)
    private boolean iposti = false;
    
    private Map<String, byte[]> iPostiData = new LinkedHashMap<String, byte[]>();


    @Override
    public boolean isIposti() {
        return iposti;
    }
    
    @Override
    public List<AsyncLetterBatchLetterDto> getLetters() {
        return letters;
    }

    public void setLetters(List<AsyncLetterBatchLetterDto> letters) {
        this.letters = letters;
    }

    public void setIposti(boolean iposti) {
        this.iposti = iposti;
    }
    
    @Override
    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    @Override
    public Long getTemplateId() {
        return templateId;
    }

    @Override
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    @Override
    public Map<String, Object> getTemplateReplacements() {
        return templateReplacements;
    }

    public void setTemplateReplacements(Map<String, Object> templateReplacements) {
        this.templateReplacements = templateReplacements;
    }

    @Override
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String getStoringOid() {
        return storingOid;
    }

    public void setStoringOid(String storingOid) {
        this.storingOid = storingOid;
    }

    @Override
    public String getOrganizationOid() {
        return organizationOid;
    }

    public void setOrganizationOid(String organizationOid) {
        this.organizationOid = organizationOid;
    }

    @Override
    public String getApplicationPeriod() {
        return applicationPeriod;
    }

    public void setApplicationPeriod(String applicationPeriod) {
        this.applicationPeriod = applicationPeriod;
    }

    @Override
    public String getFetchTarget() {
        return fetchTarget;
    }

    public void setFetchTarget(String fetchTarget) {
        this.fetchTarget = fetchTarget;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public Map<String, byte[]> getIPostiData() {
        return iPostiData;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setIPostiData(Map<String, byte[]> IPostiData) {
        this.iPostiData = IPostiData;
    }
}
