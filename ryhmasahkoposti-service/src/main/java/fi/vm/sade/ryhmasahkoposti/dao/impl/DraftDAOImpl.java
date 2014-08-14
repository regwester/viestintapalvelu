package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import com.mysema.query.jpa.impl.JPAQuery;
import fi.vm.sade.ryhmasahkoposti.model.QDraftModel;
import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.DraftDAO;
import fi.vm.sade.ryhmasahkoposti.model.DraftModel;

@Repository
public class DraftDAOImpl extends AbstractJpaDAOImpl<DraftModel, Long> implements DraftDAO {

    private QDraftModel draftModel = QDraftModel.draftModel;

    @Override
    public List<DraftModel> getAllDrafts(String oid) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(draftModel)
            .where(draftModel.userOid.eq(oid))
            .list(draftModel);
    }

    @Override
    public DraftModel getDraft(Long id, String oid) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(draftModel)
            .where(draftModel.id.eq(id), draftModel.userOid.eq(oid))
            .uniqueResult(draftModel);
    }

    @Override
    public Long getCount(String oid) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(draftModel)
            .where(draftModel.userOid.eq(oid))
            .count();
    }

    @Override
    public void saveDraft(DraftModel draft) {
        insert(draft);
    }

    @Override
    public DraftModel deleteDraft(Long id, String oid) {
        DraftModel draft = getDraft(id, oid);
        remove(draft);
        return draft;
    }
}
