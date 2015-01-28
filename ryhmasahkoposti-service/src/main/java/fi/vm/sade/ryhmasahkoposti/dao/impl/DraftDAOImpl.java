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
package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.DraftDAO;
import fi.vm.sade.ryhmasahkoposti.model.DraftModel;
import fi.vm.sade.ryhmasahkoposti.model.QDraftModel;

@Repository
public class DraftDAOImpl extends AbstractJpaDAOImpl<DraftModel, Long> implements DraftDAO {

    private QDraftModel draftModel = QDraftModel.draftModel;

    @Override
    public List<DraftModel> getAllDrafts(String oid) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(draftModel).where(draftModel.userOid.eq(oid)).list(draftModel);
    }

    @Override
    public DraftModel getDraft(Long id, String oid) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(draftModel).where(draftModel.id.eq(id), draftModel.userOid.eq(oid)).uniqueResult(draftModel);
    }

    @Override
    public Long getCount(String oid) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(draftModel).where(draftModel.userOid.eq(oid)).count();
    }

    @Override
    public DraftModel saveDraft(DraftModel draft) {
        return insert(draft);
    }

    @Override
    public void deleteDraft(Long id, String oid) {
        JPADeleteClause clause = new JPADeleteClause(getEntityManager(), draftModel);
        clause.where(draftModel.userOid.eq(oid), draftModel.id.eq(id)).execute();
    }

    @Override
    public void updateDraft(Long id, String oid, DraftModel draft) {
        DraftModel draftModel = (DraftModel) getEntityManager().find(DraftModel.class, id);
        if (draftModel.getUserOid().equals(oid)) {
            draftModel.setBody(draft.getSender());
            draftModel.setBody(draft.getSubject());
            draftModel.setReplyTo(draft.getReplyTo());
            draftModel.setBody(draft.getBody());
            draftModel.setAttachments(draft.getAttachments());
        }
    }
}
