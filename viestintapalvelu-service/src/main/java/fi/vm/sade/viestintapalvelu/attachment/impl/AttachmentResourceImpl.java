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
package fi.vm.sade.viestintapalvelu.attachment.impl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import fi.vm.sade.ryhmasahkoposti.api.constants.SecurityConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.attachment.AttachmentResource;
import fi.vm.sade.viestintapalvelu.attachment.AttachmentService;
import fi.vm.sade.viestintapalvelu.attachment.dto.UrisContainerDto;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 15:45
 */
@Path(Urls.ATTACHMENT_RESOURCE_PATH)
@Api(value = "/" + Urls.API_PATH + "/" + Urls.ATTACHMENT_RESOURCE_PATH,
        description = "Liiterajapinta ryhmäsähköpostipalvelua varten")
@Component
@PreAuthorize(SecurityConstants.USER_IS_AUTHENTICATED)
public class AttachmentResourceImpl implements AttachmentResource {
    public static final String AttachmentByUri = "Palauttaa liitteen URI-tunnisteella";
    private static final Logger logger = LoggerFactory.getLogger(AttachmentResourceImpl.class);

    @Autowired
    private AttachmentService attachmentService;

    @GET
    @Path("/getByUri")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(value = AttachmentByUri, notes = AttachmentByUri, response = EmailAttachment.class)
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize(SecurityConstants.SYSTEM_ACCOUNT_ATTACHMENT_DOWNLOAD)
    public EmailAttachment downloadByUri(@QueryParam("uri") @ApiParam(name="uri",
            value = "Liitteen URI-tunniste") String uriStr) {
        AttachmentUri uri = new AttachmentUri(uriStr);
        switch (uri.getType()) {
            case LetterReceiverLetterAttachment:
                return attachmentService.getLetterAttachment(uri.getLongParameter(0));
        default: return null;
        }
    }


    @POST
    @Path("/urisDownloaded")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(value = AttachmentByUri, notes = AttachmentByUri, response = EmailAttachment.class)
    @Override
    @Transactional
    @PreAuthorize(SecurityConstants.SYSTEM_ACCOUNT_ATTACHMENT_DOWNLOAD)
    public Response deleteByUris(UrisContainerDto urisContainer) {
        if (urisContainer == null || urisContainer.getUris() == null) {
            return Response.noContent().build();
        }
        for (String uriStr : urisContainer.getUris()) {
            AttachmentUri uri = new AttachmentUri(uriStr);
            switch (uri.getType()) {
                case LetterReceiverLetterAttachment:
                    attachmentService.markLetterReceiverAttachmentDownloaded(uri.getLongParameter(0));
                    break;
                default:
                    // Continue to next one (not implemented / ignored):
                    logger.error("Unimplemented URI type: " + uri.getType());
                    break;
            }
        }
        return Response.ok().build();
    }

    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
}
