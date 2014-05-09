package fi.vm.sade.viestintapalvelu.externalinterface.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.exception.ExternalInterfaceException;
import fi.vm.sade.viestintapalvelu.externalinterface.api.OrganisaatioResource;

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
    
    /**
     * Hae organisaation isätiedot
     * 
     * @param oid Organisaation oid-tunnus
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
     * Palauttaa listan organisaation isärakenteesta
     * 
     * @param parents Organisaation isärakenne merkkijonona eroteltuna "/"-merkillä
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
