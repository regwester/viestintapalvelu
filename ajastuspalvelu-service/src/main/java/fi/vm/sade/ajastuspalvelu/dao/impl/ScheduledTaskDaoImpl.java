/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.ajastuspalvelu.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import fi.vm.sade.ajastuspalvelu.dao.ScheduledTaskCriteria;
import fi.vm.sade.ajastuspalvelu.dao.ScheduledTaskDao;
import fi.vm.sade.ajastuspalvelu.model.ScheduledTask;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskCriteriaDto;
import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;

/**
 * User: ratamaa
 * Date: 23.10.2014
 * Time: 13:38
 */
@Repository
public class ScheduledTaskDaoImpl extends AbstractJpaDAOImpl<ScheduledTask, Long>
        implements ScheduledTaskDao {

    @Override
    public List<ScheduledTask> find(ScheduledTaskCriteria criteria) {
        TypedQuery<ScheduledTask> q = getEntityManager().createQuery("select st from ScheduledTask st " +
                " where st.removed is null order by "
                + buildOrderBy("st", criteria), ScheduledTask.class);
        if (criteria.getIndex() != null) {
            q.setFirstResult(criteria.getIndex());
        }
        if (criteria.getMaxResultCount() != null) {
            q.setMaxResults(criteria.getMaxResultCount());
        }
        return q.getResultList();
    }

    @Override
    public int count(ScheduledTaskCriteriaDto criteria) {
        return getEntityManager().createQuery("select count(st.id) from ScheduledTask st where st.removed is null", Number.class)
                .getSingleResult().intValue();
    }

    private String buildOrderBy(String alias, ScheduledTaskCriteria criteria) {
        return buildColumn(alias, criteria.getOrderBy()) + " " + criteria.getOrderDirection().name();
    }

    private String buildColumn(String alias, ScheduledTaskCriteria.OrderBy orderBy) {
        switch (orderBy) {
            case CREATED_AT:
                return alias+".created";
            case SINGLE_RUNTIME:
                return alias+".runtimeForSingle";
            case TASK_NAME:
                return alias+".task.name";
            case APPLICATION_PERIOD:
                return alias+".hakuOid";
            default: throw new IllegalStateException("Unsupported OrderBy: " + orderBy);
        }
    }
}
