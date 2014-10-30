package fi.vm.sade.ryhmasahkoposti.externalinterface.component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.exception.ExternalInterfaceException;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.OrganisaatioResource;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.dto.OrganisaatioHierarchyDto;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.dto.OrganisaatioHierarchyResultsDto;

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
     * @param oid Organisaation oid-tunnus
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
     * @param organisaatio Organisaation tiedot
     * @return Organisaation nimi
     */
    public String getNameOfOrganisation(OrganisaatioRDTO organisaatio) {
        String[] language = {"fi", "sv", "en"};
        
        if (organisaatio.getNimi() == null) {
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
     * @return organisaatiohierarkian
     */
    public OrganisaatioHierarchyDto getOrganizationHierarchy() {
        try {
            OrganisaatioHierarchyResultsDto rootResults = organisaatioResourceClient.hierarchy(true);
            /// XXX: doesn't include the root:
            OrganisaatioHierarchyDto root = new OrganisaatioHierarchyDto();
            root.setChildren(rootResults.getOrganisaatiot());
            root.setOid(rootOrganizationOID);
            Map<String,String> nimi = new HashMap<String, String>();
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
