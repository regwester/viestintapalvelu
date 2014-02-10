package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedRecipientDAO;
import fi.vm.sade.ryhmasahkoposti.model.QReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.QReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

@Repository
public class ReportedRecipientDAOImpl extends AbstractJpaDAOImpl<ReportedRecipient, Long> implements ReportedRecipientDAO {
	@Override
	public ReportedRecipient findByMessageIdAndRecipientEmail(Long messageID, String recipientEmail) {
		QReportedRecipient reportedRecipient = QReportedRecipient.reportedRecipient;
		QReportedMessage reportedMessage = QReportedMessage.reportedMessage;
		
		BooleanExpression whereExpression = reportedRecipient.reportedMessage.id.eq(messageID);
		whereExpression = reportedRecipient.recipientEmail.eq(recipientEmail);
		
		return from(reportedRecipient).join(
			reportedRecipient.reportedMessage, reportedMessage).where(
			whereExpression).singleResult(reportedRecipient);
	}

	@Override
	public ReportedRecipient findByRecipientID(Long recipientID) {
		return read(recipientID);
	}

	@Override
	public Date findMaxValueOfSendingEndedByMessageID(Long messageID) {
		EntityManager em = getEntityManager();
		
		String findMaxValueOfSendingEnded = "SELECT MAX(a.sendingEnded) FROM ReportedRecipient a "	+ 
			"WHERE a.reportedMessage.id = :messageID";
		TypedQuery<Date> query = em.createQuery(findMaxValueOfSendingEnded, Date.class);
		query.setParameter("messageID", messageID);
				
		return query.getSingleResult();
	}

	@Override
	public Long findNumberOfRecipientsByMessageID(Long messageID) {
		EntityManager em = getEntityManager();
		
		String findNumberOfRecipients = "SELECT COUNT(*) FROM ReportedRecipient a " + 
			"JOIN a.reportedMessage WHERE a.reportedMessage.id = :messageID";
		TypedQuery<Long> query = em.createQuery(findNumberOfRecipients, Long.class);
		query.setParameter("messageID", messageID);
		
		return query.getSingleResult();
	}
	
	@Override
	public Long findNumberOfRecipientsByMessageIDAndSendingSuccesful(Long messageID, boolean sendingSuccesful) {
		EntityManager em = getEntityManager();
		
		String findNumberOfRecipients = "SELECT COUNT(*) FROM ReportedRecipient a "	+ 
			"JOIN a.reportedMessage WHERE a.reportedMessage.id = :messageID AND a.sendingSuccesful = :sendingSuccesful";
		TypedQuery<Long> query = em.createQuery(findNumberOfRecipients, Long.class);
		query.setParameter("messageID", messageID);
		
		if (sendingSuccesful) {
			query.setParameter("sendingSuccesful", "1");
		} else {
			query.setParameter("sendingSuccesful", "0");
		}
		
		return query.getSingleResult();
	}

	@Override
	public List<ReportedRecipient> findUnhandled() {
		EntityManager em = getEntityManager();
		
		String findUnhandled = "SELECT a FROM ReportedRecipient a JOIN a.reportedMessage " + 
			"WHERE a.sendingStarted = null";
		TypedQuery<ReportedRecipient> query = em.createQuery(findUnhandled, ReportedRecipient.class);
		
		return query.getResultList();
	}
	
	protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }
}
