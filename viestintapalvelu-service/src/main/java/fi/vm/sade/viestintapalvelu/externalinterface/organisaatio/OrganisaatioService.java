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

package fi.vm.sade.viestintapalvelu.externalinterface.organisaatio;

import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyDto;

import java.util.List;

/**
 * User: ratamaa
 * Date: 29.10.2014
 * Time: 14:51
 */
public interface OrganisaatioService {

    /**
     * @param organizationOid
     * @return all children organization DTO objects for the given organizationOid
     */
    public OrganisaatioHierarchyDto getOrganizationHierarchy(String organizationOid);

    /**
     * @param organisaatioOid
     * @return all child organisaatio OIDs for given organisaatio including the organisaatio itself
     */
    List<String> findHierarchyOids(String organisaatioOid);

    /**
     * @param organisaatioOid
     * @return all child organisaatio OIDs for given organisaatio without including the orgnaisaatio itself
     */
    List<String> findAllChildrenOids(String organisaatioOid);

    /**
     * @param organisaatioOid
     * @return the parent OIDs for given organisaatio without including the orgnisaatio itself
     */
    List<String> findParentOids(String organisaatioOid);
}
