package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.dom4j.DocumentException;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.dto.TemplateDTO;

/**
 * Interface to template service
 * 	
 * @author ovmol1
 *
 */
@Path("/template")
public interface TemplateResource {

    @GET
    @Transactional
    @Produces("application/json")
    @Path("/{templateName}/{languageCode}/{type}/getTemplateContent")
    public TemplateDTO getTemplateContent(@PathParam("templateName") String templateName, 
	    @PathParam("languageCode") String languageCode, @PathParam("type") String type) 
	    throws IOException, DocumentException;
}
