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
public class ReportedMessageAttachmentDAOImpl extends AbstractJpaDAOImpl<ReportedMessageAttachment, Long> 
	implements ReportedMessageAttachmentDAO {

	@Override
	public List<ReportedMessageAttachment> findReportedMessageAttachments(Long viestinID) {
		EntityManager em = getEntityManager();
		
		String findViestinLiitteet = "SELECT a FROM ReportedMessageAttachment a JOIN a.reportedMessage " + 
			"WHERE a.sendingStarted = null";
		TypedQuery<ReportedMessageAttachment> query = 
			em.createQuery(findViestinLiitteet, ReportedMessageAttachment.class);
		
		return query.getResultList();
	}
	
	protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }
}
