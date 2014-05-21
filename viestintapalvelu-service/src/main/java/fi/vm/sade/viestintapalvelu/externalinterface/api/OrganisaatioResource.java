package fi.vm.sade.viestintapalvelu.externalinterface.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/organisaatio")
public interface OrganisaatioResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{oid}/parentoids")
    public String parentoids(@PathParam("oid") String oid) throws Exception;

}
