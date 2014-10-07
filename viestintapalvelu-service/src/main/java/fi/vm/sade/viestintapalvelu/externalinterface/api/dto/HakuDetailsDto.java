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

package fi.vm.sade.viestintapalvelu.externalinterface.api.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * User: ratamaa
 * Date: 7.10.2014
 * Time: 14:08
 */
@ApiModel("Haun tiedot")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HakuDetailsDto implements Serializable {
    private static final long serialVersionUID = 6049610210699613479L;

    @ApiModelProperty("Haun oid")
    private String oid;
    @ApiModelProperty("Haun tila")
    private String tila;
    @ApiModelProperty("Haun nimi (avaimilla kieli_[koodi])")
    // in format: kieli_<code> -> nimi
    private Map<String,String> nimi = new HashMap<String, String>();

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTila() {
        return tila;
    }

    public void setTila(String tila) {
        this.tila = tila;
    }

    public Map<String, String> getNimi() {
        return nimi;
    }

    public void setNimi(Map<String, String> nimi) {
        this.nimi = nimi;
    }
}
