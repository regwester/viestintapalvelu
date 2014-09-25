package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

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
    @Path("/{templateName}/{languageCode}/{type}/{applicationPeriod}/getTemplateContent")
    public TemplateDTO getTemplateContent(@PathParam("templateName") String templateName, @PathParam("languageCode") String languageCode,
            @PathParam("type") String type, @PathParam("applicationPeriod") String applicationPeriod) throws IOException, DocumentException;

    @GET
    @Path("/{templateId}/getTemplateContent")
    @Produces("application/json")
    @Transactional
    public TemplateDTO getTemplateByID(@PathParam("templateId") String templateId);
}
