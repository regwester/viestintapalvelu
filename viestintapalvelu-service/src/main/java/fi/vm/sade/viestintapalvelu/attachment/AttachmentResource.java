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

package fi.vm.sade.viestintapalvelu.attachment;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.viestintapalvelu.Urls;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 15:06
 */
@Path(Urls.ATTACHMENT_RESOURCE_PATH)
@Api(value = "/" + Urls.API_PATH + "/" + Urls.ATTACHMENT_RESOURCE_PATH,
        description = "Liiterajapinta ryhmäsähköpostipalvelua varten")
public interface AttachmentResource {
    public static final String AttachmentByUri = "Palauttaa liitteen URI-tunnisteella";

    @GET
    @Path("/getByUri")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(value = AttachmentByUri, notes = AttachmentByUri, response = EmailAttachment.class)
    public EmailAttachment downloadByUri(@QueryParam("uri") @ApiParam(name="uri",
            value = "Liitteen URI-tunniste") String uri);

    @DELETE
    @Path("/urisDownloaded")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    @ApiOperation(value = AttachmentByUri, notes = AttachmentByUri, response = EmailAttachment.class)
    public void deleteByUris(List<String> uris);

}
