/*
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
 */

package fi.vm.sade.viestintapalvelu.letter;

import com.google.common.base.Optional;
import com.lowagie.text.DocumentException;
import com.wordnik.swagger.annotations.Api;
import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.template.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jonimake on 9.1.2015.
 */
@Component
@Path("preview")
@PreAuthorize("isAuthenticated()")
@Api(value = "/" + Urls.API_PATH + "/preview/")
public class PreviewDataResource {

    public static final String testApplicationId = "-1";
    public static final Logger log = LoggerFactory.getLogger(PreviewDataResource.class);

    @Autowired private TemplateService templateService;
    @Autowired private PreviewDataService previewDataService;

    @GET
    @Path("letterbatch/pdf")
    @Consumes("application/json")
    @Produces("application/pdf")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_LETTER)
    public byte[] dummyBatchPdf(@QueryParam("templateId") int templateId,
                                @QueryParam("templateState") String state) throws IOException, DocumentException {
        final Template template = templateService.findByIdAndState(templateId, ContentStructureType.letter, null);

        final byte[] previewPdf = previewDataService.getPreviewPdf(template, testApplicationId);
        return previewPdf;
    }

    @GET
    @Path("letterbatch/email")
    @Consumes("application/json")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_SEND_LETTER_EMAIL)
    public Response dummyBatchEmail(@QueryParam("templateId") int templateId,
                                @QueryParam("templateState") String state) throws IOException, DocumentException {
        final Template template = templateService.findByIdAndState(templateId, ContentStructureType.letter, null);
        String email = previewDataService.getEmailPreview(template, "");
        return Response.ok(email).header("Content-Disposition", "attachment; filename=\"preview.eml\"").build();
    }

}
