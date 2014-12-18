package fi.vm.sade.ryhmasahkoposti.externalinterface.component;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.OmattiedotResource;
import fi.vm.sade.viestintapalvelu.common.exception.ExternalInterfaceException;

/**
 * Komponenttiluokka omien tietojen hakemiseksi käyttäen CXF:ää {@link service-context.xml}
 * 
 * @author vehei1
 *
 */
@Component
public class CurrentUserComponent {
    private static Logger LOGGER = LoggerFactory.getLogger(CurrentUserComponent.class);
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
            LOGGER.error(e.getMessage());
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
            LOGGER.error(e.getMessage());
            throw new ExternalInterfaceException("error.msg.gettingCurrentUserOrganizationFailed", e);
        }
    }
}
