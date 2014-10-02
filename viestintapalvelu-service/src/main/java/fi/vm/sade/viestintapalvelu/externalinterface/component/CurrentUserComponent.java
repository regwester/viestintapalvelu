package fi.vm.sade.viestintapalvelu.externalinterface.component;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.exception.ExternalInterfaceException;
import fi.vm.sade.viestintapalvelu.externalinterface.api.OmattiedotResource;

/**
 * Komponenttiluokka omien tietojen hakemiseksi käyttäen CXF:ää {@link service-context.xml}
 * 
 * @author vehei1
 *
 */
@Component
public class CurrentUserComponent {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private OmattiedotResource omattiedotResourceClient;
    
    /**
     * Hakee kirjaantuneen käyttäjän tiedot
     * 
     * @return Henkilon tiedot
     */
    public Henkilo getCurrentUser() {
        try {
            return omattiedotResourceClient.currentHenkiloTiedot();
        } catch (Exception e) {
            logger.error("Error getting current user: " + e.getMessage(), e);
            throw new ExternalInterfaceException("error.msg.gettingCurrentUserFailed", e);
        }
    }

    /**
     * Hakee kirjaantuneen käyttäjän organisaattioiden tiedot
     * 
     * @return Lista henkilön organisaattiotietoja
     */
    public List<OrganisaatioHenkilo> getCurrentUserOrganizations() {
        try {
            return omattiedotResourceClient.currentHenkiloOrganisaatioHenkiloTiedot();
        } catch (Exception e) {
            logger.error("Error getting current user's organizations: " + e.getMessage(), e);
            throw new ExternalInterfaceException(e);
        }
    }
}
