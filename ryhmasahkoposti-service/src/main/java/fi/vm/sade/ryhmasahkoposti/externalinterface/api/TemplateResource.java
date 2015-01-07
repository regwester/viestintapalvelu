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
package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Component;

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
    @Produces("application/json")
    @Path("/{templateName}/{languageCode}/{type}/{applicationPeriod}/getTemplateContent")
    public TemplateDTO getTemplateContent(@PathParam("templateName") String templateName, @PathParam("languageCode") String languageCode,
            @PathParam("type") String type, @PathParam("applicationPeriod") String applicationPeriod) throws IOException, DocumentException;

    @GET
    @Produces("application/json")
    @Path("/{templateName}/{languageCode}/{type}/getTemplateContent")
    public TemplateDTO getTemplateContent(@PathParam("templateName") String templateName, @PathParam("languageCode") String languageCode,
            @PathParam("type") String type) throws IOException, DocumentException;

    @GET
    @Path("/{templateId}/{type}/getTemplateContent")
    @Produces("application/json")
    public TemplateDTO getTemplateByID(@PathParam("templateId") String templateId, @PathParam("type") String type);
}
