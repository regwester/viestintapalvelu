package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.PathBuilder;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchStatusDto;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.dto.query.LetterReportQueryDTO;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.QLetterBatch;
import fi.vm.sade.viestintapalvelu.model.QLetterReceiverAddress;
import fi.vm.sade.viestintapalvelu.model.QLetterReceivers;

@Repository
public class LetterBatchDAOImpl extends AbstractJpaDAOImpl<LetterBatch, Long> implements LetterBatchDAO {

    public LetterBatch findLetterBatchByNameOrgTag(String templateName, String language, String organizationOid,
                                                   Optional<String> tag, Optional<String> applicationPeriod) {
        EntityManager em = getEntityManager();

        String findTemplate = "SELECT a FROM LetterBatch a WHERE "
            + "a.templateName=:templateName AND "
            + "a.organizationOid=:organizationOid AND "
            + "a.language=:language  ";
        if (tag.isPresent()) {
            findTemplate += " AND a.tag LIKE :tag ";
        }
        if (applicationPeriod.isPresent()) {
            findTemplate += " AND a.applicationPeriod = :applicationPeriod ";
        }
        findTemplate +=  "ORDER BY a.timestamp DESC";

        TypedQuery<LetterBatch> query = em.createQuery(findTemplate, LetterBatch.class);
        query.setParameter("templateName", templateName);
        query.setParameter("language", language);
        query.setParameter("organizationOid", organizationOid);
        if (tag.isPresent()) {
            query.setParameter("tag", tag.get());
        }
        if (applicationPeriod.isPresent()) {
            query.setParameter("applicationPeriod", applicationPeriod.get());
        }
        query.setFirstResult(0);	// LIMIT 1
        query.setMaxResults(1);  	//

        LetterBatch letterBatch = new LetterBatch();
        try {
            letterBatch = query.getSingleResult();
        } catch (Exception e) {
            letterBatch = null;
        }

        return letterBatch;
    }

    public LetterBatch findLetterBatchByNameOrg(String templateName, String language, String organizationOid) {
        EntityManager em = getEntityManager();

        String findTemplate = "SELECT a FROM LetterBatch a WHERE "
                + "a.templateName=:templateName AND "
                + "a.language=:language AND "
                + "a.organizationOid=:organizationOid "
                + "ORDER BY a.timestamp DESC";

        TypedQuery<LetterBatch> query = em.createQuery(findTemplate, LetterBatch.class);
        query.setParameter("templateName", templateName);
        query.setParameter("language", language);
        query.setParameter("organizationOid", organizationOid);
        query.setFirstResult(0);    // LIMIT 1
        query.setMaxResults(1);     //

        LetterBatch letterBatch = new LetterBatch();
        try {
            letterBatch = query.getSingleResult();
        } catch (Exception e) {
            letterBatch = null;
        }

        return letterBatch;
    }

    @Override
    public List<LetterBatch> findLetterBatchesByOrganizationOid(String organizationOID, 
        PagingAndSortingDTO pagingAndSorting) {
        QLetterBatch letterBatch = QLetterBatch.letterBatch;

        OrderSpecifier<?> orderBy = orderBy(pagingAndSorting);        
        BooleanExpression whereExpression = letterBatch.organizationOid.eq(organizationOID);
        JPAQuery findLetterBatches = from(letterBatch).where(whereExpression).orderBy(orderBy);
        
        if (pagingAndSorting.getNumberOfRows() != 0) {
            findLetterBatches.limit(pagingAndSorting.getNumberOfRows()).offset(pagingAndSorting.getFromIndex());
        }
        
        return findLetterBatches.list(letterBatch);
    }

    @Override
    public List<LetterBatch> findLetterBatchesBySearchArgument(LetterReportQueryDTO query,
        PagingAndSortingDTO pagingAndSorting) {
        QLetterBatch letterBatch = QLetterBatch.letterBatch;
        QLetterReceivers letterReceivers = QLetterReceivers.letterReceivers;
        QLetterReceiverAddress letterReceiverAddress = QLetterReceiverAddress.letterReceiverAddress;
        
        BooleanBuilder whereExpression = whereExpressionForSearchCriteria(query, letterBatch, letterReceiverAddress);        
        OrderSpecifier<?> orderBy = orderBy(pagingAndSorting);
    
        JPAQuery findBySearchCriteria = from(letterBatch).distinct().leftJoin(
            letterBatch.letterReceivers, letterReceivers).leftJoin(
            letterReceivers.letterReceiverAddress, letterReceiverAddress).where(whereExpression).orderBy(orderBy).limit(
            pagingAndSorting.getNumberOfRows()).offset(pagingAndSorting.getFromIndex());
        
        return findBySearchCriteria.list(letterBatch);
    }

