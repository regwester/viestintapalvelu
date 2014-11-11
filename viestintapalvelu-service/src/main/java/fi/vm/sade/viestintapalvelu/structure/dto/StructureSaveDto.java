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

package fi.vm.sade.viestintapalvelu.structure.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 10:01
 */
@ApiModel("Tallennettava rakenne")
public class StructureSaveDto implements Serializable {
    private static final long serialVersionUID = -2377272637158787917L;

    @NotNull
    @ApiModelProperty("Rakenteen nimi")
    private String name;
    @NotNull
    @ApiModelProperty("Kielikoodi. Yksilöi rakenteen yhdessä nimen kanssa")
    private String language;
    @ApiModelProperty("Rakenteen kuvaus, joka näytetään käyttöliittymässä rakenteen valinnassa")
    private String description;
    @ApiModelProperty("Sisältörakenteet eri esitystavoille")
    @Valid @NotNull @Size(min=1)
    @DtoConversion
    private List<ContentStructureSaveDto> contentStructures = new ArrayList<ContentStructureSaveDto>();
    @ApiModelProperty("Rakenteessa käytettävät korvaukentät niiden esitysjärjestyksessä")
    @Valid @NotNull
    @DtoConversion(exported = false)
    private List<ContentReplacementSaveDto> replacements = new ArrayList<ContentReplacementSaveDto>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ContentStructureSaveDto> getContentStructures() {
        return contentStructures;
    }

    public void setContentStructures(List<ContentStructureSaveDto> contentStructures) {
        this.contentStructures = contentStructures;
    }

    public List<ContentReplacementSaveDto> getReplacements() {
        return replacements;
    }

    public void setReplacements(List<ContentReplacementSaveDto> replacements) {
        this.replacements = replacements;
    }
}
