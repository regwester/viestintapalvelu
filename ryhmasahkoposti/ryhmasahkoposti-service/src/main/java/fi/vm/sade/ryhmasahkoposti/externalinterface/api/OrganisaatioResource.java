package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import org.springframework.stereotype.Component;

@Component
@Path("/organisaatio")
public interface OrganisaatioResource {
    @GET
    @Path("{oid}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public OrganisaatioRDTO getOrganisaatioByOID(@PathParam("oid") String oid);
}
