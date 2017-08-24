package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailBounces;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/")
public interface BounceResource {

    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{bouncesUrl}")
    @GET
    EmailBounces getBounces(@PathParam("bouncesUrl") String bouncesUrl);
}

