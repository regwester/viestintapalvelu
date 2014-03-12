package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

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
    
	@Override
    public List<ReportedMessage> findAll(PagingAndSortingDTO pagingAndSorting) {
	    QReportedMessage reportedMessage = QReportedMessage.reportedMessage;
	    
	    OrderSpecifier<?> orderBy = orderBy(pagingAndSorting);
	    
	    JPAQuery findAllReportedMessagesQuery = from(reportedMessage).orderBy(orderBy);
	    
	    if (pagingAndSorting.getNumberOfRows() != 0) {
    	    findAllReportedMessagesQuery.limit(pagingAndSorting.getNumberOfRows()).offset(pagingAndSorting.getFromIndex());
	    }
	    
        return findAllReportedMessagesQuery.list(reportedMessage);
    }

    @Override
	public List<ReportedMessage> findBySearchCriteria(ReportedMessageQueryDTO query, 
	    PagingAndSortingDTO pagingAndSorting) {
		ReportedRecipientQueryDTO reportedRecipientQuery = query.getReportedRecipientQueryDTO();
		
		QReportedRecipient reportedRecipient = QReportedRecipient.reportedRecipient;
		QReportedMessage reportedMessage = QReportedMessage.reportedMessage;
		
		BooleanExpression whereExpression = null;
		
		if (reportedRecipientQuery.getRecipientOid() != null) {
			whereExpression = reportedRecipient.recipientOid.eq(reportedRecipientQuery.getRecipientOid());
		}
		
		if (reportedRecipientQuery.getRecipientEmail() != null) {
			whereExpression = reportedRecipient.recipientEmail.eq(reportedRecipientQuery.getRecipientEmail());
		}
		
		if (reportedRecipientQuery.getRecipientSocialSecurityID() != null) {
		}

		if (reportedRecipientQuery.getRecipientName() != null) {
			whereExpression = reportedRecipient.searchName.containsIgnoreCase(reportedRecipientQuery.getRecipientName());
		}
		
		OrderSpecifier<?> orderBy = orderBy(pagingAndSorting);
		JPAQuery findBySearchCriteria = from(reportedMessage).distinct().leftJoin(
		    reportedMessage.reportedRecipients, reportedRecipient).where(whereExpression).orderBy(orderBy).limit(
		    pagingAndSorting.getNumberOfRows()).offset(pagingAndSorting.getFromIndex());
		
		return findBySearchCriteria.list(reportedMessage);
	}

	@Override
	public List<ReportedMessage> findReportedMessagesBySendersOids(List<String> senderOids) {
		QReportedMessage reportedMessage = QReportedMessage.reportedMessage;
		
		BooleanExpression whereExpression = reportedMessage.senderOid.in(senderOids);		
		List<ReportedMessage> reportedMessages = from(reportedMessage).where(whereExpression).list(reportedMessage);
			
		return reportedMessages;
	}

	@Override
	public Long findNumberOfReportedMessage() {
		EntityManager em = getEntityManager();
		
		String findNumberOfReportedMessages = "SELECT COUNT(*) FROM ReportedMessage a";
		TypedQuery<Long> query = em.createQuery(findNumberOfReportedMessages, Long.class);
		
		return query.getSingleResult();
	}
	
	protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }
	
	protected OrderSpecifier<?> orderBy(PagingAndSortingDTO pagingAndSorting) {
	    if (pagingAndSorting.getSortedBy() == null || pagingAndSorting.getSortedBy().isEmpty()) {
	        return QReportedMessage.reportedMessage.sendingStarted.asc();
	    }
	    
	    if (pagingAndSorting.getSortedBy().equalsIgnoreCase("sendingStarted")) {
	        if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
	            return QReportedMessage.reportedMessage.sendingStarted.asc();
	        }
	        
	        return QReportedMessage.reportedMessage.sendingStarted.desc();
	    }
	    
	    if (pagingAndSorting.getSortedBy().equalsIgnoreCase("process")) {
            if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
                return QReportedMessage.reportedMessage.process.asc();
            }
            
            return QReportedMessage.reportedMessage.process.desc();	        
	    }

       if (pagingAndSorting.getSortedBy().equalsIgnoreCase("subject")) {
           if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
               return QReportedMessage.reportedMessage.process.asc();
           }
           
           return QReportedMessage.reportedMessage.process.desc();         	            
       }

       return QReportedMessage.reportedMessage.sendingStarted.asc();
	}
}
