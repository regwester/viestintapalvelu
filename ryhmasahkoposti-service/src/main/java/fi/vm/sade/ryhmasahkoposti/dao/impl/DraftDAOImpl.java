package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.DraftDAO;
import fi.vm.sade.ryhmasahkoposti.model.DraftModel;

@Repository
public class DraftDAOImpl extends AbstractJpaDAOImpl<DraftModel, Long> implements DraftDAO {
    
    @Override
    public List<DraftModel> getAllDrafts() {
        return findAll();
    }

    @Override
    public DraftModel getDraft(Long id) {
        List<DraftModel> draft = findBy("id", id);
        if(draft.size() != 0){
            return draft.get(0);
        }
        return null;
    }

    @Override
    public Long getCount() {
        Number number = (Number) getEntityManager()
                .createNativeQuery("SELECT count(1) FROM luonnos").getSingleResult();
        return number.longValue();
    }

    @Override
    public void saveDraft(DraftModel draft) {
        insert(draft);
    }

    @Override
    public DraftModel deleteDraft(Long id) {
        DraftModel draft = getDraft(id);
        remove(draft);
        return draft;
    }
}
