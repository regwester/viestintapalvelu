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
package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.Draft;
import fi.vm.sade.ryhmasahkoposti.converter.DraftConverter;
import fi.vm.sade.ryhmasahkoposti.dao.DraftDAO;
import fi.vm.sade.ryhmasahkoposti.model.DraftModel;
import fi.vm.sade.ryhmasahkoposti.service.DraftService;
import fi.vm.sade.viestintapalvelu.common.exception.PersistenceException;

@Service
public class DraftServiceImpl implements DraftService {

    final Logger log = LoggerFactory.getLogger(DraftServiceImpl.class);
    
    @Autowired
    private DraftDAO draftDao;
    
    @Autowired
    private DraftConverter draftConverter;
    
    @Override
    @Transactional
    public Draft getDraft(Long id, String oid) {
        DraftModel draftModel = draftDao.getDraft(id, oid);
        return draftConverter.convert(draftModel);
    }

    @Override
    public List<Draft> getAllDrafts(String oid) {
        List<DraftModel> draftModels = draftDao.getAllDrafts(oid);
        List<Draft> drafts = new ArrayList<Draft>();
        for(DraftModel draftModel : draftModels) {
            drafts.add(draftConverter.convert(draftModel));
        }
        return drafts;
    }

    @Override
    public Long getCount(String oid) {
        return draftDao.getCount(oid);
    }

    @Override
    @Transactional
    public void deleteDraft(Long id, String oid) throws Exception {
        draftDao.deleteDraft(id, oid);
    }

    @Override
    @Transactional
    public Long saveDraft(Draft draft) {
        try {
            DraftModel draftModel = draftConverter.convert(draft);
            draftModel = draftDao.saveDraft(draftModel);
            return draftModel.getId();
        } catch(Exception e) {
            log.error("JPA Exception: {}", e);
            throw new PersistenceException("error.msg.savingDraft", e);
        }

    }

    @Override
    @Transactional
    public void updateDraft(Long id, String oid, Draft draft) {
        try {
            DraftModel draftModel = draftConverter.convert(draft);
            draftDao.updateDraft(id, oid, draftModel);
        } catch(Exception e) {
            log.error("JPA Exception: {}", e);
            throw new PersistenceException("error.msg.updatingDraft", e);
        }

    }

}
