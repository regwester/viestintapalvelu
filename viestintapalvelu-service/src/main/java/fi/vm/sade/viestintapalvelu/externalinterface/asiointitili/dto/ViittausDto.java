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

package fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.ratamaa.dtoconverter.annotation.DtoPath;
import fi.ratamaa.dtoconverter.annotation.DtoValidate;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 9:51
 */
@DtoValidate
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="Liittyvän asiointiasian tunniste", description = "Liittyvän asiointiasian tunniste. \n" +
        "Asiointitilipalvelu liittää lähetetyn asian tämän tiedon avulla asiointitilipalvelussa jo olevaan asiaan." +
        "" +
        "Voidaan antaa joko asiointitilipalvelun tunniste asialle tai viranomaisjärjestelmän oma tunniste.\n" +
        "\n" +
        "Mikäli annettua tunnistetta vastaavaa asiaa ei löydy, jätetään liitos tekemättä" +
        "" +
        "Käytetään, jos halutaan liittää viesti aiemmin lähetettyyn viestiin asiointitilillä, jolloin viesti näkyy" +
        "samassa viestiketjussa. Esimerkiksi, jos vastataan asiointitililtä saapuneeseen viestiin.\n" +
        "Ei todennäköisesti ole tarpeen käyttää tätä." )
public class ViittausDto implements Serializable  {
    private static final long serialVersionUID = -9158498331044415921L;

    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value="Liittyvän asian tunniste.", notes = "Joko viranomaisen käyttämä viranomaistunniste" +
            "tai jos viesti on tullut asiointitililtä, asiointitilin käyttämä viestin asiointitilitunniste." +
            "UUID-generoitu OPH:n puolella.", required = true)
    @DtoPath("viittausTunniste")
    private String tunniste;
    @NotNull
    @Size(max = 20)
    @ApiModelProperty(value = "Liittyvän asian tunnisteen tyyppi. Joko asiointitilipalvelun oma yksilöivä tunniste" +
            "AsiointitiliTunniste (ks. vastausviesti) tai viranomaisjärjestelmän oma yksilöivä tunniste ViranomaisTunniste.",
            notes = "AsiointitiliTunniste ViranomaisTunniste (KÄYTETÄÄN OPH tunnistetta)",
            required = true)
    @DtoPath("viittausTunnisteTyyppi")
    private String tunnisteTyyppi;

    public String getTunniste() {
        return tunniste;
    }

    public void setTunniste(String tunniste) {
        this.tunniste = tunniste;
    }

    public String getTunnisteTyyppi() {
        return tunnisteTyyppi;
    }

    public void setTunnisteTyyppi(String tunnisteTyyppi) {
        this.tunnisteTyyppi = tunnisteTyyppi;
    }
}
