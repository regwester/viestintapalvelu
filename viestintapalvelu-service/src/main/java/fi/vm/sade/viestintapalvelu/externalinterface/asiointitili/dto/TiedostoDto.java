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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.ratamaa.dtoconverter.annotation.DtoConverted;
import fi.ratamaa.dtoconverter.annotation.DtoPath;
import fi.ratamaa.dtoconverter.annotation.DtoValidate;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 9:53
 */
@ApiModel(value="Asiaan liittyvä asiakirja",
        description="Asiaan liittyvä asiakirja voi tulla asian liitteenä TiedostoSisalto-kentässä" +
        "tai URL-osoitteena viranomaisen omaan asiakirjavarastoon TiedostoURL-kentässä." +
        "Vain URLia käytetään 1. vaiheessa, mutta jatkossa mahdolista välittää muotoinen PDF päätös")
@DtoValidate
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiedostoDto implements Serializable {
    private static final long serialVersionUID = -8346140031056859027L;

    @NotNull @Size(max=200) @Pattern(regexp = "[A-Za-z0-9öäåÖÄÅ_.]+")
    @DtoPath("tiedostoNimi")
    @ApiModelProperty(value = "ASIAKAS. Tiedoston nimi sisältäen tiedostopäätteen. \n" +
        "Tiedosto näkyy ladattaessa käyttäjälle tällä nimellä. Samaan asiaan liittyvissä eri dokumenteissa" +
        "tulee käyttää eri nimiä (myös case insensitiivisesti eri nimiä).\n" +
        "\n" +
        "Sallittuja merkkejä ovat A-Ö a-ö 0-9  _ .\n" +
        "Merkkirajauksen syy on se, että asiointitilin käyttäjä saisi mahdollisimman suurella varmuudella avattua" +
        "dokumentin, kun siinä ei ole mitään erikoismerkkejä.\"\n", required = true)
    private String nimi;
    @NotNull @Size(max=200)
    @DtoPath("tiedostonKuvaus")
    @ApiModelProperty(value="ASIAKAS. Asiakirjan selite.", required = true)
    private String kuvaus;
    @Size(max=200) // TODO: validate URL pattern
    @ApiModelProperty(value="ASIAKAS. Tiedoston URL, josta asiakirja on ladattavissa, " +
                "jos käytetään viranomaisen omaa välivarastoa. Asiointitilillä näytetään linkki kyseiseen osoitteeseen.",
            notes="https://opintopolku.fi/omatsivut/secure/paikanvastaanotto/<id> (?)" +
                    "Jompi kumpi siis annetaan / Tiedosto. Joko URL tai Sisälto")
    @DtoPath("tiedostoURL")
    private String url;
    @ApiModelProperty(value="ASIAKAS. Tiedosto BASE64 -enkoodattuna, jos tallennetaan tiedosto " +
                "asiointitilipalvelun välivarastoon. Asiointitililtä saa avattua kyseisen tiedoston.\n",
            notes = "Jos on linkkinä niin ei.")
    @DtoPath("tiedostoSisalto")
    private String sisalto;
    @NotNull
    @DtoPath("tiedostoKoko") @DtoConverted // to string
    @ApiModelProperty(value="ASIAKAS. Tiedoston koko kilotavuina. Pyöristetään ylöspäin seuraavaan kiloon.",
            notes = "Tarkkuudella ei ole väliä. Vain suuntaa antavana tietona asiakkaalle että tietää millaista" +
                    "tiedostoa on avaamassa.",
            required = true)
    private Integer koko;
    @NotNull @Size(max=150)
    @DtoPath("tiedostoMuoto")
    @ApiModelProperty(value= "ASIAKAS. Tiedoston formaatti. MimeTyyppi (esimerkiksi application/pdf)",
            required = true)
    private String muoto;

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSisalto() {
        return sisalto;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }

    public Integer getKoko() {
        return koko;
    }

    public void setKoko(Integer koko) {
        this.koko = koko;
    }

    public String getMuoto() {
        return muoto;
    }

    public void setMuoto(String muoto) {
        this.muoto = muoto;
    }
}
