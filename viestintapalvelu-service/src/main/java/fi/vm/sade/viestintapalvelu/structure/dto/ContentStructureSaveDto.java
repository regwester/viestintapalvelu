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
package fi.vm.sade.viestintapalvelu.structure.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.ratamaa.dtoconverter.annotation.DtoPath;
import fi.ratamaa.dtoconverter.annotation.DtoSkipped;import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.structure.dto.constraint.ValidContentStructure;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 10:02
 */
@ValidContentStructure
@ApiModel("Kuvaa rakenteen kirjettä, sähköpostia tai asiointitiliä varten.")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentStructureSaveDto implements Serializable, TypedContentStructure {
    private static final long serialVersionUID = 4210603060735538525L;

    @NotNull
    @ApiModelProperty("Rakenteen tyyppi: letter|email|asiointtili")
    private ContentStructureType type;
    @ApiModelProperty("Tyylin nimi. Jos annettu, eikä style tai ole määritelty, haetaan rakenneosalle tällä nimellä" +
            "uusin tyyli tällä nimellä.")
    private String styleName;
    @ApiModelProperty("Tyyli CSS-muodossa. Jos annettu, myös styleName on pakollinen ja luodaan uusi tyyli.")
    @DtoPath("style.style")
    private String style;
    @NotNull @Size(min=1) @Valid
    @ApiModelProperty("Sisältää tämän rakenteen sisällöt ja liitteet halutussa järjestyksessä")
    @DtoSkipped
    private List<ContentStructureContentSaveDto> contents = new ArrayList<ContentStructureContentSaveDto>();

    @Override
    public ContentStructureType getType() {
        return type;
    }

    public void setType(ContentStructureType type) {
        this.type = type;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public List<ContentStructureContentSaveDto> getContents() {
        return contents;
    }

    public void setContents(List<ContentStructureContentSaveDto> contents) {
        this.contents = contents;
    }
}
