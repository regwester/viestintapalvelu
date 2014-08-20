package fi.vm.sade.ryhmasahkoposti.resource;

import java.util.List;

import javax.ws.rs.PathParam;

import fi.vm.sade.ryhmasahkoposti.common.util.InputCleaner;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.Draft;
import fi.vm.sade.ryhmasahkoposti.api.resource.DraftResource;
import fi.vm.sade.ryhmasahkoposti.service.DraftService;

@Component
public class DraftResourceImpl extends GenericResourceImpl implements DraftResource {
    
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
        return "{\"count\":" + count.toString() + "}";
    }

    public String deleteDraft(Long draftId) {
        if(draftId == null) {
            throwError400("DraftId is not defined");
        }
        try {
            String oid = getCurrentUserOid();
            draftService.deleteDraft(draftId, oid);
            return "{\"status\": \"success\"}";
        } catch (Exception e) {
            logger.error("Failed to delete the draft: ", e);
            return "{\"status\": \"failure\", \"reason\": \"" + e.toString() + "\" }";
        }
    }
    
    public String saveDraft(Draft draft) {
        if(draft == null) {
            throwError400("Draft is not defined");
        }
        //Reset the time (might not be the best solution)
        draft.setCreateDate(new DateTime());
        //Set the user oid (again, overwriting things might not be the most elegant solution)
        draft.setUserOid(getCurrentUserOid());
        //clean the html
        draft.setBody(InputCleaner.cleanHtml(draft.getBody()));

        logger.debug("Draft: ", draft.toString());
        return draftService.saveDraft(draft);
    }

    @Override
    public String updateDraft(Long id, Draft draft) {
        if(id == null) {
            throwError400("DraftId is not defined");
        }
        try {
            String oid = getCurrentUserOid();
            draftService.updateDraft(id, oid, draft);
            return "{\"status\": \"success\"}";
        } catch(Exception e ) {
            logger.error("Failed to update the draft: ", e);
            return "{\"status\": \"failure\"}"; //TODO: throw some unique webapplication exception?
        }
    }


}
