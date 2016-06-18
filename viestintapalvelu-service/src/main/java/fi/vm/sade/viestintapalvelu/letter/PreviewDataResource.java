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
 */
package fi.vm.sade.viestintapalvelu.letter;

import java.io.IOException;

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

import com.lowagie.text.DocumentException;
import com.wordnik.swagger.annotations.Api;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.template.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateService;

@Component("PreviewDataResource")
@Path("preview")
@PreAuthorize("isAuthenticated()")
@Api(value = "/" + Urls.API_PATH + "/preview/")
public class PreviewDataResource {

    public static final String testApplicationId = "-1";
    public static final Logger log = LoggerFactory.getLogger(PreviewDataResource.class);

    @Autowired
    private TemplateService templateService;
    @Autowired
    private PreviewDataService previewDataService;

    @POST
    @Path("letterbatch/pdf")
    @Consumes("application/json")
    @Produces("application/pdf")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_LETTER)
    public byte[] dummyBatchPdf(PreviewRequest request) throws IOException, DocumentException {
        try {
            final Template template = templateService.findByIdAndState(request.getTemplateId(), ContentStructureType.letter, request.getTemplateState());
            return previewDataService.getPreviewPdf(template, testApplicationId, request.getLetterContent());
        } catch (Exception e) {
            log.error("Esikatselu-PDF:n muodostus epäonnistui: {}", request.toString(), e);
            throw e;
        }
    }

    @POST
    @Path("letterbatch/email")
    @Consumes("application/json")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_SEND_LETTER_EMAIL)
    public Response dummyBatchEmail(PreviewRequest request) throws IOException, DocumentException {
        try {
            final Template template = templateService.findByIdAndState(request.getTemplateId(), ContentStructureType.letter, request.getTemplateState());
            String email = previewDataService.getEmailPreview(template, "", request.getLetterContent());
            return Response.ok(email).header("Content-Disposition", "attachment; filename=\"preview.eml\"").build();
        } catch (Exception e) {
            log.error("Esikatselu-emailin muodostus epäonnistui: {}", request.toString(), e);
            throw e;
        }
    }

}
