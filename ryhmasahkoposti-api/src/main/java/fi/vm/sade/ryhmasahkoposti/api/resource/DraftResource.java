package fi.vm.sade.ryhmasahkoposti.api.resource;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.security.access.prepost.PreAuthorize;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import fi.vm.sade.ryhmasahkoposti.api.constants.SecurityConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.Draft;
import org.springframework.stereotype.Component;

@Component
@Path("drafts")
@PreAuthorize(SecurityConstants.USER_IS_AUTHENTICATED)
@Api(value = "/drafts", description = "S&auml;hk&oumlpostien luonnosten tallentaminen")
public interface DraftResource {

    /**
     * Hakee käyttäjän tallentaman luonnoksen annetun tunnisteen perusteella.
     *
     * @param id Luonnoksen tunniste
     * @return Tunnistetta vastaavan luonnoksen
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/{draftId}")
    @ApiOperation(value = "Palauttaa halutun luonnoksen tiedot", response = Draft.class)
    public Draft getDraft(@PathParam(value = "draftId") Long id);

    /**
     * Hakee kaikki käyttäjän tallentamat luonnokset.
     *
     * @return Listan luonnoksista
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Draft> getAllDrafts();

    /**
     * Hakee käyttäjän tallentamien luonnoksien lukumäärän.
     *
     * @return Luonnoksen tiedot
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/count")
    public String getCount();

    /**
     * Poistaa käyttäjän tallentaman luonnoksen annetun tunnisteen perusteella.
     *
     * @param id Luonnoksen tunniste
     * @return Tiedon poistamisen onnistumisesta
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/{draftId}")
    public String deleteDraft(@PathParam(value = "draftId") Long id) throws Exception;

    /**
     * Tallentaa annetun luonnoksen.
     *
     * @param draft Luonnos objekti
     * @return Tiedon tallentamisen onnistumisesta
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public String saveDraft(Draft draft);

    /**
     * Muokkaa tallennettua luonnosta annetun tunnisteen ja luonnos objektin perusteella.
     *
     * @param id Luonnoksen tunniste
     * @param draft Luonnos objekti
     * @return Tiedon muokkauksen onnistumisesta.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    @Path("/{draftId}")
    public String updateDraft(@PathParam(value="draftId") Long id, Draft draft) throws Exception;
}
