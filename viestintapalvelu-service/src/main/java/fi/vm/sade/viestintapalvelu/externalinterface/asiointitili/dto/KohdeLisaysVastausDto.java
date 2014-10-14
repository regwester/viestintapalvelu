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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.ratamaa.dtoconverter.annotation.DtoPath;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 10:01
 */
public class KohdeLisaysVastausDto implements Serializable  {
    private static final long serialVersionUID = -9147756164857667593L;

    @DtoPath("tilaKoodi.tilaKoodi")
    private int tilaKoodi;
    @DtoPath("tilaKoodi.tilaKoodiKuvaus")
    private String kuvaus;
    @DtoPath("tilaKoodi.sanomaTunniste")
    private String sanomaTunniste;
    private int kohdeMaara;
    @XmlElement(name = "Kohteet")
    @DtoConversion
    private List<KohdeJaAsiakasTilaDto> kohteet = new ArrayList<KohdeJaAsiakasTilaDto>();

    public int getTilaKoodi() {
        return tilaKoodi;
    }

    public void setTilaKoodi(int tilaKoodi) {
        this.tilaKoodi = tilaKoodi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public String getSanomaTunniste() {
        return sanomaTunniste;
    }

    public void setSanomaTunniste(String sanomaTunniste) {
        this.sanomaTunniste = sanomaTunniste;
    }

    public int getKohdeMaara() {
        return kohdeMaara;
    }

    public void setKohdeMaara(int kohdeMaara) {
        this.kohdeMaara = kohdeMaara;
    }

    public List<KohdeJaAsiakasTilaDto> getKohteet() {
        return kohteet;
    }

    public void setKohteet(List<KohdeJaAsiakasTilaDto> kohteet) {
        this.kohteet = kohteet;
    }
}
