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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageAttachmentDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageAttachment;

@Repository
public class ReportedMessageAttachmentDAOImpl extends AbstractJpaDAOImpl<ReportedMessageAttachment, Long> implements ReportedMessageAttachmentDAO {

    @Override
    public List<ReportedMessageAttachment> findReportedMessageAttachments(Long viestinID) {
        EntityManager em = getEntityManager();

        String findViestinLiitteet = "SELECT a FROM ReportedMessageAttachment a JOIN a.reportedMessage " + "WHERE a.sendingStarted = null";
        TypedQuery<ReportedMessageAttachment> query = em.createQuery(findViestinLiitteet, ReportedMessageAttachment.class);

        return query.getResultList();
    }

    protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }
}
