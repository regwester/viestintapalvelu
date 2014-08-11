package fi.vm.sade.ryhmasahkoposti.api.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
     * Hakee luonnoksen id:n perusteella.
     * 
     * @param request Http pyyntö
     * @param response Http vastaus
     * @return Luonnoksen tiedot
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/{draftId}")
    @ApiOperation(value = "Palauttaa halutun luonnoksen tiedot", response = Draft.class)
    public Draft getDraft(@PathParam(value = "draftId") Long id);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Draft> getAllDrafts();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/count")
    public String getCount();

    @DELETE
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/{draftId}")
    public Draft deleteDraft(@PathParam(value = "draftId") Long id);
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public String saveDraft(Draft draft);

}
