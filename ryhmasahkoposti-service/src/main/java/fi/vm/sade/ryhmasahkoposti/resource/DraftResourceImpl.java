package fi.vm.sade.ryhmasahkoposti.resource;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.Draft;
import fi.vm.sade.ryhmasahkoposti.api.resource.DraftResource;
import fi.vm.sade.ryhmasahkoposti.service.DraftService;

@Component
public class DraftResourceImpl implements DraftResource {
    
    private final Logger logger = LoggerFactory.getLogger(DraftResourceImpl.class.getName());
    
    @Autowired
    private DraftService draftService;
    
    public Draft getDraft(@PathParam("draftId") Long draftId) {
        if(draftId == null) {
            throwError400("DraftId is not defined");
        }
        String oid = getCurrentUserOid();
        Draft draft = draftService.getDraft(draftId, oid);
        if(draft == null) {
            throwError404("Draft could not be found with id: " + draftId.toString());
        }
        return draft;
    }
    
    public List<Draft> getAllDrafts() {
        String oid = getCurrentUserOid();
        List<Draft> drafts = draftService.getAllDrafts(oid);
        if(drafts == null) {
            throwError500("Drafts could not be retrieved");
        }
        return drafts;
    }

    public String getCount() {
        String oid = getCurrentUserOid();
        Long count = draftService.getCount(oid);
        if(count == null) {
            throwError500("Count could not be retrieved");
        }
        String response = "{\"count\":" + count.toString() + "}";
        return response;
    }

    public Draft deleteDraft(Long draftId) {
        if(draftId == null) {
            throwError400("DraftId is not defined");
        }
        String oid = getCurrentUserOid();
        return draftService.deleteDraft(draftId, oid);
    }
    
    public String saveDraft(Draft draft) { //TODO: validate that organization oid in draft matches user oid, or better yet, simply ignore the given oid
        if(draft == null) {
            throwError400("Draft is not defined");
        }
        //Reset the time (might not be the best solution)
        draft.setCreateDate(new DateTime());
        //Set the user oid (again, overwriting things might not be the most elegant solution)
        //TODO: fix inconsistensy with the naming
        draft.setOrganizationOid(getCurrentUserOid());

        logger.debug("Draft: ", draft.toString());
        return draftService.saveDraft(draft);
    }
    
    private void throwError404(String msg) {
        throwError(Response.Status.NOT_FOUND, msg);
    }
    
    private void throwError400(String msg) {
        throwError(Response.Status.BAD_REQUEST, msg);
    }
    
    private void throwError500(String msg) {
        throwError(Response.Status.INTERNAL_SERVER_ERROR, msg);
    }
    
    private void throwError(Response.Status status, String msg) {
        throw new WebApplicationException(Response.status(status)
                .type(MediaType.TEXT_PLAIN).entity(msg).build());
    }

    /**
     * Fetches user oid from SecurityContext
     *
     * @return oid of currently authenticated user
     * @throws NullPointerException
     *             if user oid is not available from security context
     */
    protected String getCurrentUserOid() throws NullPointerException { //Should this be placed in a static util function?
        String oid = SecurityContextHolder.getContext().getAuthentication().getName();
        if (oid == null) {
            throw new NullPointerException("No user name available from SecurityContext!");
        }
        return oid;
    }
}
