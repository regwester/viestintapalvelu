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

package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 15:06
 */
@Path("/attachment")
public interface AttachmentResource {

    @GET
    @Path("/getByUri")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    EmailAttachment downloadByUri(@QueryParam("uri") String uri);

    @POST
    @Path("/urisDownloaded")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    Response deleteByUris(UrisContainerDto urisContainerDto);

}
