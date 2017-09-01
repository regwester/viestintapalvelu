package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import fi.vm.sade.dto.OrganisaatioHenkiloDto;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;

/**
 * Rajapinta käyttöoikeuspalvelun henkilötietoihin.
 */
@Component
@Path("henkilo")
public interface KayttooikeusHenkiloResource {

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{oid}/organisaatiohenkilo")
    @GET
    List<OrganisaatioHenkiloDto> getOrganisaatioHenkiloTiedot(@PathParam("oid") String oid);

}
