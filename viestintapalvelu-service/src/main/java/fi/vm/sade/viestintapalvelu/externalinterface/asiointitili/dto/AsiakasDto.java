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

import fi.vm.sade.viestintapalvelu.util.ValidHetu;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 9:50
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("Asiaan liittyvän asiakkaan tiedot.")
public class AsiakasDto implements Serializable {
    private static final long serialVersionUID = 533428770069677860L;

    @NotNull @ValidHetu
    @Size(max = 20)
    @ApiModelProperty(value="Asiakkaan tunniste. Aina hetu. Rajapinnassa varauduttu muihin tunnustyyppeihin," +
            "mutta HETU on ainoa tuettu.", notes = "väli A ja hetun tarkastusmerkki isoilla kirjaimilla.",
            required = true)
    private String asiakasTunnus;
    @NotNull
    @Size(max = 10)
    @ApiModelProperty(value="Asiakkaan tunnisteen tyyppi, Tuettu arvo: SSN", required = true)
    private String tunnusTyyppi;

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
