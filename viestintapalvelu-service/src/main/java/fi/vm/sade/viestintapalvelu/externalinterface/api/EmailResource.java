package fi.vm.sade.viestintapalvelu.externalinterface.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonView;

import fi.vm.sade.authentication.model.JsonViews;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;

@Component
@Path("/")
public interface EmailResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(JsonViews.Basic.class)
    @Consumes("application/json")
    @Path("ok")
    Response ok();
    
    
    /**
     * Lähettää ryhmäsähköpostin vastaanottajille ilman alaviitettä
     *
     * @param emailData Lähetettävän ryhmäsähköpostin tiedot
     * @return Lähetettävän ryhmäsähköpostiviestin tunnus
     */
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(JsonViews.Basic.class)
    @Consumes("application/json")
    Response sendEmail(EmailData emailData);

    @POST
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("preview")
    Response getPreview(EmailData emailData) throws Exception;
}
