package fi.vm.sade.viestintapalvelu.externalinterface.api;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonView;
import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.JsonViews;
import org.springframework.stereotype.Component;

/**
 * Rajapinta henkiloiden hakemiseen
 * 
 * @author jahyn1
 *
 */
@Component
@Path("henkilo")
public interface HenkiloResource {
    
    /**
     * Hakee käyttäjän tiedot oid:n perusteella
     * 
     * @return Henkilön tiedot
     */
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(JsonViews.Basic.class)
    @GET
    @Path("/{oid}")
    public Henkilo getHenkiloByOid(@PathParam("oid") String oid);
}