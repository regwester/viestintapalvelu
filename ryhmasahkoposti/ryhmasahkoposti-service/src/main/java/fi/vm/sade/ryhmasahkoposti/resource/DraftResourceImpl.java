package fi.vm.sade.ryhmasahkoposti.resource;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        Draft draft = draftService.getDraft(draftId);
        if(draft == null) {
            throwError404("Draft could not be found with id: " + draftId.toString());
        }
        return draft;
    }
    
    public List<Draft> getAllDrafts() {
        List<Draft> drafts = draftService.getAllDrafts();
        if(drafts == null) {
            throwError500("Drafts could not be retrieved");
        }
        return drafts;
    }
    
    public Draft deleteDraft(Long draftId) {
        if(draftId == null) {
            throwError400("DraftId is not defined");
        }
        return draftService.deleteDraft(draftId);
    }
    
    public String saveDraft(Draft draft) {
        if(draft == null) {
            throwError400("Draft is not defined");
        }
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
}
