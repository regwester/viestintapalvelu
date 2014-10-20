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

import fi.ratamaa.dtoconverter.annotation.DtoConversion;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 10:03
 */
public class KohdeJaAsiakasTilaDto implements Serializable  {
    private static final long serialVersionUID = 1851024167335255632L;

    private String viranomaisTunniste;
    @DtoConversion
    private List<AsiakasJaKohteenTilaDto> asiakas = new ArrayList<AsiakasJaKohteenTilaDto>();

    public String getViranomaisTunniste() {
        return viranomaisTunniste;
    }

    public void setViranomaisTunniste(String viranomaisTunniste) {
        this.viranomaisTunniste = viranomaisTunniste;
    }

    public List<AsiakasJaKohteenTilaDto> getAsiakas() {
        return asiakas;
    }

    public void setAsiakas(List<AsiakasJaKohteenTilaDto> asiakas) {
        this.asiakas = asiakas;
    }
}
