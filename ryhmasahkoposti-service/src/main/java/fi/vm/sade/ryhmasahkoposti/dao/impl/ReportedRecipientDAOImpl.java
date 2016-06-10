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

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import fi.vm.sade.ryhmasahkoposti.api.constants.GroupEmailConstants;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedRecipientDAO;
import fi.vm.sade.ryhmasahkoposti.model.QReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

@Repository
public class ReportedRecipientDAOImpl extends AbstractJpaDAOImpl<ReportedRecipient, Long> implements ReportedRecipientDAO {

    private QReportedRecipient reportedRecipient = QReportedRecipient.reportedRecipient;

    @Override
    public List<ReportedRecipient> findByMessageId(Long messageID, PagingAndSortingDTO pagingAndSorting) {

        BooleanExpression whereExpression = reportedRecipient.reportedMessage.id.eq(messageID);
        OrderSpecifier<?> orderBy = orderBy(pagingAndSorting);

        JPAQuery findByMessageIdQuery = from(reportedRecipient).where(whereExpression).limit(pagingAndSorting.getNumberOfRows())
                .offset(pagingAndSorting.getFromIndex()).orderBy(orderBy);

        return findByMessageIdQuery.list(reportedRecipient);
    }

    @Override
    public List<ReportedRecipient> findByMessageIdAndSendingUnsuccessful(Long messageID, PagingAndSortingDTO pagingAndSorting) {
        return getByRecipientsStatus(messageID, pagingAndSorting, GroupEmailConstants.SENDING_FAILED);
    }

    @Override
    public List<ReportedRecipient> findByMessageIdAndSendingBounced(Long messageID, PagingAndSortingDTO pagingAndSorting) {
        return getByRecipientsStatus(messageID, pagingAndSorting, GroupEmailConstants.SENDING_BOUNCED);
    }

    private List<ReportedRecipient> getByRecipientsStatus(Long messageID, PagingAndSortingDTO pagingAndSorting, String status) {
        BooleanExpression whereExpression = reportedRecipient.reportedMessage.id.eq(messageID);
        whereExpression = whereExpression.and(reportedRecipient.sendingSuccessful.eq(status));
        OrderSpecifier<?> orderBy = orderBy(pagingAndSorting);
        JPAQuery findByMessageIdQuery = from(reportedRecipient).where(whereExpression).limit(pagingAndSorting.getNumberOfRows())
                .offset(pagingAndSorting.getFromIndex()).orderBy(orderBy);
        return findByMessageIdQuery.list(reportedRecipient);
    }

    @Override
    public ReportedRecipient findByRecipientID(Long recipientID) {
        return read(recipientID);
    }

    @Override
    public List<ReportedRecipient> findByLetterHash(String letterHash) {
        BooleanExpression whereExpression = reportedRecipient.letterHash.eq(letterHash);
        JPAQuery findByLetterHashQuery = from(reportedRecipient).where(whereExpression);
        return findByLetterHashQuery.list(reportedRecipient);
    }

    @Override
    public Date findMaxValueOfSendingEndedByMessageID(Long messageID) {
        EntityManager em = getEntityManager();

        String findMaxValueOfSendingEnded = "SELECT MAX(a.sendingEnded) FROM ReportedRecipient a " + "WHERE a.reportedMessage.id = :messageID";
        TypedQuery<Date> query = em.createQuery(findMaxValueOfSendingEnded, Date.class);
        query.setParameter("messageID", messageID);

        return query.getSingleResult();
    }

    @Override
    public Long findNumberOfRecipientsByMessageID(Long messageID) {
        EntityManager em = getEntityManager();

        String findNumberOfRecipients = "SELECT COUNT(*) FROM ReportedRecipient a " + "JOIN a.reportedMessage WHERE a.reportedMessage.id = :messageID";
        TypedQuery<Long> query = em.createQuery(findNumberOfRecipients, Long.class);
        query.setParameter("messageID", messageID);

        return query.getSingleResult();
    }

    @Override
    public Long findNumberOfRecipientsByMessageIDAndSendingStatus(Long messageID, String sendingStatus) {
        EntityManager em = getEntityManager();

        String findNumberOfRecipients = "SELECT COUNT(*) FROM ReportedRecipient a "
                + "JOIN a.reportedMessage WHERE a.reportedMessage.id = :messageID AND a.sendingSuccessful = :sendingSuccessful";
        TypedQuery<Long> query = em.createQuery(findNumberOfRecipients, Long.class);
        query.setParameter("messageID", messageID);
        query.setParameter("sendingSuccessful", sendingStatus);
        return query.getSingleResult();
    }

    @Override
    public List<ReportedRecipient> findUnhandled() {
        EntityManager em = getEntityManager();

        String findUnhandled = "SELECT a FROM ReportedRecipient a JOIN a.reportedMessage " + "WHERE a.sendingStarted = null";
        TypedQuery<ReportedRecipient> query = em.createQuery(findUnhandled, ReportedRecipient.class);

        return query.getResultList();
    }

    @Override
    public List<Long> findRecipientIdsWithIncompleteInformation() {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(reportedRecipient).where(reportedRecipient.detailsRetrieved.eq(false)).list(reportedRecipient.id);
    }

    protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }

    protected OrderSpecifier<?> orderBy(PagingAndSortingDTO pagingAndSorting) {
        if (pagingAndSorting.getSortedBy() == null || pagingAndSorting.getSortedBy().isEmpty()) {
            return reportedRecipient.searchName.asc();
        }

        if (pagingAndSorting.getSortedBy().equalsIgnoreCase("searchName")) {
            if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
                return reportedRecipient.searchName.asc();
            }

            return reportedRecipient.searchName.desc();
        }

        if (pagingAndSorting.getSortedBy().equalsIgnoreCase("recipientOid")) {
            if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
                return reportedRecipient.recipientOid.asc();
            }

            return reportedRecipient.recipientOid.desc();
        }

        if (pagingAndSorting.getSortedBy().equalsIgnoreCase("recipientEmail")) {
            if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
                return reportedRecipient.recipientEmail.asc();
            }

            return reportedRecipient.recipientEmail.desc();
        }

        return reportedRecipient.searchName.asc();
    }

}
