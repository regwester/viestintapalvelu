package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import com.fasterxml.jackson.annotation.JsonView;
import fi.vm.sade.authentication.model.JsonViews;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailBounces;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Component
@Path("/")
public interface BounceResource {

    @JsonView(JsonViews.Basic.class)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{bouncesUrl}")
    @GET
    EmailBounces getBounces(@PathParam("bouncesUrl") String bouncesUrl);
}

