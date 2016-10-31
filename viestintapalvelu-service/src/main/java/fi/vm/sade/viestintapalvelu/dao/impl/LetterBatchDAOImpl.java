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
package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import fi.vm.sade.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.dao.DAOUtil;
import fi.vm.sade.viestintapalvelu.dao.dto.LetterBatchCountDto;
import fi.vm.sade.viestintapalvelu.letter.LetterListItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.template.BooleanTemplate;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.dto.LetterBatchStatusDto;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterBatchReportDTO;
import fi.vm.sade.viestintapalvelu.dto.query.LetterReportQueryDTO;
import fi.vm.sade.viestintapalvelu.model.*;
import static com.mysema.query.types.expr.BooleanExpression.anyOf;

@Repository
public class LetterBatchDAOImpl extends AbstractJpaDAOImpl<LetterBatch, Long> implements LetterBatchDAO {
    private static final Logger LOG = LoggerFactory.getLogger(LetterBatchDAOImpl.class);

    public static final int MAX_CHUNK_SIZE_FOR_IN_EXPRESSION = 1000;

    public LetterBatch findLetterBatchByNameOrgTag(String templateName, String language, String organizationOid, Optional<String> tag,
            Optional<String> applicationPeriod) {
        EntityManager em = getEntityManager();

        String findTemplate = "SELECT a FROM LetterBatch a WHERE " + "a.templateName=:templateName AND " + "a.organizationOid=:organizationOid AND "
                + "a.language=:language  ";
        if (tag.isPresent()) {
            findTemplate += " AND a.tag LIKE :tag ";
        }
        if (applicationPeriod.isPresent()) {
            findTemplate += " AND a.applicationPeriod = :applicationPeriod ";
        }
        findTemplate += "ORDER BY a.timestamp DESC";

        TypedQuery<LetterBatch> query = getLetterBatchTypedQuery(templateName, language, organizationOid, em, findTemplate);
        if (tag.isPresent()) {
            query.setParameter("tag", tag.get());
        }
        if (applicationPeriod.isPresent()) {
            query.setParameter("applicationPeriod", applicationPeriod.get());
        }
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    private TypedQuery<LetterBatch> getLetterBatchTypedQuery(String templateName, String language, String organizationOid, EntityManager em, String findTemplate) {
        TypedQuery<LetterBatch> query = em.createQuery(findTemplate, LetterBatch.class);
        query.setParameter("templateName", templateName);
        query.setParameter("language", language);
        query.setParameter("organizationOid", organizationOid);
        query.setFirstResult(0); // LIMIT 1
        query.setMaxResults(1); //
        return query;
    }

    public LetterBatch findLetterBatchByNameOrg(String templateName, String language, String organizationOid) {
        EntityManager em = getEntityManager();

        String findTemplate = "SELECT a FROM LetterBatch a WHERE " + "a.templateName=:templateName AND " + "a.language=:language AND "
                + "a.organizationOid=:organizationOid " + "ORDER BY a.timestamp DESC";

        TypedQuery<LetterBatch> query = getLetterBatchTypedQuery(templateName, language, organizationOid, em, findTemplate);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<LetterBatch> findAll(PagingAndSortingDTO pagingAndSorting) {
        QLetterBatch letterBatch = QLetterBatch.letterBatch;

        OrderSpecifier<?> orderBy = orderBy(pagingAndSorting, null);
        JPAQuery findLetterBatches = from(letterBatch).orderBy(orderBy);

        return getLetterBatches(pagingAndSorting, letterBatch, findLetterBatches);
    }

    private List<LetterBatch> getLetterBatches(PagingAndSortingDTO pagingAndSorting, QLetterBatch letterBatch, JPAQuery findLetterBatches) {
        if (pagingAndSorting.getNumberOfRows() != 0) {
            findLetterBatches.limit(pagingAndSorting.getNumberOfRows()).offset(pagingAndSorting.getFromIndex());
        }
        return findLetterBatches.list(letterBatch);
    }

    @Override
    public String findTemplateNameForLetterBatch(long batchId) {
        EntityManager em = getEntityManager();
        TypedQuery<String> query = em.createQuery("SELECT a.templateName FROM LetterBatch a WHERE a.id = :value", String.class);
        query.setParameter("value", batchId);
        return query.getSingleResult();
    }

    @Override
    public List<LetterBatch> findLetterBatchesByOrganizationOid(List<String> organizationOIDs, PagingAndSortingDTO pagingAndSorting) {
        if (organizationOIDs.isEmpty()) {
            return new ArrayList<>();
        }

        QLetterBatch letterBatch = QLetterBatch.letterBatch;
        OrderSpecifier<?> orderBy = orderBy(pagingAndSorting, null);
        BooleanExpression whereExpression = anyOf(DAOUtil.splittedInExpression(organizationOIDs, letterBatch.organizationOid));
        JPAQuery findLetterBatches = from(letterBatch).where(whereExpression).orderBy(orderBy);

        return getLetterBatches(pagingAndSorting, letterBatch, findLetterBatches);
    }

    @Override
    public List<LetterBatchReportDTO> findLetterBatchesBySearchArgument(LetterReportQueryDTO query, PagingAndSortingDTO pagingAndSorting) {
        QLetterBatch letterBatch = QLetterBatch.letterBatch;
        QLetterReceivers receiver = QLetterReceivers.letterReceivers;
        QLetterReceiverAddress letterReceiverAddress = QLetterReceiverAddress.letterReceiverAddress;
        JPAQuery q = fromSearchTarget(query.getTarget(), letterBatch, receiver, letterReceiverAddress)
                .where(whereExpressionForSearchCriteria(query, letterBatch, receiver, letterReceiverAddress))
                .orderBy(orderBy(pagingAndSorting, letterReceiverAddress)).limit(pagingAndSorting.getNumberOfRows()).offset(pagingAndSorting.getFromIndex());
        if (query.getTarget() == LetterReportQueryDTO.SearchTarget.receiver) {
            return q.list(ConstructorExpression.create(LetterBatchReportDTO.class, letterBatch.id, letterBatch.templateId, letterBatch.templateName,
                    letterBatch.applicationPeriod, letterBatch.fetchTarget, letterBatch.tag, letterBatch.iposti, letterBatch.timestamp,
                    letterBatch.organizationOid, letterBatch.batchStatus, receiver.letterReceiverLetter.id,
                    letterReceiverAddress.lastName.concat(" ").concat(letterReceiverAddress.firstName)));
        }
        return q.list(ConstructorExpression.create(LetterBatchReportDTO.class, letterBatch.id, letterBatch.templateId, letterBatch.templateName,
                letterBatch.applicationPeriod, letterBatch.fetchTarget, letterBatch.tag, letterBatch.iposti, letterBatch.timestamp,
                letterBatch.organizationOid, letterBatch.batchStatus));
    }

    @Override
    public Long findNumberOfLetterBatches() {
        EntityManager em = getEntityManager();

        String findNumberOfLetterBatches = "SELECT COUNT(*) FROM LetterBatch";
        TypedQuery<Long> query = em.createQuery(findNumberOfLetterBatches, Long.class);
        return query.getSingleResult();
    }

    @Override
    public Long findNumberOfLetterBatches(List<String> oids) {
        if (oids.isEmpty()) {
            return 0L;
        }

        Map<String, Object> params = new HashMap<>();
        String findNumberOfLetterBatches = "SELECT COUNT(*) FROM LetterBatch a WHERE " + DAOUtil.splittedInExpression(oids, "a.organizationOid", params, "_oids");

        return DAOUtil.querySingleLong(getEntityManager(), params, findNumberOfLetterBatches);
    }

    @Override
    public Long findNumberOfLetterBatchesBySearchArgument(LetterReportQueryDTO letterReportQuery, Long maxCount) {
        QLetterBatch letterBatch = QLetterBatch.letterBatch;
        QLetterReceivers receiver = QLetterReceivers.letterReceivers;
        QLetterReceiverAddress letterReceiverAddress = QLetterReceiverAddress.letterReceiverAddress;
        JPAQuery q = fromSearchTarget(letterReportQuery.getTarget(), letterBatch, receiver, letterReceiverAddress).where(
                whereExpressionForSearchCriteria(letterReportQuery, letterBatch, receiver, letterReceiverAddress));
        if (maxCount != null) {
            // can not get count effectively (runtime of receiver join
            // explodes), just tell if we got more than maxCount
            return (long) q
                    .limit(maxCount + 1L)
                    .orderBy(letterBatch.timestamp.desc())
                    .list(letterReportQuery.getTarget() == LetterReportQueryDTO.SearchTarget.receiver ? new Expression<?>[] { receiver.letterReceiverLetter.id,
                            letterBatch.timestamp } : new Expression<?>[] { letterBatch.timestamp }).size();
        }
        return q.count();
    }

    protected JPAQuery fromSearchTarget(LetterReportQueryDTO.SearchTarget target, QLetterBatch letterBatch, QLetterReceivers receiver,
            QLetterReceiverAddress letterReceiverAddress) {
        switch (target) {
        case batch:
            return from(letterBatch).innerJoin(letterBatch.letterReceivers, receiver).leftJoin(receiver.letterReceiverAddress, letterReceiverAddress);
        case receiver:
            return from(letterBatch).distinct().leftJoin(letterBatch.letterReceivers, receiver).leftJoin(receiver.letterReceiverAddress, letterReceiverAddress);
        default:
            throw new IllegalArgumentException("Unknown SearchTarget" + target);
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public LetterBatchStatusDto getLetterBatchStatus(long letterBatchId) {
        List<LetterBatchStatusDto> results = (List<LetterBatchStatusDto>) getEntityManager().createNamedQuery("letterBatchStatus")
                .setParameter("batchId", letterBatchId).setMaxResults(1).getResultList();
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    @Override
    public List<Long> findUnprocessedLetterReceiverIdsByBatch(long batchId) {
        return getEntityManager()
                .createQuery(
                        "select lr.id from LetterReceivers lr" + " inner join lr.letterBatch batch with batch.id = :batchId"
                                + " inner join lr.letterReceiverLetter letter" + " where letter.letter = null" + " order by lr.id", Long.class)
                .setParameter("batchId", batchId).getResultList();
    }

    @Override
    public List<Long> findAllLetterReceiverIdsByBatch(long batchId) {
        return getEntityManager()
                .createQuery("select lr.id from LetterReceivers lr" + " inner join lr.letterBatch batch with batch.id = :batchId" + " order by lr.id",
                        Long.class).setParameter("batchId", batchId).getResultList();
    }

    @Override
    public List<Long> findUnfinishedLetterBatches() {
        return getEntityManager().createQuery(
                "SELECT lb.id FROM LetterBatch lb" + " WHERE lb.batchStatus != 'ready' AND lb.batchStatus != 'error'" + " ORDER BY lb.timestamp ASC",
                Long.class).getResultList();
    }

    @Override
    public List<LetterListItem> findLettersReadyForPublishByPersonOid(String personOid) {
        return getEntityManager().createQuery(
            "SELECT new fi.vm.sade.viestintapalvelu.letter.LetterListItem(lrl.id, lb.applicationPeriod, lb.templateName, lrl.contentType, lrl.timestamp)"
                + " FROM LetterBatch lb"
                + " INNER JOIN lb.letterReceivers lr WITH lr.oidPerson = :oidPerson"
                + " INNER JOIN lr.letterReceiverLetter lrl WITH lrl.readyForPublish = :readyForPublish"
                + " WHERE lb.tag = lb.applicationPeriod", LetterListItem.class)
                .setParameter("oidPerson", personOid).setParameter("readyForPublish", true).getResultList();
    }

    protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }

    protected OrderSpecifier<?> orderBy(PagingAndSortingDTO pagingAndSorting, QLetterReceiverAddress receiverAddress) {
        PathBuilder<LetterBatch> pb = new PathBuilder<>(LetterBatch.class, "letterBatch");

        if (pagingAndSorting.getSortedBy() != null && !pagingAndSorting.getSortedBy().isEmpty()) {
            if (receiverAddress != null && "receiverName".equals(pagingAndSorting.getSortedBy())) {
                return direction(receiverAddress.lastName.concat(" ").concat(receiverAddress.firstName), pagingAndSorting.getSortOrder());
            }
            return direction(pb.getString(pagingAndSorting.getSortedBy()), pagingAndSorting.getSortOrder());
        }
        return pb.getString("timestamp").asc();
    }

    protected OrderSpecifier<?> direction(ComparableExpressionBase<?> exp, String sortOrder) {
        if (sortOrder == null || sortOrder.isEmpty()) {
            return exp.asc();
        }
        if (sortOrder.equalsIgnoreCase("asc")) {
            return exp.asc();
        }
        return exp.desc();
    }

    protected BooleanBuilder whereExpressionForSearchCriteria(LetterReportQueryDTO query, final QLetterBatch letterBatch,
            final QLetterReceivers letterReceivers, final QLetterReceiverAddress letterReceiverAddress) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (query.getBeginDate() != null) {
            // gte missing from QueryDSL
            booleanBuilder.andAnyOf(letterBatch.timestamp.after(query.getBeginDate().toDateTimeAtStartOfDay().toDate()),
                    letterBatch.timestamp.eq(query.getBeginDate().toDateTimeAtStartOfDay().toDate()));
        }
        if (query.getEndDate() != null) {
            // as well as lte
            booleanBuilder.andAnyOf(letterBatch.timestamp.before(query.getEndDate().toDateTimeAtStartOfDay().toDate()),
                    letterBatch.timestamp.eq(query.getEndDate().toDateTimeAtStartOfDay().toDate()));
        }

        if (query.getOrganizationOids() != null) {
            if (query.getOrganizationOids().isEmpty()) {
                // no organisaatios should yield no results, thus:
                booleanBuilder.and(BooleanTemplate.TRUE.eq(BooleanTemplate.FALSE));
            } else {
                booleanBuilder.andAnyOf(DAOUtil.splittedInExpression(query.getOrganizationOids(), letterBatch.organizationOid));
            }
        }

        if (query.getApplicationPeriod() != null && !query.getApplicationPeriod().isEmpty()) {
            booleanBuilder.and(letterBatch.applicationPeriod.eq(query.getApplicationPeriod()));
        }

        int subQueryNum = 0;
        if (query.getLetterBatchSearchArgument() != null && !query.getLetterBatchSearchArgument().isEmpty()) {
            for (String word : ExpressionHelper.words(query.getLetterBatchSearchArgument())) {
                booleanBuilder.andAnyOf(anyOfLetterBatchRelatedConditions(letterBatch, word), letterBatchReplacementsContain(letterBatch, word, ++subQueryNum));
            }
        }
        if (query.getReceiverSearchArgument() != null && !query.getReceiverSearchArgument().isEmpty()) {
            for (String word : ExpressionHelper.words(query.getReceiverSearchArgument())) {
                booleanBuilder.andAnyOf(anyOfLetterBatchRelatedConditions(letterBatch, word),
                        ExpressionHelper.anyOfReceiverAddressFieldsContains(letterReceiverAddress, word),
                        letterBatchReplacementsContain(letterBatch, word, ++subQueryNum),
                        ExpressionHelper.receiverReplacementsContain(letterReceivers, word, ++subQueryNum));
            }
        }

        return booleanBuilder;
    }

    private BooleanExpression letterBatchReplacementsContain(QLetterBatch letterBatch, String word, int subQueryNumber) {
        QLetterBatch subQueryLetterBatch = new QLetterBatch("sqLetterBatch_" + subQueryNumber);
        QLetterReplacement replacement = new QLetterReplacement("sqLetterReplacement_" + subQueryNumber);
        return new JPASubQuery()
                .from(subQueryLetterBatch)
                .innerJoin(subQueryLetterBatch.letterReplacements, replacement)
                .where(subQueryLetterBatch.id.eq(letterBatch.id).andAnyOf(replacement.defaultValue.containsIgnoreCase(word),
                        replacement.jsonValue.containsIgnoreCase(word))).exists();
    }

    private BooleanExpression anyOfLetterBatchRelatedConditions(QLetterBatch letterBatch, String word) {
        return letterBatch.templateName.containsIgnoreCase(word).or(
                letterBatch.fetchTarget.containsIgnoreCase(word).or(letterBatch.applicationPeriod.containsIgnoreCase(word)));
    }

    @Override
    public List<LetterReceiverLetter> getUnpublishedLetters(long letterBatchId) {
        return getEntityManager().createQuery("SELECT l FROM LetterReceiverLetter l"
                + " WHERE l.id IN ("
                + " SELECT lrl.id FROM LetterBatch lb"
                + " INNER JOIN lb.letterReceivers lr"
                + " INNER JOIN lr.letterReceiverLetter lrl"
                + " WHERE lrl.readyForPublish = :readyForPublish AND lb.id = :letterBatchId AND lb.batchStatus = :status)",
                LetterReceiverLetter.class
                ).setParameter("readyForPublish", false)
                .setParameter("letterBatchId", letterBatchId)
                .setParameter("status", LetterBatch.Status.ready).getResultList();
    }

    @Override
    public Optional<Long> getLetterBatchIdReadyForPublish(String hakuOid, String type, String language) {
        return getLatestLetterBatchId(hakuOid, type, language, false);
    }

    @Override
    public Optional<Long> getLetterBatchIdReadyForEPosti(String hakuOid, String type, String language) {
        return getLatestLetterBatchId(hakuOid, type, language, true);
    }

    private Optional<Long> getLatestLetterBatchId(String hakuOid, String type, String language, boolean published) {
        List<Long> batchIds = getEntityManager().createQuery("SELECT l.id "
                + " FROM LetterBatch l"
                + " WHERE l.tag = l.applicationPeriod AND l.applicationPeriod = :applicationPeriod"
                + " AND l.templateName = :templateName AND l.language = :language AND l.id NOT IN ("
                    + " SELECT lb.id "
                    + " FROM LetterBatch lb"
                    + " INNER JOIN lb.letterReceivers lr "
                    + " INNER JOIN lr.letterReceiverLetter lrl WITH lrl.readyForPublish = :readyForPublish)"
                + " AND l.timestamp = ("
                    + " SELECT MAX(lb.timestamp)"
                    + " FROM LetterBatch lb"
                    + " WHERE lb.tag = lb.applicationPeriod AND lb.applicationPeriod = :applicationPeriod"
                    + " AND lb.templateName = :templateName AND lb.language = :language"
                + ")")
                .setParameter("applicationPeriod", hakuOid)
                .setParameter("templateName", type)
                .setParameter("language", language.toUpperCase())
                .setParameter("readyForPublish", !published).getResultList();
        return 0 == batchIds.size() ? Optional.<Long>absent() : Optional.of(batchIds.get(0));
    }

    public LetterBatchCountDto countBatchStatus(String hakuOid, String type, String language) {
        List<?> resultList1 = getEntityManager().createQuery("SELECT l.id, l.batchStatus "
            + " FROM LetterBatch l "
            + " WHERE l.tag = l.applicationPeriod AND l.applicationPeriod = :applicationPeriod"
            + " AND l.templateName = :templateName AND l.language = :language"
            + " ORDER BY l.timestamp DESC")
            .setParameter("applicationPeriod", hakuOid)
            .setParameter("templateName", type)
            .setParameter("language", language.toUpperCase()).getResultList();

        if(0 == resultList1.size()) {
            return new LetterBatchCountDto();
        }

        Object[] result1 = (Object[])resultList1.get(0);
        long batchId = (Long)result1[0];
        LetterBatch.Status batchStatus = (LetterBatch.Status)result1[1];

        Object[] result2 = (Object[])getEntityManager().createQuery(
              " SELECT COUNT(lr.id),"
            + " COALESCE(SUM(CASE WHEN lrl.readyForPublish = true THEN 1 ELSE 0 END), 0),"
            + " COALESCE(SUM(CASE WHEN lrl.letter IS NULL THEN 1 ELSE 0 END), 0)"
            + " FROM LetterBatch l"
            + " INNER JOIN l.letterReceivers lr "
            + " INNER JOIN lr.letterReceiverLetter lrl "
            + " WHERE l.id = :letterBatchId")
            .setParameter("letterBatchId", batchId)
            .getResultList().get(0);

        long totalCount = (Long)result2[0];
        long readyForPublishCount = (Long)result2[1];
        long notReadyCount = (Long)result2[2];

        if(LetterBatch.Status.ready.equals(batchStatus)) {
            boolean readyForPublish = readyForPublishCount == 0l;
            boolean readyForEPosti = readyForPublishCount == totalCount;
            return new LetterBatchCountDto(totalCount, totalCount, 0l, readyForPublish, readyForEPosti);
        } else if(LetterBatch.Status.error.equals(batchStatus)) {
            return new LetterBatchCountDto(totalCount, 0l, totalCount, false, false);
        } else {
            return new LetterBatchCountDto(totalCount, (totalCount - notReadyCount), 0l, false, false);
        }
    }

    public Map<String, String> getEPostiEmailAddressesByBatchId(long letterBatchId) {
        Map<String, String> applicationOidToEmailAddress = new HashMap<>();
        List<Object[]> resultList = getEntityManager().createQuery("SELECT lr.oidApplication, lr.emailAddressEPosti"
                + " FROM LetterBatch l"
                + " INNER JOIN l.letterReceivers lr "
                + " INNER JOIN lr.letterReceiverLetter lrl WITH lrl.readyForPublish = :readyForPublish"
                + " WHERE l.id = :letterBatchId")
                .setParameter("readyForPublish", true)
                .setParameter("letterBatchId", letterBatchId)
                .getResultList();
        for (Object[] row : resultList) {
            applicationOidToEmailAddress.put((String) row[0], (String) row[1]);
        }
        return applicationOidToEmailAddress;
    }
}
