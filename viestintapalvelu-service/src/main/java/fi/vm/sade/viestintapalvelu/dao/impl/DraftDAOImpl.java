/*
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
 */
package fi.vm.sade.viestintapalvelu.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.DraftDAO;
import fi.vm.sade.viestintapalvelu.model.Draft;
import fi.vm.sade.viestintapalvelu.model.QDraft;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DraftDAOImpl extends AbstractJpaDAOImpl<Draft, Long> implements DraftDAO {

    private static final Logger log = LoggerFactory.getLogger(DraftDAOImpl.class);

    public Draft findDraftByNameOrgTag(String templateName, String templateLanguage, String organizationOid, String applicationPeriod, String fetchTarget,
            String tag) {
        EntityManager em = getEntityManager();

        boolean hasApplicationPeriod = !StringUtils.isBlank(applicationPeriod);
        boolean hasFetchTarget = !StringUtils.isBlank(fetchTarget);
        boolean hasTag = !StringUtils.isBlank(tag);

        StringBuilder draftSelect = new StringBuilder("SELECT a FROM Draft a WHERE " + "a.templateName = :templateName AND " + "a.templateLanguage = :templateLanguage AND a.organizationOid = :organizationOid");

        if (hasApplicationPeriod)
            draftSelect.append(" AND a.applicationPeriod = :applicationPeriod");
        if (hasFetchTarget)
            draftSelect.append(" AND a.fetchTarget = :fetchTarget");
        else 
            draftSelect.append(" AND a.fetchTarget IS NULL");
        if (hasTag)
            draftSelect.append(" AND a.tag = :tag");
        
        draftSelect.append(" ORDER BY a.timestamp DESC");

        TypedQuery<Draft> query = em.createQuery(draftSelect.toString(), Draft.class);
        query.setParameter("templateName", templateName);
        query.setParameter("templateLanguage", templateLanguage);
        query.setParameter("organizationOid", organizationOid);
        if (hasApplicationPeriod)
            query.setParameter("applicationPeriod", applicationPeriod);
        if (hasFetchTarget)
            query.setParameter("fetchTarget", fetchTarget);
        if (hasTag)
            query.setParameter("tag", tag);
        query.setFirstResult(0); // LIMIT 1
        query.setMaxResults(1); //

        Draft draft = new Draft();
        try {
            draft = query.getSingleResult();
        } catch (Exception e) {
            draft = null;
        }

        return draft;
    }

    public Draft findDraftByNameOrgTag2(String templateName, String templateLanguage, String organizationOid, String applicationPeriod, String fetchTarget,
            String tag) {
        QDraft draft = QDraft.draft;

        BooleanExpression whereExpression = draft.templateName.eq(templateName).and(
                draft.templateLanguage.eq(templateLanguage).and(draft.organizationOid.eq(organizationOid)));

        if (!"".equals(applicationPeriod)) {
            whereExpression = whereExpression.and(draft.applicationPeriod.eq(applicationPeriod));
        }
        if (!"".equals(fetchTarget)) {
            whereExpression = whereExpression.and(draft.fetchTarget.eq(fetchTarget));
        }
        if (!"".equals(tag)) {
            whereExpression = whereExpression.and(draft.tag.eq(tag));
        }

        JPAQuery findDraf = from(draft).where(whereExpression).orderBy(QDraft.draft.timestamp.desc()).limit(1);

        return findDraf.singleResult(draft);
    }

    @Override
    public List<Draft> findByOrgOidsAndApplicationPeriod(List<String> oids, String applicationPeriod) {
        final String querySql = "select a FROM Draft a where a.applicationPeriod = :applicationPeriod and a.organizationOid in :oids";
        TypedQuery<Draft> query = getEntityManager().createQuery(querySql, Draft.class);
        query.setParameter("applicationPeriod", applicationPeriod);
        query.setParameter("oids", oids);
        return query.getResultList();
    }

    @Override
    public List<Draft> findDraftsByTags(List<String> tags) {
        try {
            final String findDrafts = "SELECT a FROM Draft a WHERE a.tag in :tags";
            TypedQuery<Draft> query = getEntityManager().createQuery(findDrafts, Draft.class);
            query.setParameter("tags", tags);
            return query.getResultList();

        } catch (Exception e) {
            log.error("error finding drafts by tags", e);
            return new ArrayList<>();
        }
    }


    protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }

}
