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

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 10:21
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsiakasTilaTarkastusKyselyDto implements Serializable {
    private static final long serialVersionUID = 590486540331626563L;

    @DtoConversion
    @Size(min=1) @Valid
    private List<AsiakasDto> asiakkaat = new ArrayList<AsiakasDto>();

    public List<AsiakasDto> getAsiakkaat() {
        return asiakkaat;
    }

    public void setAsiakkaat(List<AsiakasDto> asiakkaat) {
        this.asiakkaat = asiakkaat;
    }
}
