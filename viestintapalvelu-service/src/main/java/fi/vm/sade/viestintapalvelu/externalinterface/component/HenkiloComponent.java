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
package fi.vm.sade.viestintapalvelu.externalinterface.component;

import fi.vm.sade.externalinterface.OppijanumeroRekisteriRestClient;
import fi.vm.sade.viestintapalvelu.common.exception.ExternalInterfaceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.dto.HenkiloDto;

/**
 * Komponenttiluokka omien tietojen hakemiseksi käyttäen CXF:ää {@link service
 * -context.xml}
 * 
 * @author vehei1
 *
 */
@Component
public class HenkiloComponent {

    private final OppijanumeroRekisteriRestClient oppijanumeroRekisteriRestClient;

    @Autowired
    public HenkiloComponent(OppijanumeroRekisteriRestClient oppijanumeroRekisteriRestClient) {
        this.oppijanumeroRekisteriRestClient = oppijanumeroRekisteriRestClient;
    }

    /**
     * Hakee henkilön tiedot oid:n perusteella
     * 
     * @return Henkilon tiedot
     */
    public HenkiloDto getHenkilo(String oid) {
        try {
            return oppijanumeroRekisteriRestClient.getHenkilo(oid);
        } catch (Exception e) {
            throw new ExternalInterfaceException("error.msg.gettingPersonDataFailed", e);
        }
    }
}
