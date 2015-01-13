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
package fi.vm.sade.viestintapalvelu.externalinterface.component;

import java.util.*;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.viestintapalvelu.exception.ExternalInterfaceException;
import fi.vm.sade.viestintapalvelu.externalinterface.api.OrganisaatioResource;
import fi.vm.sade.viestintapalvelu.externalinterface.api.OrganisaatioResourceWithoutAuthentication;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyDto;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyResultsDto;

/**
 * Komponenttiluokka organisaation tietojen hakemiseksi käyttäen CXF:ää
 * 
 * @author vehei1
 *
 */
@Component
public class OrganizationComponent {
    private static Logger LOGGER = LoggerFactory.getLogger(OrganizationComponent.class);
    @Resource
    private OrganisaatioResource organisaatioResourceClient;
    @Resource
    private OrganisaatioResourceWithoutAuthentication organisaatioResourceWithoutAuthenticationClient;

    @Value("${viestintapalvelu.rekisterinpitajaOID}")
    private String rootOrganizationOID;

    /**
     * Hae organisaation tiedot
     * 
     * @param oid
     *            Organisaation oid-tunnus
     * @return Organisaation tiedot
     */
    public OrganisaatioRDTO getOrganization(String oid) {
        try {
            return organisaatioResourceClient.getOrganisaatioByOID(oid);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ExternalInterfaceException("error.msg.gettingOrganizationDataFailed", e);
        }
    }

    /**
     * Palauttaa organisaation nimen
     * 
     * @param organisaatio
     *            Organisaation tiedot
     * @return Organisaation nimi
     */
    public String getNameOfOrganisation(OrganisaatioRDTO organisaatio) {
        String[] language = { "fi", "sv", "en" };
        if (organisaatio == null || organisaatio.getNimi() == null) {
            return "";
        }

        for (int i = 0; language.length > i; i++) {
            String nameOfOrganisation = organisaatio.getNimi().get(language[i]);
            if (nameOfOrganisation != null && !nameOfOrganisation.isEmpty()) {
                return nameOfOrganisation;
            }
        }

        return "";
    }

    /**
     * Hae organisaation isätiedot
     * 
     * @param oid
     *            Organisaation oid-tunnus
     * @return Organisaation isätiedot listana
     */
    public List<String> getOrganizationParents(String oid) {
        try {
            String organizationParents = organisaatioResourceClient.parentoids(oid);
            return getParentOidList(organizationParents);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ExternalInterfaceException("error.msg.gettingOrganizationDataFailed", e);
        }
    }

    /**
     * @return organisaatiohierarkian
     */
    public OrganisaatioHierarchyDto getOrganizationHierarchy() {
        try {
            OrganisaatioHierarchyResultsDto rootResults = organisaatioResourceWithoutAuthenticationClient.hierarchy(true);
            // / XXX: doesn't include the root:
            OrganisaatioHierarchyDto root = new OrganisaatioHierarchyDto();
            root.setChildren(rootResults.getOrganisaatiot());
            root.setOid(rootOrganizationOID);
            Map<String, String> nimi = new HashMap<String, String>();
            nimi.put("fi", "Opetushallitus");
            nimi.put("sv", "Utbildningsstyrelsen");
            nimi.put("en", "The Finnish National Board of Education");
            root.setNimi(nimi);
            return root;
        } catch (Exception e) {
            LOGGER.error("Exception occurred while fetching OrganisaatioHierarchyDto", e);
            throw new ExternalInterfaceException("error.msg.getOrganizationHierarchyFailed", e);
        }
    }

    /**
     * Palauttaa listan organisaation isärakenteesta
     * 
     * @param parents
     *            Organisaation isärakenne merkkijonona eroteltuna "/"-merkillä
     * @return Lista isärakenteesta
     */
    private List<String> getParentOidList(String parents) {
        if (parents == null || parents.isEmpty()) {
            return new ArrayList<String>();
        }

        String[] parentOids = parents.split("/");
        return Arrays.asList(parentOids);
    }
}
