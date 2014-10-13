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

package fi.vm.sade.viestintapalvelu.asiontitili;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import fi.suomi.asiointitili.KyselyWS2;
import fi.suomi.asiointitili.VastausWS2;
import fi.vm.sade.ryhmasahkoposti.api.constants.SecurityConstants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.AsiointitiliService;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.KyselyWS2Dto;

/**
 * User: ratamaa
 * Date: 10.10.2014
 * Time: 16:15
 */
@Component
@Api(value=Urls.ASIOINTITILI, description = "Kansalaisen asiointitilin tominnot")
@Path(Urls.ASIOINTITILI)
@PreAuthorize("isAuthenticated()")
public class AsiointitiliResource {
    private static final Logger logger = LoggerFactory.getLogger(AsiointitiliResource.class);

    @Autowired
    private AsiointitiliService asiointitiliService;

    @POST
    @PreAuthorize(SecurityConstants.ASIOINTITILI)
    @Consumes("text/json")
    @Produces("text/json")
    @Path("/kyselyWS2")
    @ApiOperation(value="Asiointilikyely WS2", response = VastausWS2.class)
    public Response kyselyWs2(@ApiParam("Kysely") KyselyWS2Dto kyselyWS2) {
        VastausWS2 vastaus = asiointitiliService.kyselyWS2(kyselyWS2);
        return Response.ok().entity(vastaus).build();
    }
}
