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
package fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.ratamaa.dtoconverter.annotation.DtoConverted;
import fi.ratamaa.dtoconverter.annotation.DtoNotExported;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 9:42
 */
@ApiModel("Yhden asian tiedot kokoava elementti")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KohdeDto implements Serializable {
    private static final long serialVersionUID = -6161720816265352792L;

    @Size(min=1) @Valid
    @DtoConversion(path="asiakas")
    @ApiModelProperty("Asiaan liittyvän asiakkaan tiedot. Asia liitetään kyseisen asiakkaan asiointitiliin.\n" +
       "Mahdollistetaan saman asian lähettäminen useille asiakkaille, jolloin näitä elementtejä voi olla useita.")
    private List<AsiakasDto> asiakkaat = new ArrayList<>();
    @NotNull
    @Size(max = 100)
    private String viranomaisTunniste;
    @Valid
    @DtoConversion @ApiModelProperty("Käytetään, jos halutaan liittää viesti aiemmin lähetettyyn viestiin" +
            "asiointitilillä, jolloin viesti näkyy samassa viestiketjussa.")
    private ViittausDto viittaus;
    @DtoConverted
    @ApiModelProperty(value="ASIAKAS.Onko kyseessä todisteellinen tiedoksianto, joka vaatii kuittauksen lukemisesta." +
            "Kyllä = viesti vastaa juridisesti Postin tarjoamaa todisteellista tiedoksiantoa, eli käyttäjän pitää" +
            "klikata Kuittaa vastaanotetuksi ennen liitteiden avaamista.",
            required = true, notes = "true=kyllä, false=ei (TÄTÄ KÄYTETÄÄN)")
    private boolean vahvistusVaatimus=false;
    @DtoConverted
    @ApiModelProperty(value="Parametrin avulla voidaan pakottaa asiointitili lähettämään kuittausviesti kohteesta," +
            "vaikka lukukuittausten lähettäminen olisi muuten disabloitu.")
    protected boolean vaadiLukukuittaus;
    @Size(max=100)
    @ApiModelProperty(value="ASIAKAS. Asiointitilillä näytettävä asiakirjan tunniste. Voi olla sama kuin ViranomaisTunniste." +
            "Asiointitilipalvelu ainoastaan näyttää tiedon sellaisenaan asiointitilillä.", required = false,
            notes = "Mikä tahansa merkkijono tai tyhjä. Näytetään asiakkaalle viestin tiedoissa.\n")
    private String asiaNumero;
    @NotNull
    @Size(max=400)
    @ApiModelProperty(value="ASIAKAS. Asian otsikkotieto.\n" +
            "Viranomaisjärjestelmä tuottaa tämän suoraan asiakkaan omalla kielellä.\n" +
            "Viranomaisella tulee olla asioidakseen tieto vastaanottajan kielestä.", required = true)
    private String nimeke;
    @NotNull @DtoConverted
    @ApiModelProperty(value="ASIAKAS. Lähetyspäivämäärä", required = true)
    private DateTime lahetysPvm=new DateTime();
    @Size(max=200)
    @ApiModelProperty("ASIAKAS. Lähettäneen viranomaisen tarkempi nimi." +
            "Viranomainen ja palvelutunnus tulevat kutsun otsikkotiedoissa ja asiointitili näyttää asiakkaalle" +
            "niiden mukaan keneltä asia on tullut. \n" +
            "Tämän kentän avulla voi antaa tarkemman lähettäjätiedon.")
    private String lahettajaNimi;
    @NotNull @Size(min=1)
    private String kuvausTeksti;
    @DtoConverted
    @ApiModelProperty("ASIAKAS. Tieto maksullisuudesta.")
    private boolean maksullisuus=false;
    @Size(max=200)
    @ApiModelProperty("ASIAKAS. Maksun vapaamuotoinen kuvaustieto. Esim. hinta ja maksuohjeet.\n" +
            "HUOM! Asiointitilipalvelu ei tarjoa verkkomaksutoiminnallisuuksia.")
    private String maksamisKuvausTeksti;
    @Valid
    @DtoConversion
    @ApiModelProperty("Asiaan liittyvät asiakirjat.")
    private List<TiedostoDto> tiedostot = new ArrayList<>();
    @Size(max=1000)
    @ApiModelProperty("Heräteviestin sisältö, joka välitetään matkapuhelimeen. Uusi kenttä 12/2013 alkaen.")
    protected String smsLisatieto;
    @Size(max=200)
    @ApiModelProperty("Heräteviestin otsikko, joka välitetään sähköpostiin.\n" +
            "Uusi kenttä 27.11.2013 alkaen.")
    protected String emailLisatietoOtsikko;
    @ApiModelProperty("Heräteviestin sisältö, joka välitetään sähköpostiin.\n" +
            "Uusi kenttä 27.11.2013 alkaen.")
    protected String emailLisatietoSisalto;
    @Size(max=200)
    @DtoNotExported // ei vielä käytössä
    @ApiModelProperty("Asiakkaalle tallennettava SMS-notifikaatin lähetysnumero, joka tallennetaan asiakaan tietoihin, " +
            "mikäli kutsun seurauksena asiakkaalle luodaan pikatili. Mikäli asiakkaalla on tili käytössä, " +
            "ei kenttää huomioida. Kenttä ei ole vielä käytössä.")
    protected String tavoitettavuusTietoSMS;
    @Size(max=200)
    @DtoNotExported // ei vielä käytössä
    @ApiModelProperty("Asiakkaalle tallennettava sähköpostinotifikaatin lähetysnumero, " +
            "joka tallennetaan asiakaan tietoihin, mikäli kutsun seurauksena asiakkaalle luodaan pikatili." +
            "Mikäli asiakkaalla on tili käytössä, ei kenttää huomioida. Kenttä ei ole vielä käytössä.")
    protected String tavoitettavuusTietoEmail;

    public List<AsiakasDto> getAsiakkaat() {
        return asiakkaat;
    }

    public void setAsiakkaat(List<AsiakasDto> asiakkaat) {
        this.asiakkaat = asiakkaat;
    }

    public String getViranomaisTunniste() {
        return viranomaisTunniste;
    }

    public void setViranomaisTunniste(String viranomaisTunniste) {
        this.viranomaisTunniste = viranomaisTunniste;
    }

    public ViittausDto getViittaus() {
        return viittaus;
    }

    public void setViittaus(ViittausDto viittaus) {
        this.viittaus = viittaus;
    }

    public String getAsiaNumero() {
        return asiaNumero;
    }

    public void setAsiaNumero(String asiaNumero) {
        this.asiaNumero = asiaNumero;
    }

    public String getNimeke() {
        return nimeke;
    }

    public void setNimeke(String nimeke) {
        this.nimeke = nimeke;
    }

    public DateTime getLahetysPvm() {
        return lahetysPvm;
    }

    public void setLahetysPvm(DateTime lahetysPvm) {
        this.lahetysPvm = lahetysPvm;
    }

    public String getLahettajaNimi() {
        return lahettajaNimi;
    }

    public void setLahettajaNimi(String lahettajaNimi) {
        this.lahettajaNimi = lahettajaNimi;
    }

    public String getKuvausTeksti() {
        return kuvausTeksti;
    }

    public void setKuvausTeksti(String kuvausTeksti) {
        this.kuvausTeksti = kuvausTeksti;
    }

    public String getMaksamisKuvausTeksti() {
        return maksamisKuvausTeksti;
    }

    public void setMaksamisKuvausTeksti(String maksamisKuvausTeksti) {
        this.maksamisKuvausTeksti = maksamisKuvausTeksti;
    }

    public List<TiedostoDto> getTiedostot() {
        return tiedostot;
    }

    public void setTiedostot(List<TiedostoDto> tiedostot) {
        this.tiedostot = tiedostot;
    }

    public boolean isVahvistusVaatimus() {
        return vahvistusVaatimus;
    }

    public void setVahvistusVaatimus(boolean vahvistusVaatimus) {
        this.vahvistusVaatimus = vahvistusVaatimus;
    }

    public boolean isMaksullisuus() {
        return maksullisuus;
    }

    public void setMaksullisuus(boolean maksullisuus) {
        this.maksullisuus = maksullisuus;
    }

    public String getTavoitettavuusTietoEmail() {
        return tavoitettavuusTietoEmail;
    }

    public void setTavoitettavuusTietoEmail(String tavoitettavuusTietoEmail) {
        this.tavoitettavuusTietoEmail = tavoitettavuusTietoEmail;
    }

    public String getTavoitettavuusTietoSMS() {
        return tavoitettavuusTietoSMS;
    }

    public void setTavoitettavuusTietoSMS(String tavoitettavuusTietoSMS) {
        this.tavoitettavuusTietoSMS = tavoitettavuusTietoSMS;
    }

    public String getEmailLisatietoSisalto() {
        return emailLisatietoSisalto;
    }

    public void setEmailLisatietoSisalto(String emailLisatietoSisalto) {
        this.emailLisatietoSisalto = emailLisatietoSisalto;
    }

    public String getEmailLisatietoOtsikko() {
        return emailLisatietoOtsikko;
    }

    public void setEmailLisatietoOtsikko(String emailLisatietoOtsikko) {
        this.emailLisatietoOtsikko = emailLisatietoOtsikko;
    }

    public String getSmsLisatieto() {
        return smsLisatieto;
    }

    public void setSmsLisatieto(String smsLisatieto) {
        this.smsLisatieto = smsLisatieto;
    }

    public boolean isVaadiLukukuittaus() {
        return vaadiLukukuittaus;
    }

    public void setVaadiLukukuittaus(boolean vaadiLukukuittaus) {
        this.vaadiLukukuittaus = vaadiLukukuittaus;
    }
}
