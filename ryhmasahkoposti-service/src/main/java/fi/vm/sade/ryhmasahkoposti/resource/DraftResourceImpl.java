/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.ryhmasahkoposti.resource;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
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

@Component("DraftResourceImpl")
public class DraftResourceImpl extends GenericResourceImpl implements DraftResource {
    
    private final Logger logger = LoggerFactory.getLogger(DraftResourceImpl.class.getName());
    
    @Autowired
    private DraftService draftService;
    
    public Draft getDraft(@PathParam("draftId") Long draftId) {
        if(draftId == null) {
            throw new BadRequestException("DraftId is not defined");
        }
        String oid = getCurrentUserOid();
        Draft draft = draftService.getDraft(draftId, oid);
        if(draft == null) {
            throw new NotFoundException("Draft could not be found with id: " + draftId.toString());
        }
        return draft;
    }
    
    public List<Draft> getAllDrafts() {
        String oid = getCurrentUserOid();
        List<Draft> drafts = draftService.getAllDrafts(oid);
        if(drafts == null) {
            throw new InternalServerErrorException("Drafts could not be retrieved");
        }
        return drafts;
    }

    public String getCount() {
        String oid = getCurrentUserOid();
        Long count = draftService.getCount(oid);
        if(count == null) {
            throw new InternalServerErrorException("Count could not be retrieved");
        }
        return "{\"count\":" + count.toString() + "}";
    }

    public String deleteDraft(Long draftId) throws Exception {
        if(draftId == null) {
            throw new BadRequestException("DraftId is not defined");
        }
        String oid = getCurrentUserOid();
        draftService.deleteDraft(draftId, oid);
        return "{\"status\": \"success\"}";
    }
    
    public Long saveDraft(Draft draft) {
        if(draft == null) {
            throw new BadRequestException("Draft is not defined");
        }
        //Reset the time (might not be the best solution)
        draft.setCreateDate(new DateTime());
        //Set the user oid (again, overwriting things might not be the most elegant solution)
        draft.setUserOid(getCurrentUserOid());
        //clean the html
        draft.setBody(InputCleaner.cleanHtmlDocument(draft.getBody()));

        logger.debug("Draft: {}", draft.toString());
        return draftService.saveDraft(draft);
    }

    @Override
    public String updateDraft(Long id, Draft draft) throws Exception {
        if(id == null) {
            throw new BadRequestException("DraftId is not defined");
        }

        String oid = getCurrentUserOid();
        //clean the html
        draft.setBody(InputCleaner.cleanHtmlDocument(draft.getBody()));
        draftService.updateDraft(id, oid, draft);
        return "{\"status\": \"success\"}";
    }


}
