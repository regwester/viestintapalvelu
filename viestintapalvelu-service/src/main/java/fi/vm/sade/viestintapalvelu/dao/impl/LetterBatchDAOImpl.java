package fi.vm.sade.viestintapalvelu.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;

@Repository
public class LetterBatchDAOImpl extends AbstractJpaDAOImpl<LetterBatch, Long> implements LetterBatchDAO {

	public LetterBatch findLetterBatchByNameOrgTag(String templateName, String organizationOid, String tag) {
		EntityManager em = getEntityManager();
		
		String findTemplate = "SELECT a FROM LetterBatch a WHERE "
													+ "a.templateName=:templateName AND "
													+ "organizationOid=:organizationOid AND "
													+ "fetchTarget LIKE :tag "
								+ "ORDER BY a.timestamp DESC";
		
		TypedQuery<LetterBatch> query = em.createQuery(findTemplate, LetterBatch.class);
		query.setParameter("templateName", templateName);
		query.setParameter("organizationOid", organizationOid);		
		query.setParameter("tag", tag);		
		query.setFirstResult(0);	// LIMIT 1
		query.setMaxResults(1);  	//
			
		LetterBatch letterBatch = query.getSingleResult();
		
		return letterBatch;
	}
}
