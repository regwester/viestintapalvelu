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

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fi.ratamaa.dtoconverter.annotation.DtoConverted;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 13:06
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HaeAsiakasTilojaKyselyDto implements Serializable {
    private static final long serialVersionUID = 7965935645471426330L;

    @NotNull
    @DtoConverted
    private DateTime kyselyAlku;
    @NotNull
    @DtoConverted
    private DateTime kyselyLoppu;

    public DateTime getKyselyAlku() {
        return kyselyAlku;
    }

    public void setKyselyAlku(DateTime kyselyAlku) {
        this.kyselyAlku = kyselyAlku;
    }

    public DateTime getKyselyLoppu() {
        return kyselyLoppu;
    }

    public void setKyselyLoppu(DateTime kyselyLoppu) {
        this.kyselyLoppu = kyselyLoppu;
    }
}
