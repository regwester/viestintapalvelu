package fi.vm.sade.ryhmasahkoposti.externalinterface.component;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.HenkiloResource;

/**
 * Komponenttiluokka henkilon tietojen hakemiseksi CXF:n avulla {@link service-context.xml}
 * 
 * @author vehei1
 *
 */
@Component
public class PersonComponent {
    @Resource
    private HenkiloResource henkiloResourceClient;
    
    /**
     * Hakee henkilön tiedot oid-tunnuksella
     * 
     * @param oid Henkilön oid-tunnus
     * @return Henkilön tiedot
     */
    public Henkilo getPerson(String oid) {
        return henkiloResourceClient.findByOid(oid);
    }
}
