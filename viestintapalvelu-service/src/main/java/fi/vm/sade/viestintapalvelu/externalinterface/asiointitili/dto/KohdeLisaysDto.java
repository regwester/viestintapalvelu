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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;

/**
 * User: ratamaa
 * Date: 13.10.2014
 * Time: 17:48
 */
@ApiModel("KyselyWS2")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KohdeLisaysDto implements Serializable {
    private static final long serialVersionUID = 1301104464235527503L;

    @Size(min=1) @Valid
    @ApiModelProperty("Kyselyn kohteet")
    @DtoConversion
    private List<KohdeDto> kohteet = new ArrayList<KohdeDto>();

    public List<KohdeDto> getKohteet() {
        return kohteet;
    }

    public void setKohteet(List<KohdeDto> kohteet) {
        this.kohteet = kohteet;
    }
}
