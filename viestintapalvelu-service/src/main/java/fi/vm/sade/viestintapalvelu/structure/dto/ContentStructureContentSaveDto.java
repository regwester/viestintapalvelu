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

import javax.validation.constraints.NotNull;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.ratamaa.dtoconverter.annotation.DtoPath;
import fi.vm.sade.viestintapalvelu.model.types.ContentRole;
import fi.vm.sade.viestintapalvelu.model.types.ContentType;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 10:04
 */
@ApiModel(value = "Rakenteen sisältöosa",
        description = "Kuvaa kirjeen/sähköpostin/asiointitiliviestin sisällön, yksittäisen sähköpostin tai asiointitlilin liitteen " +
                " tai asiointitilin tekstiviestin")
public class ContentStructureContentSaveDto implements Serializable {
    private static final long serialVersionUID = 8957552890776904394L;

    @NotNull
    @ApiModelProperty("Kirjeessä aina body, sähköpostissa body tai attachment, asiointitilillä body, attachment tai sms")
    private ContentRole role;
    @NotNull
    @DtoPath("content.name")
    @ApiModelProperty("Sisältäosan nimi")
    private String name;
    @NotNull
    @DtoPath("content.contentType")
    @ApiModelProperty("Sisällön tyyppi html/plain. Asiointilillä body ja sms oltava aina plain.")
    private ContentType contentType;
    @NotNull
    @ApiModelProperty("Sisältö, joka vastaa contentTypen muotoa: html-tyyppisessä HTML-dokumentti ja plain-muodossa pelkkää tekstiä")
    @DtoPath("content.content")
    private String content;

    public ContentStructureContentSaveDto() {
    }

    public ContentStructureContentSaveDto(ContentRole role, String name, ContentType contentType, String content) {
        this.role = role;
        this.name = name;
        this.contentType = contentType;
        this.content = content;
    }

    public ContentRole getRole() {
        return role;
    }

    public void setRole(ContentRole role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ContentStructureContentSaveDto{" +
                "role=" + role +
                ", name='" + name + '\'' +
                ", contentType=" + contentType +
                ", content='" + content + '\'' +
                '}';
    }
}
