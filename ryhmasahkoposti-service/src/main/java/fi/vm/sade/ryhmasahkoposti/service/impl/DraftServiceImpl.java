package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.List;

import fi.vm.sade.ryhmasahkoposti.exception.PersistenceException;
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
