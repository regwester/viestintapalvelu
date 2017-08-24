package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import com.fasterxml.jackson.annotation.JsonView;
import fi.vm.sade.authentication.model.JsonViews;
import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
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
    @JsonView(JsonViews.Basic.class)
    @Path("/{oid}/organisaatiohenkilo")
    @GET
    List<OrganisaatioHenkilo> getOrganisaatioHenkiloTiedot(@PathParam("oid") String oid);

}
