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

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.ratamaa.dtoconverter.annotation.DtoPath;
import fi.vm.sade.viestintapalvelu.model.ContentStructureContent;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 14:24
 */
@ApiModel("Rakennetyypin (esim. letter|email|asiointitili) sisältö")
public class ContentStructureViewDto implements Serializable {
    private static final long serialVersionUID = 4909549781715757219L;

    private Long id;
    @DtoConversion
    @ApiModelProperty("Rakenteen tyyppi: email|letter|asiointtili")
    private String type;
    private Long styleId;
    private String styleName;
    @DtoPath("style.style")
    private String style;
    @DtoConversion
    private List<ContentStructureContentViewDto> contents = new ArrayList<ContentStructureContentViewDto>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
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

    public List<ContentStructureContentViewDto> getContents() {
        return contents;
    }

    public void setContents(List<ContentStructureContentViewDto> contents) {
        this.contents = contents;
    }
}
