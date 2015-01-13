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

import fi.vm.sade.viestintapalvelu.externalinterface.component.OrganizationComponent;

/**
 * Camel ja CXF reititys organisaatiopalveluun
 * 
 * @author vehei1
 *
 */
@Component
public class OrganisaatioRoute extends SpringRouteBuilder {
    private static Logger LOGGER = LoggerFactory.getLogger(OrganisaatioRoute.class);
    private static String ROUTE_GET_ORGANISATION_PARENTS = "direct:getParentOids";
    private OrganizationComponent organizationComponent;

    /**
     * Muodostin organisaation hakukomponentin asettamiseksi
     * 
     * @param organizationComponent
     *            Organisaation hakukomponentti
     */
    @Autowired
    public OrganisaatioRoute(OrganizationComponent organizationComponent) {
        this.organizationComponent = organizationComponent;
    }

    /**
     * Muodostaa organisaation isätietojen hakuun liittyvät reitit
     * organisaatiopalveluun
     * 
     */
    @Override
    public void configure() throws Exception {
        LOGGER.info("Configure route to OrganisaatioResource");
        from(ROUTE_GET_ORGANISATION_PARENTS).bean(organizationComponent);
    }
}
