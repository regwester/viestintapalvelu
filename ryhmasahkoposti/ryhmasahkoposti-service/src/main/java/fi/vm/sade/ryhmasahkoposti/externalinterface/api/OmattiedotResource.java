package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.annotate.JsonView;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.JsonViews;

/**
 * Rajapinta käyttäjän omiin tietoihin
 * 
 * @author vehei1
 *
 */
@Path("omattiedot")
public interface OmattiedotResource {
    
    /**
     * Hakee käyttäjän omat tiedot
     * 
     * @return Henkilön tiedot
     */
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(JsonViews.Basic.class)
    @GET
    public Henkilo currentHenkiloTiedot();
}
