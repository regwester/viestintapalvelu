package fi.vm.sade.viestintapalvelu.externalinterface.component;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.externalinterface.api.OmattiedotResource;

/**
 * Komponenttiluokka omien tietojen hakemiseksi käyttäen CXF:ää {@link service-context.xml}
 * 
 * @author vehei1
 *
 */
@Component
public class CurrentUserComponent {
    @Resource
    private OmattiedotResource omattiedotResourceClient;
    
    /**
     * Hakee kirjaantuneen käyttäjän tiedot
     * 
     * @return Henkilon tiedot
     */
    public Henkilo getCurrentUser() {
        return omattiedotResourceClient.currentHenkiloTiedot();
    }

    /**
     * Hakee kirjaantuneen käyttäjän organisaattioiden tiedot
     * 
     * @return Lista henkilön organisaattiotietoja
     */
    public List<OrganisaatioHenkilo> getCurrentUserOrganizations() {
        return omattiedotResourceClient.currentHenkiloOrganisaatioHenkiloTiedot();
    }
}
