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
package fi.vm.sade.viestintapalvelu.asiontitili.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * User: ratamaa
 * Date: 27.10.2014
 * Time: 10:12
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsiointitiliAttachmentDto implements Serializable {
    private static final long serialVersionUID = -151747294953656746L;

    @NotNull
    @Size(max=200) @Pattern(regexp = "[A-Za-z0-9öäåÖÄÅ_.]+")
    @ApiModelProperty("Tiedoston nimi sisältäen tiedostopäätteen. Sallittuja merkkejä ovat A-Ö a-ö 0-9  _ .")
    private String name;
    @NotNull @Size(max=200)
    @ApiModelProperty("Tiedoston kuvaus")
    private String description;
    @NotNull @Size(min=1)
    @ApiModelProperty(value="Tiedoston sisältö BASE64 -enkoodattuna.", required = true)
    private String content;
    @NotNull
    @ApiModelProperty(value="Tiedoston koko kilotavuina. Pyöristetään ylöspäin seuraavaan kilotavuun.",
            notes = "Tarkkuudella ei ole väliä. Vain suuntaa antavana tietona asiakkaalle että tietää millaista" +
                    "tiedostoa on avaamassa.", required = true)
    private Integer size = 0;
    @NotNull @Size(max=150)
    @ApiModelProperty(value= "Tiedoston formaatti. MimeTyyppi (esimerkiksi application/pdf)", required = true)
    private String contentType = "application/pdf";

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
