package fi.vm.sade.ryhmasahkoposti.externalinterface.component;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.OrganisaatioResource;

/**
 * Komponenttiluokka organisaation tietojen hakemiseksi käyttäen CXF:ää
 * 
 * @author vehei1
 *
 */
@Component
public class GetOrganizationComponent {
    @Resource
    private OrganisaatioResource organisaatioResourceClient;
    
    /**
     * Hae organisaation tiedot
     * 
     * @param oid Organisaation oid-tunnus
     * @return Organisaation tiedot
     */
    public OrganisaatioRDTO getOrganization(String oid) {
        return organisaatioResourceClient.getOrganisaatioByOID(oid);
    }
}
