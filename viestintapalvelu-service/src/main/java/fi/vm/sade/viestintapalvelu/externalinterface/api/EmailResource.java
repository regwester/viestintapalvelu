package fi.vm.sade.viestintapalvelu.externalinterface.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonView;

import fi.vm.sade.authentication.model.JsonViews;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;

@Component
@Path("email")
public interface EmailResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(JsonViews.Basic.class)
    @Consumes("application/json")
    @Path("ok")
    public Response ok();
    
    
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
    @Path("")
    public Response sendEmail(EmailData emailData);
}
