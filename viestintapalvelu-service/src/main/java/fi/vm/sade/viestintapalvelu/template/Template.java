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
package fi.vm.sade.viestintapalvelu.template;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureSaveDto;

@ApiModel(value = "Kirjetemplate")
public class Template {

    private static final long serialVersionUID = 4178735997933155683L;

    private long id;

    private Date timestamp;

    @NotNull
    @ApiModelProperty("Kirjepohjan tekninen nimi")
    private String name;
    @NotNull
    @Size(min = 2, max = 3)
    @ApiModelProperty("Kirjepohjan kielikoodi")
    private String language;

    @ApiModelProperty("Kirjepohjan kuvaus")
    private String description;

    private String styles;

    private String storingOid;

    private String organizationOid;

    private List<TemplateContent> contents;

    @NotNull
    @Size(min = 1)
    private List<Replacement> replacements;

    @ApiModelProperty("Haku OID:t, joihin tämä kirjepohja linkittyy")
    private List<String> applicationPeriods;

    @Deprecated
    // Tätä ei käytetä mihinkään
    private String templateVersio;

    @ApiModelProperty("Jos true, tätä kirjepohjaa käytetään oletuksena suhteessa muihin samalla tavalla rajattuihin kirjepohjiin")
    private boolean usedAsDefault;

    private String type;

    @ApiModelProperty("Rakenteen id. Jos syötetty valitaan rakenne tämän mukaan. Oltava saman kielinen kuin kirjepohja.")
    private Long structureId;
    @ApiModelProperty("Rakenteen nimi. Jos syötetty ja structureId ei ole syötetty haetaan uusin rakenne tällä nimellä ja kielellä.")
    private String structureName;
    @Valid
    @ApiModelProperty("Käytetään luomaan uusi rakene jos sekä structureId että structureName ovat null")
    private StructureSaveDto structure;

    @ApiModelProperty("Kirjepohjan tila")
    private State state;

    public List<TemplateContent> getContents() {
        return contents;
    }

    public long getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public String getOrganizationOid() {
        return organizationOid;
    }

    public List<Replacement> getReplacements() {
        return replacements;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getStoringOid() {
        return storingOid;
    }

    @Deprecated
    public String getStyles() {
        return styles;
    }

    /**
     * Method getTemplateVersio returns the version of the template.
     * 
     * @return String
     */
    public String getTemplateVersio() {
        return templateVersio;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setContents(List<TemplateContent> contents) {
        this.contents = contents;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganizationOid(String organizationOid) {
        this.organizationOid = organizationOid;
    }

    public void setReplacements(List<Replacement> replacements) {
        this.replacements = replacements;
    }

    public void setStoringOid(String storingOid) {
        this.storingOid = storingOid;
    }

    public void setStyles(String styles) {
        this.styles = styles;
    }

    public void setTemplateVersio(String templateVersio) {
        this.templateVersio = templateVersio;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getApplicationPeriods() {
        return applicationPeriods;
    }

    public void setApplicationPeriods(List<String> applicationPeriods) {
        this.applicationPeriods = applicationPeriods;
    }

    public boolean isUsedAsDefault() {
        return usedAsDefault;
    }

    public void setUsedAsDefault(boolean usedAsDefault) {
        this.usedAsDefault = usedAsDefault;
    }

    public Long getStructureId() {
        return structureId;
    }

    public void setStructureId(Long structureId) {
        this.structureId = structureId;
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public StructureSaveDto getStructure() {
        return structure;
    }

    public void setStructure(StructureSaveDto structure) {
        this.structure = structure;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Template [id=" + id + ", timestamp=" + timestamp + ", name=" + name + ", description=" + description + ", language=" + language + ", styles="
                + styles + ", storingOid=" + storingOid + ", organizationOid=" + organizationOid + ", contents=" + contents + ", replacements=" + replacements
                + ", templateVersio=" + templateVersio + ", type=" + type + ", applicationPeriods=" + applicationPeriods + ", usedAsDefault=" + usedAsDefault
                + ", state=" + state + "]";
    }
}
