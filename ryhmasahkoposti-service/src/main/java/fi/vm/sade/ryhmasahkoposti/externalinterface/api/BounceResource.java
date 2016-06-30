package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import com.fasterxml.jackson.annotation.JsonView;
import fi.vm.sade.authentication.model.JsonViews;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailBounces;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/")
public interface BounceResource {

    @JsonView(JsonViews.Basic.class)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{bouncesUrl}")
    @GET
    EmailBounces getBounces(@PathParam("bouncesUrl") String bouncesUrl);
}

