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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import fi.vm.sade.viestintapalvelu.dao.DAOUtil;
import org.hibernate.internal.util.StringHelper;
import org.springframework.stereotype.Repository;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.template.BooleanTemplate;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedRecipientQueryDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import fi.vm.sade.ryhmasahkoposti.model.QReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.QReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.viestintapalvelu.common.util.CollectionHelper;

import static com.mysema.query.types.expr.BooleanExpression.anyOf;

@Repository
public class ReportedMessageDAOImpl extends AbstractJpaDAOImpl<ReportedMessage, Long> implements ReportedMessageDAO {

    public static final int MAX_CHUNK_SIZE_FOR_IN_EXPRESSION = 1000;
    private QReportedMessage reportedMessage = QReportedMessage.reportedMessage;
    private QReportedRecipient reportedRecipient = QReportedRecipient.reportedRecipient;

    @Override
    public List<ReportedMessage> findByOrganizationOids(List<String> organizationOids, PagingAndSortingDTO pagingAndSorting) {
        if (organizationOids != null && organizationOids.isEmpty()) {
            return new ArrayList<>();
        }

        JPAQuery findAllReportedMessagesQuery = from(reportedMessage).orderBy(orderBy(pagingAndSorting));
        if (organizationOids != null) {
            findAllReportedMessagesQuery = findAllReportedMessagesQuery.where(anyOf(splittedInExpression(organizationOids,
                    reportedMessage.senderOrganizationOid)));
        }

        if (pagingAndSorting.getNumberOfRows() != 0) {
            findAllReportedMessagesQuery.limit(pagingAndSorting.getNumberOfRows()).offset(pagingAndSorting.getFromIndex());
        }

        return findAllReportedMessagesQuery.list(reportedMessage);
    }

