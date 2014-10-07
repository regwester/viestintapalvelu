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

package fi.vm.sade.viestintapalvelu.externalinterface.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuDetailsDto;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuListDto;

/**
 * User: ratamaa
 * Date: 7.10.2014
 * Time: 12:49
 */
@Component
@Path("/haku")
public interface TarjontaHakuResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    List<HakuListDto> hakus() throws Exception;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{oid}")
    HakuDetailsDto hakuByOid(@PathParam("oid") String oid) throws Exception;

}
