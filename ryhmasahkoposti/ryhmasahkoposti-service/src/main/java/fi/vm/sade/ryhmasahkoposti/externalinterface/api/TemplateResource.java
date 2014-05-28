package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.dom4j.DocumentException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface to template service
 * 	
 * @author ovmol1
 *
 */
@Path("/template")
public interface TemplateResource {

    @POST
    @Transactional
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/getTemplateContent")
    public String getTemplateContent(String templateName, String languageCode, String type) 
	    throws IOException, DocumentException;
}
