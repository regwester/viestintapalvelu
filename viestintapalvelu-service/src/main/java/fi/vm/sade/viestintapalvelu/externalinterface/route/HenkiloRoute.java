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
package fi.vm.sade.viestintapalvelu.externalinterface.route;

import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.externalinterface.component.HenkiloComponent;

/**
 * Luokka Camel- ja CXF-reitin luomiseksi autentikaatiopalveluun omien tietojen
 * hakemiseksi
 * 
 * @author vehei1
 *
 */
@Component
public class HenkiloRoute extends SpringRouteBuilder {
    private static Logger LOGGER = LoggerFactory.getLogger(OmattiedotRoute.class);
    private static String ROUTE_GET_HENKILO = "direct:getHenkilo";
    private HenkiloComponent henkiloComponent;

    /**
     * Muodostin omat tiedot komponentin asettamiseksi
     * 
     * @param currentUserComponent
     *            Hae omat tiedot komponentti
     */
    @Autowired
    public HenkiloRoute(HenkiloComponent henkiloComponent) {
        this.henkiloComponent = henkiloComponent;
    }

    /**
     * Muodostaa omien tietojen hakuun liittyvät reitit autentikointipalveluun
     * 
     */
    @Override
    public void configure() throws Exception {
        LOGGER.info("Configure route to OmatTiedotResource");
        from(ROUTE_GET_HENKILO).bean(henkiloComponent);
    }
}
