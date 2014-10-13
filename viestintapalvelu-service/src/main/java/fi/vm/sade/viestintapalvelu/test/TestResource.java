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

package fi.vm.sade.viestintapalvelu.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import fi.suomi.asiointitili.VastausWS2;
import fi.vm.sade.viestintapalvelu.asiointitili.AsiointitiliService;

/**
 * User: ratamaa
 * Date: 10.10.2014
 * Time: 16:15
 */
@Component
@Path("test")
@PreAuthorize("isAuthenticated()")
public class TestResource {
    private static final Logger logger = LoggerFactory.getLogger(TestResource.class);

    @Autowired
    private AsiointitiliService asiointitiliService;

    @GET
    @Produces("text/json")
    @Path("/testKysely")
    public Response testSome() {
        VastausWS2 vastaus = asiointitiliService.kysely();
        return Response.ok().entity(vastaus).build();
    }
}
