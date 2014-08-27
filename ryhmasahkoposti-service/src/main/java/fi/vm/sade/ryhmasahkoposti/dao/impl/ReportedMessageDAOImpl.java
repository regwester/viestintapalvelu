package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedRecipientQueryDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import fi.vm.sade.ryhmasahkoposti.model.QReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.QReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;

@Repository
public class ReportedMessageDAOImpl extends AbstractJpaDAOImpl<ReportedMessage, Long> implements ReportedMessageDAO {

    private QReportedMessage reportedMessage = QReportedMessage.reportedMessage;
    private QReportedRecipient reportedRecipient = QReportedRecipient.reportedRecipient;

    @Override
    public List<ReportedMessage> findByOrganizationOid(String organizationOid, PagingAndSortingDTO pagingAndSorting) {

        JPAQuery findAllReportedMessagesQuery = null;
        OrderSpecifier<?> orderBy = orderBy(pagingAndSorting);
        BooleanExpression whereExpression = reportedMessage.senderOrganizationOid.in(organizationOid);

        findAllReportedMessagesQuery = from(reportedMessage).where(whereExpression).orderBy(orderBy);

        if (pagingAndSorting.getNumberOfRows() != 0) {
            findAllReportedMessagesQuery.limit(pagingAndSorting.getNumberOfRows()).offset(pagingAndSorting.getFromIndex());
        }

        return findAllReportedMessagesQuery.list(reportedMessage);
    }

    @Override
    public List<ReportedMessage> findBySenderOid(String senderOid, PagingAndSortingDTO pagingAndSortingDTO) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(reportedMessage).where(reportedMessage.senderOid.eq(senderOid))
                .orderBy(orderBy(pagingAndSortingDTO)).list(reportedMessage);
    }

    @Override
    public List<ReportedMessage> findBySenderOidAndProcess(String senderOid, String process, PagingAndSortingDTO pagingAndSorting) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(reportedMessage).where(reportedMessage.senderOid.eq(senderOid)
                .and(reportedMessage.process.equalsIgnoreCase(process)))
                .orderBy(orderBy(pagingAndSorting)).list(reportedMessage);
    }

    @Override
    public List<ReportedMessage> findBySearchCriteria(ReportedMessageQueryDTO query,
                                                      PagingAndSortingDTO pagingAndSorting) {
        ReportedRecipientQueryDTO reportedRecipientQuery = query.getReportedRecipientQueryDTO();

        BooleanBuilder whereExpression = whereExpressionForSearchCriteria(query, reportedRecipientQuery);
        OrderSpecifier<?> orderBy = orderBy(pagingAndSorting);

        JPAQuery findBySearchCriteria = from(reportedMessage).distinct().leftJoin(
                reportedMessage.reportedRecipients, reportedRecipient).where(whereExpression).orderBy(orderBy).limit(
                pagingAndSorting.getNumberOfRows()).offset(pagingAndSorting.getFromIndex());

        return findBySearchCriteria.list(reportedMessage);
    }

    @Override
    public Long findNumberOfReportedMessages(String organizationOid) {
        EntityManager em = getEntityManager();

        String findNumberOfReportedMessages =
                "SELECT COUNT(*) FROM ReportedMessage a WHERE a.senderOrganizationOid = :organizationOid";
        TypedQuery<Long> query = em.createQuery(findNumberOfReportedMessages, Long.class);
        query.setParameter("organizationOid", organizationOid);

        return query.getSingleResult();
    }

    @Override
    public Long findNumberOfUserMessages(String userOid) {
        JPAQuery query = new JPAQuery(getEntityManager());
        return query.from(reportedMessage)
                .where(reportedMessage.senderOid.eq(userOid))
                .count();
    }

    @Override
    public Long findNumberOfReportedMessage(ReportedMessageQueryDTO query) {
        ReportedRecipientQueryDTO reportedRecipientQuery = query.getReportedRecipientQueryDTO();

        BooleanBuilder whereExpression = whereExpressionForSearchCriteria(query, reportedRecipientQuery);

        JPAQuery findBySearchCriteria = from(reportedMessage).distinct().leftJoin(
                reportedMessage.reportedRecipients, reportedRecipient).where(whereExpression);

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

        if (pagingAndSorting.getSortedBy().equalsIgnoreCase("process")) {
            if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
                return reportedMessage.process.asc();
            }

            return reportedMessage.process.desc();
        }

        if (pagingAndSorting.getSortedBy().equalsIgnoreCase("subject")) {
            if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
                return reportedMessage.process.asc();
            }

            return reportedMessage.process.desc();
        }

        return reportedMessage.sendingStarted.asc();
    }

    protected BooleanBuilder whereExpressionForSearchCriteria(ReportedMessageQueryDTO query,
                                                              ReportedRecipientQueryDTO reportedRecipientQuery) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (query.getOrganizationOid() != null) {
            booleanBuilder.and(reportedMessage.senderOrganizationOid.in(query.getOrganizationOid()));
        }

        if (reportedRecipientQuery.getRecipientOid() != null) {
            booleanBuilder.and(reportedRecipient.recipientOid.eq(reportedRecipientQuery.getRecipientOid()));
        }

        if (reportedRecipientQuery.getRecipientEmail() != null) {
            booleanBuilder.and(reportedRecipient.recipientEmail.eq(reportedRecipientQuery.getRecipientEmail()));
        }

        if (reportedRecipientQuery.getRecipientSocialSecurityID() != null) {
            booleanBuilder.and(reportedRecipient.socialSecurityID.eq(
                    reportedRecipientQuery.getRecipientSocialSecurityID()));
        }

        if (query.getSearchArgument() != null && !query.getSearchArgument().isEmpty()) {
            booleanBuilder.and(reportedRecipient.searchName.containsIgnoreCase(reportedRecipientQuery.getRecipientName()));
            booleanBuilder.or(reportedMessage.process.containsIgnoreCase(query.getSearchArgument()));
            booleanBuilder.or(reportedMessage.subject.containsIgnoreCase(query.getSearchArgument()));
            booleanBuilder.or(reportedMessage.message.containsIgnoreCase(query.getSearchArgument()));
        }

        return booleanBuilder;
    }
}
