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
public class OrganizationComponent {
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
    
    /**
     * Palauttaa organisaation nimen
     * 
     * @param organisaatio Organisaation tiedot
     * @return Organisaation nimi
     */
    public String getNameOfOrganisation(OrganisaatioRDTO organisaatio) {
        String[] language = {"fi", "sv", "en"};
        
        for (int i = 0; language.length > i; i++) {
            String nameOfOrganisation = organisaatio.getNimi().get(language[i]);
            if (nameOfOrganisation != null && !nameOfOrganisation.isEmpty()) {
                return nameOfOrganisation;
            }
        }
        
        return "";
    }

}
