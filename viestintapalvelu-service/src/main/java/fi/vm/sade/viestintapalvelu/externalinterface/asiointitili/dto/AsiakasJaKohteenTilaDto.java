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

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 10:06
 */
public class AsiakasJaKohteenTilaDto implements Serializable {
    private static final long serialVersionUID = 4695143431871541343L;
    
    private String asiointitiliTunniste;
    private int kohteenTila;
    private String kohteenTilaKuvaus;
    private String asiakasTunnus;
    private String tunnusTyyppi;

    public String getAsiointitiliTunniste() {
        return asiointitiliTunniste;
    }

    public void setAsiointitiliTunniste(String asiointitiliTunniste) {
        this.asiointitiliTunniste = asiointitiliTunniste;
    }

    public int getKohteenTila() {
        return kohteenTila;
    }

    public void setKohteenTila(int kohteenTila) {
        this.kohteenTila = kohteenTila;
    }

    public String getKohteenTilaKuvaus() {
        return kohteenTilaKuvaus;
    }

    public void setKohteenTilaKuvaus(String kohteenTilaKuvaus) {
        this.kohteenTilaKuvaus = kohteenTilaKuvaus;
    }

    public String getAsiakasTunnus() {
        return asiakasTunnus;
    }

    public void setAsiakasTunnus(String asiakasTunnus) {
        this.asiakasTunnus = asiakasTunnus;
    }

    public String getTunnusTyyppi() {
        return tunnusTyyppi;
    }

    public void setTunnusTyyppi(String tunnusTyyppi) {
        this.tunnusTyyppi = tunnusTyyppi;
    }
}
