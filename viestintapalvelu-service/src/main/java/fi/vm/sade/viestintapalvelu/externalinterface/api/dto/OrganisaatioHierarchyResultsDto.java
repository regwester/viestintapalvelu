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
package fi.vm.sade.viestintapalvelu.externalinterface.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * User: ratamaa Date: 29.10.2014 Time: 16:15
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisaatioHierarchyResultsDto implements Serializable {
    private static final long serialVersionUID = 1188550443593004724L;

    private List<OrganisaatioHierarchyDto> organisaatiot = new ArrayList<>();

    public List<OrganisaatioHierarchyDto> getOrganisaatiot() {
        return organisaatiot;
    }

    public void setOrganisaatiot(List<OrganisaatioHierarchyDto> organisaatiot) {
        this.organisaatiot = organisaatiot;
    }
}
