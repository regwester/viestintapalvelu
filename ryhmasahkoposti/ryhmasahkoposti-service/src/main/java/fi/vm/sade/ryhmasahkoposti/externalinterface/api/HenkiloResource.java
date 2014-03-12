package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.annotate.JsonView;
import org.springframework.security.access.prepost.PreAuthorize;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.JsonViews;

/**
 * Rajapinta henkilöpalveluun
 * 
 * @author vehei1
 *
 */
@Path("henkilo")
public interface HenkiloResource {
    /**
     * Hakee henkilön tiedot OID:lla
     * 
     * @param oid Henkilön OID-tunnus
     * @return Henkilön tiedot
     */
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(JsonViews.Basic.class)
    @PreAuthorize("hasAnyRole('ROLE_APP_HENKILONHALLINTA_READ', 'ROLE_APP_HENKILONHALLINTA_READ_UPDATE', 'ROLE_APP_HENKILONHALLINTA_CRUD')")
    @Path("{oid}")
    @GET
    public Henkilo findByOid(@PathParam("oid") String oid);
    
    /**
     * Palauttaa kirjaantuneen käyttäjän tiedot
     * 
     * @return Kirjaantuneen käyttäjän henkilötiedot
     */
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(JsonViews.Basic.class)
    @PreAuthorize("hasAnyRole('ROLE_APP_HENKILONHALLINTA_READ')")
    @Path("current")
    @GET
    public Henkilo fetchHenkiloCurrentUser();
}
