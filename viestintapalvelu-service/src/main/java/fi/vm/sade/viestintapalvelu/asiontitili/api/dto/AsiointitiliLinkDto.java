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
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * User: ratamaa
 * Date: 27.10.2014
 * Time: 9:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("Asiointilin viestissä näytettävä linkki")
public class AsiointitiliLinkDto implements Serializable {
    private static final long serialVersionUID = -4823450420700896299L;

    @NotNull @Size(max=200) @Pattern(regexp = "[A-Za-z0-9öäåÖÄÅ_.]+")
    @ApiModelProperty(value = "Linkin nimi. Sallittuja merkkejä ovat A-Ö a-ö 0-9  _ .", required = true)
    private String name;
    @NotNull @Size(max=200)
    @ApiModelProperty(value = "Asiakirjan seliteteksti", required = true)
    private String description;
    @Size(max=200)
    @NotNull
    @ApiModelProperty("Linkin URL")
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