    @Override
    public Long findNumberOfLetterBatches(String organizationOid) {
    	EntityManager em = getEntityManager();
    	
    	String findNumberOfLetterBatches = 
    		"SELECT COUNT(*) FROM LetterBatch a WHERE a.organizationOid = :organizationOid";
    	TypedQuery<Long> query = em.createQuery(findNumberOfLetterBatches, Long.class);
    	query.setParameter("organizationOid", organizationOid);
    
    	return query.getSingleResult();
    }

    @Override
    public Long findNumberOfLetterBatchesBySearchArgument(LetterReportQueryDTO letterReportQuery) {
    	QLetterBatch letterBatch = QLetterBatch.letterBatch;
        QLetterReceivers letterReceivers = QLetterReceivers.letterReceivers;
        QLetterReceiverAddress letterReceiverAddress = QLetterReceiverAddress.letterReceiverAddress;
        
        BooleanBuilder whereExpression = whereExpressionForSearchCriteria(letterReportQuery, letterBatch, letterReceiverAddress);        
        JPAQuery findBySearchCriteria = from(letterBatch).distinct().leftJoin(
                letterBatch.letterReceivers, letterReceivers).leftJoin(
                letterReceivers.letterReceiverAddress, letterReceiverAddress).where(whereExpression);
        
    	return findBySearchCriteria.count();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public LetterBatchStatusDto getLetterBatchStatus(long letterBatchId) {
        List<LetterBatchStatusDto> results = (List<LetterBatchStatusDto>) getEntityManager()
                .createNamedQuery("letterBatchStatus")
                    .setParameter("batchId", letterBatchId)
                .setMaxResults(1)
                .getResultList();
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    @Override
    public List<Long> findUnprocessedLetterReceiverIdsByBatch(long batchId) {
        return getEntityManager().createQuery(
                    "select lr.id from LetterReceivers lr"
                    + " inner join lr.letterBatch batch with batch.id = :batchId"
                    + " inner join lr.letterReceiverLetter letter"
                    + " where letter.letter = null"
                    + " order by lr.id", Long.class)
            .setParameter("batchId", batchId).getResultList();
    }
    
    @Override
    public List<Long> findAllLetterReceiverIdsByBatch(long batchId) {
        return getEntityManager().createQuery(
                    "select lr.id from LetterReceivers lr"
                    + " inner join lr.letterBatch batch with batch.id = :batchId"
                    + " order by lr.id", Long.class)
            .setParameter("batchId", batchId).getResultList();
    }
    
    @Override
    public List<Long> findUnfinishedLetterBatches() {
        return getEntityManager().createQuery("SELECT lb.id FROM LetterBatch lb"
                + " WHERE lb.batchStatus != 'ready' AND lb.batchStatus != 'error'"
                + " ORDER BY lb.timestamp ASC", Long.class).getResultList();
    }

    protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }

    protected OrderSpecifier<?> orderBy(PagingAndSortingDTO pagingAndSorting) {
        PathBuilder<LetterBatch> pb = new PathBuilder<LetterBatch>(LetterBatch.class, "letterBatch");
        
        if (pagingAndSorting.getSortedBy() != null && !pagingAndSorting.getSortedBy().isEmpty()) {
            if (pagingAndSorting.getSortOrder() == null || pagingAndSorting.getSortOrder().isEmpty()) {
                return pb.getString(pagingAndSorting.getSortedBy()).asc();
            }
            
            if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
                return pb.getString(pagingAndSorting.getSortedBy()).asc();
            }
            
            return pb.getString(pagingAndSorting.getSortedBy()).desc();
        }
        
        return pb.getString("timestamp").asc();
    }
    
    protected BooleanBuilder whereExpressionForSearchCriteria(LetterReportQueryDTO query, QLetterBatch letterBatch, 
        QLetterReceiverAddress letterReceiverAddress) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        
        if (query.getOrganizationOid() != null) {
            booleanBuilder.and(letterBatch.organizationOid.in(query.getOrganizationOid()));
        }
        
        if (query.getSearchArgument() != null && !query.getSearchArgument().isEmpty()) {
            booleanBuilder.and(letterBatch.templateName.containsIgnoreCase(query.getSearchArgument()));
            booleanBuilder.or(letterBatch.fetchTarget.containsIgnoreCase(query.getSearchArgument()));
            booleanBuilder.or(letterBatch.applicationPeriod.contains(query.getSearchArgument()));
            booleanBuilder.or(letterReceiverAddress.lastName.concat(" ").concat(
                letterReceiverAddress.firstName).containsIgnoreCase(query.getSearchArgument()));
            booleanBuilder.or(letterReceiverAddress.postalCode.contains(query.getSearchArgument()));
        }
                
        return booleanBuilder;
    }
}
