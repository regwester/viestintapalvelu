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

package fi.vm.sade.ajastuspalvelu.service.external.api;

import java.io.IOException;

import javax.ws.rs.*;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateDTO;

/**
 * Interface to template service
 * 
 * @author ovmol1
 *
 */
@Component
@Path("/template")
public interface TemplateResource {

    @GET
    @Transactional
    @Produces("application/json")
    @Path("/getByName")
    TemplateDTO getTemplate(@QueryParam("templateName") String templateName, @QueryParam("languageCode") String languageCode,
                                          @QueryParam("type") String type, @QueryParam("applicationPeriod") String applicationPeriod,
                                          @QueryParam("content") String content) throws IOException, DocumentException;

    @GET
    @Path("/{templateId}/getTemplateContent")
    @Produces("application/json")
    @Transactional
    TemplateDTO getTemplateByID(@PathParam("templateId") String templateId);
}
