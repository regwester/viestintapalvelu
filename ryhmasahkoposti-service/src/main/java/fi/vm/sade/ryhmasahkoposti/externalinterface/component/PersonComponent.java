/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.ryhmasahkoposti.externalinterface.component;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.HenkiloResource;
import fi.vm.sade.viestintapalvelu.common.exception.ExternalInterfaceException;

/**
 * Komponenttiluokka henkilon tietojen hakemiseksi CXF:n avulla {@link service-context.xml}
 * 
 * @author vehei1
 *
 */
@Component
public class PersonComponent {
    private static Logger LOGGER = LoggerFactory.getLogger(PersonComponent.class);
    @Resource
    private HenkiloResource henkiloResourceClient;
    
    /**
     * Hakee henkilön tiedot oid-tunnuksella
     * 
     * @param oid Henkilön oid-tunnus
     * @return Henkilön tiedot
     */
    public Henkilo getPerson(String oid) {
        try {
            return henkiloResourceClient.findByOid(oid);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
            throw new ExternalInterfaceException("error.msg.gettingPersonDataFailed", e);
        }
    }
}
