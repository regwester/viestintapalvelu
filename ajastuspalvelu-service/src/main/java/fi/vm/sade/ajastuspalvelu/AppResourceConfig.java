/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.ajastuspalvelu;

import org.glassfish.jersey.server.ResourceConfig;

import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider;
import com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider;

import fi.vm.sade.viestintapalvelu.common.exception.WebExceptionMapper;
import fi.vm.sade.viestintapalvelu.common.feature.JacksonFeature;
import fi.vm.sade.viestintapalvelu.common.resource.filter.CORSFilter;

/**
 * User: ratamaa
 * Date: 20.10.2014
 * Time: 18:00
 */
public class AppResourceConfig extends ResourceConfig {

    /*@Resource
    WebExceptionMapper exceptionMapper;
    */
    public AppResourceConfig() {

        // Register resources using package scan
        packages("fi.vm.sade.ajastuspalvelu.resource");
        packages("fi.vm.sade.generic");

        // Register custom filters, providers and features
        register(JacksonFeature.class);
        //register(CorsFilter.class);
        register(CORSFilter.class);
        //register exception handler
        //register(exceptionMapper);
        register(WebExceptionMapper.class);
        // Register swagger resources
        register(ApiListingResourceJSON.class);
        register(JerseyApiDeclarationProvider.class);
        register(JerseyResourceListingProvider.class);
    }
}
