package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonView;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.JsonViews;
import fi.vm.sade.authentication.model.OrganisaatioHenkilo;

/**
 * Rajapinta käyttäjän omiin tietoihin
 * 
 * @author vehei1
 *
 */
@Component
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
    
    /**
     * Hakee käyttäjän organisaatiotiedot
     * 
     * @return Lista henkilön organisaatiotietoja
     */
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(JsonViews.Basic.class)
    @Path("/organisaatiohenkilo")
    @GET
    public List<OrganisaatioHenkilo> currentHenkiloOrganisaatioHenkiloTiedot();
}
