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
package fi.vm.sade.ryhmasahkoposti.externalinterface.component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import fi.vm.sade.viestintapalvelu.externalinterface.component.ComponentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.OrganisaatioResource;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.dto.OrganisaatioHierarchyDto;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.dto.OrganisaatioHierarchyResultsDto;
import fi.vm.sade.viestintapalvelu.common.exception.ExternalInterfaceException;

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

    @Value("${viestintapalvelu.rekisterinpitajaOID:}")
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
        if (organisaatio.getNimi() == null) {
            return "";
        }
        return ComponentUtil.getOrganizationNameFromRDTO(organisaatio);
    }

    /**
     * @return organisaatiohierarkian
     */
    public OrganisaatioHierarchyDto getOrganizationHierarchy() {
        try {
            OrganisaatioHierarchyResultsDto rootResults = organisaatioResourceClient.hierarchy(true);
            // / XXX: doesn't include the root:
            OrganisaatioHierarchyDto root = new OrganisaatioHierarchyDto();
            root.setChildren(rootResults.getOrganisaatiot());
            root.setOid(rootOrganizationOID);
            Map<String, String> nimi = new HashMap<>();
            nimi.put("fi", "Opetushallitus");
            nimi.put("sv", "Utbildningsstyrelsen");
            nimi.put("en", "The Finnish National Board of Education");
            root.setNimi(nimi);
            return root;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ExternalInterfaceException("error.msg.getOrganizationHierarchyFailed", e);
        }
    }

}
