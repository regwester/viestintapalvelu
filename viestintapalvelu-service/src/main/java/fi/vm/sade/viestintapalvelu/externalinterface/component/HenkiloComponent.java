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

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.externalinterface.api.OppijanumerorekisteriHenkiloResource;

/**
 * Komponenttiluokka omien tietojen hakemiseksi käyttäen CXF:ää {@link service
 * -context.xml}
 * 
 * @author vehei1
 *
 */
@Component
public class HenkiloComponent {
    @Resource
    private OppijanumerorekisteriHenkiloResource oppijanumerorekisteriHenkiloResource;

    /**
     * Hakee henkilön tiedot oid:n perusteella
     * 
     * @return Henkilon tiedot
     */
    public Henkilo getHenkilo(String oid) {
        return oppijanumerorekisteriHenkiloResource.getHenkiloByOid(oid);
    }
}
