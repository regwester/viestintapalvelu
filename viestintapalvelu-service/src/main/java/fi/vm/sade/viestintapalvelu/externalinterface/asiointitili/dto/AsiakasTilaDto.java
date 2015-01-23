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

import org.joda.time.DateTime;

import fi.ratamaa.dtoconverter.annotation.DtoConverted;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 10:43
 */
public class AsiakasTilaDto implements Serializable  {
    private static final long serialVersionUID = -4760297480679002864L;

    private int tila;
    @DtoConverted
    private DateTime tilaPvm;
    private String asiakasTunnus;
    private String tunnusTyyppi;

    public int getTila() {
        return tila;
    }

    public void setTila(int tila) {
        this.tila = tila;
    }

    public DateTime getTilaPvm() {
        return tilaPvm;
    }

    public void setTilaPvm(DateTime tilaPvm) {
        this.tilaPvm = tilaPvm;
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
