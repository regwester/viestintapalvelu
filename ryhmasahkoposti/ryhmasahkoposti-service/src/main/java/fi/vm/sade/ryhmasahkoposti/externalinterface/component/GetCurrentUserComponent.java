package fi.vm.sade.ryhmasahkoposti.externalinterface.component;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.OmattiedotResource;

/**
 * Komponenttiluokka omien tietojen hakemiseksi käyttäen CXF:ää {@link service-context.xml}
 * 
 * @author vehei1
 *
 */
@Component
public class GetCurrentUserComponent {
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
}