    @Override
    public List<ReportedMessage> findBySenderOid(String senderOid, PagingAndSortingDTO pagingAndSortingDTO) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(reportedMessage).where(reportedMessage.senderOid.eq(senderOid)).orderBy(orderBy(pagingAndSortingDTO)).list(reportedMessage);
    }

    @Override
    public List<ReportedMessage> findBySenderOidAndProcess(String senderOid, String process, PagingAndSortingDTO pagingAndSorting) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(reportedMessage).where(reportedMessage.senderOid.eq(senderOid).and(reportedMessage.process.equalsIgnoreCase(process)))
                .orderBy(orderBy(pagingAndSorting)).list(reportedMessage);
    }

    @Override
    public List<ReportedMessage> findBySearchCriteria(ReportedMessageQueryDTO query, PagingAndSortingDTO pagingAndSorting) {
        ReportedRecipientQueryDTO reportedRecipientQuery = query.getReportedRecipientQueryDTO();

        BooleanBuilder whereExpression = whereExpressionForSearchCriteria(query, reportedRecipientQuery);
        OrderSpecifier<?> orderBy = orderBy(pagingAndSorting);

        JPAQuery findBySearchCriteria = from(reportedMessage).distinct().leftJoin(reportedMessage.reportedRecipients, reportedRecipient).where(whereExpression)
                .orderBy(orderBy).limit(pagingAndSorting.getNumberOfRows()).offset(pagingAndSorting.getFromIndex());

        return findBySearchCriteria.list(reportedMessage);
    }

    @Override
    public Long findNumberOfReportedMessages(List<String> organizationOids) {
        if (organizationOids != null && organizationOids.isEmpty()) {
            return 0L;
        }

        EntityManager em = getEntityManager();

        Map<String, Object> params = new HashMap<>();
        String findNumberOfReportedMessages = "SELECT COUNT(*) FROM ReportedMessage a ";
        if (organizationOids != null) {
            findNumberOfReportedMessages += " WHERE " + DAOUtil.splittedInExpression(organizationOids, "a.senderOrganizationOid", params, "_oids");
        }
        TypedQuery<Long> query = em.createQuery(findNumberOfReportedMessages, Long.class);
        for (Map.Entry<String, Object> kv : params.entrySet()) {
            query.setParameter(kv.getKey(), kv.getValue());
        }

        return query.getSingleResult();
    }

    @Override
    public Long findNumberOfUserMessages(String userOid) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(reportedMessage).where(reportedMessage.senderOid.eq(userOid)).count();
    }

    @Override
    public Long findNumberOfReportedMessage(ReportedMessageQueryDTO query) {
        ReportedRecipientQueryDTO reportedRecipientQuery = query.getReportedRecipientQueryDTO();

        BooleanBuilder whereExpression = whereExpressionForSearchCriteria(query, reportedRecipientQuery);

        JPAQuery findBySearchCriteria = from(reportedMessage).distinct().leftJoin(reportedMessage.reportedRecipients, reportedRecipient).where(whereExpression);

        return findBySearchCriteria.count();
    }

    protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }

    protected OrderSpecifier<?> orderBy(PagingAndSortingDTO pagingAndSorting) {
        if (pagingAndSorting.getSortedBy() == null || pagingAndSorting.getSortedBy().isEmpty()) {
            return reportedMessage.sendingStarted.asc();
        }

        if (pagingAndSorting.getSortedBy().equalsIgnoreCase("sendingStarted")) {
            if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
                return reportedMessage.sendingStarted.asc();
            }

            return reportedMessage.sendingStarted.desc();
        }

        if (pagingAndSorting.getSortedBy().equalsIgnoreCase("senderName")) {
            if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
                return reportedMessage.senderName.asc();
            }

            return reportedMessage.sendingStarted.desc();
        }

        if (pagingAndSorting.getSortedBy().equalsIgnoreCase("process") || pagingAndSorting.getSortedBy().equalsIgnoreCase("subject")) {
            if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
                return reportedMessage.process.asc();
            }

            return reportedMessage.process.desc();
        }

        return reportedMessage.sendingStarted.asc();
    }

    protected BooleanBuilder whereExpressionForSearchCriteria(ReportedMessageQueryDTO query, ReportedRecipientQueryDTO reportedRecipientQuery) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (query.getOrganizationOids() != null) {
            if (query.getOrganizationOids().isEmpty()) {
                booleanBuilder.and(BooleanTemplate.TRUE.eq(BooleanTemplate.FALSE));
            } else {
                booleanBuilder.andAnyOf(splittedInExpression(query.getOrganizationOids(), reportedMessage.senderOrganizationOid));
            }
        } else if (query.getOrganizationOid() != null) {
            booleanBuilder.and(reportedMessage.senderOrganizationOid.in(query.getOrganizationOid()));
        }

        if (reportedRecipientQuery.getRecipientOid() != null) {
            booleanBuilder.and(reportedRecipient.recipientOid.eq(reportedRecipientQuery.getRecipientOid()));
        }

        if (reportedRecipientQuery.getRecipientEmail() != null) {
            booleanBuilder.and(reportedRecipient.recipientEmail.eq(reportedRecipientQuery.getRecipientEmail()));
        }

        if (reportedRecipientQuery.getRecipientSocialSecurityID() != null) {
            booleanBuilder.and(reportedRecipient.socialSecurityID.eq(reportedRecipientQuery.getRecipientSocialSecurityID()));
        }

        if (query.getSearchArgument() != null && !query.getSearchArgument().isEmpty()) {
            booleanBuilder.andAnyOf(reportedRecipient.searchName.containsIgnoreCase(reportedRecipientQuery.getRecipientName()),
                    reportedMessage.process.containsIgnoreCase(query.getSearchArgument()),
                    reportedMessage.subject.containsIgnoreCase(query.getSearchArgument()),
                    reportedMessage.message.containsIgnoreCase(query.getSearchArgument()));
        }

        return booleanBuilder;
    }

    private BooleanExpression[] splittedInExpression(List<String> values, final StringPath column) {
        List<List<String>> oidChunks = CollectionHelper.split(values, MAX_CHUNK_SIZE_FOR_IN_EXPRESSION);
        Collection<BooleanExpression> inExcepssionsCollection = Collections2.transform(oidChunks, new Function<List<String>, BooleanExpression>() {
            public BooleanExpression apply(@Nullable List<String> oidsChunk) {
                return column.in(oidsChunk);
            }
        });
        return inExcepssionsCollection.toArray(new BooleanExpression[inExcepssionsCollection.size()]);
    }

}
