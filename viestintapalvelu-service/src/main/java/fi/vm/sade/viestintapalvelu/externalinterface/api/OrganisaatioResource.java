package fi.vm.sade.viestintapalvelu.externalinterface.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;

@Path("/organisaatio")
public interface OrganisaatioResource {
    @GET
    @Path("{oid}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public OrganisaatioRDTO getOrganisaatioByOID(@PathParam("oid") String oid);
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{oid}/parentoids")
    public String parentoids(@PathParam("oid") String oid) throws Exception;

}
