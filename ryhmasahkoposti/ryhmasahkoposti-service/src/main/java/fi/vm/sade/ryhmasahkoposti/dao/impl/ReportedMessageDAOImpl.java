package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailRecipientQueryDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import fi.vm.sade.ryhmasahkoposti.model.QReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.QReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;

@Repository
public class ReportedMessageDAOImpl extends AbstractJpaDAOImpl<ReportedMessage, Long> implements ReportedMessageDAO {
	
	@Override
	public List<ReportedMessage> findBySearchCriteria(EmailMessageQueryDTO query) {
		EmailRecipientQueryDTO emailRecipientQuery = query.getEmailRecipientQueryDTO();
		
		QReportedRecipient reportedRecipient = QReportedRecipient.reportedRecipient;
		QReportedMessage reportedMessage = QReportedMessage.reportedMessage;
		
		BooleanExpression whereExpression = null;
		
		if (emailRecipientQuery.getRecipientOid() != null) {
			whereExpression = reportedRecipient.recipientOid.eq(emailRecipientQuery.getRecipientOid());
		}
		
		if (emailRecipientQuery.getRecipientEmail() != null) {
			whereExpression = reportedRecipient.recipientEmail.eq(emailRecipientQuery.getRecipientEmail());
		}
		
		if (emailRecipientQuery.getRecipientSocialSecurityID() != null) {
		}

		if (emailRecipientQuery.getRecipientName() != null) {
			whereExpression = reportedRecipient.searchName.containsIgnoreCase(emailRecipientQuery.getRecipientName());
		}
		
		return from(reportedMessage).leftJoin(
			reportedMessage.reportedRecipients, reportedRecipient).fetch().where(
			whereExpression).list(reportedMessage);
	}

	@Override
	public List<ReportedMessage> findSendersReportedMessages(List<String> senderOids) {
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
}
