package fi.vm.sade.viestintapalvelu.externalinterface.component;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.externalinterface.api.HenkiloResource;

/**
 * Komponenttiluokka omien tietojen hakemiseksi käyttäen CXF:ää {@link service-context.xml}
 * 
 * @author vehei1
 *
 */
@Component
public class HenkiloComponent {
    @Resource
    private HenkiloResource henkiloResourceClient;
    
    /**
     * Hakee henkilön tiedot oid:n perusteella
     * 
     * @return Henkilon tiedot
     */
    public Henkilo getHenkilo(String oid) {
        return henkiloResourceClient.getHenkiloByOid(oid);
    }
}
