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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.model.types.ContentType;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 10:02
 */
@ApiModel("Rakenteeseen liittyvä korvauskenttä")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentReplacementSaveDto implements Serializable {
    private static final long serialVersionUID = -4428639427097651014L;

    @NotNull
    @ApiModelProperty("Korvauskentän yksilöivä muuttujanimi, jolla tähän korvauskenttään viitataan sisällöissä ja kirjepohjassa.")
    private String key;
    @NotNull
    @ApiModelProperty("Käyttöliittymässä näytettävä korvaukentän nimi")
    private String name;
    @ApiModelProperty("Käyttöliittymässä näytettävä korvaukentän kuvaus")
    private String description;
    @NotNull
    @ApiModelProperty("Käyttöliittymäkentän tyyppi html/plain: HTML tuottaa RichText-editorin ja teksti normaalin syöttökentän")
    private ContentType contentType = ContentType.plain;
    @NotNull @Min(1)
    @ApiModelProperty(value = "Käyttöliittymässä näytettävän kentän rivien lukumäärä.",
            notes = "1 tarkoittaa yksirivistä syöttökenttää, johon ei voi syöttää enempää rivejä. " +
                    "Suurempi arvo on ohjeellinen syöttökentän oletusmitta, joka voidaan ylittää.")
    private int numberOfRows = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }
}
