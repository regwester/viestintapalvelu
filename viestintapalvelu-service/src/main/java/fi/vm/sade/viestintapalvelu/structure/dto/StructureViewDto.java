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

import org.joda.time.DateTime;

import com.wordnik.swagger.annotations.ApiModel;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 14:23
 */
@ApiModel("Rakenne")
public class StructureViewDto implements Serializable {
    private static final long serialVersionUID = -4413108416336072603L;

    private Long id;
    private String name;
    private String language;
    private String description;
    @DtoConversion
    private DateTime timestamp;
    @DtoConversion
    private List<ContentStructureViewDto> contentStructures = new ArrayList<ContentStructureViewDto>();
    @DtoConversion
    private List<ContentReplacementDto> replacements = new ArrayList<ContentReplacementDto>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<ContentStructureViewDto> getContentStructures() {
        return contentStructures;
    }

    public void setContentStructures(List<ContentStructureViewDto> contentStructures) {
        this.contentStructures = contentStructures;
    }

    public List<ContentReplacementDto> getReplacements() {
        return replacements;
    }

    public void setReplacements(List<ContentReplacementDto> replacements) {
        this.replacements = replacements;
    }
}
