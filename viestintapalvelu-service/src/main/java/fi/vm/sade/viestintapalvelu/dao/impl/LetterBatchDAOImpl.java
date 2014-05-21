package fi.vm.sade.viestintapalvelu.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mysema.query.Query;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;

@Repository
public class LetterBatchDAOImpl extends AbstractJpaDAOImpl<LetterBatch, Long> implements LetterBatchDAO {

	public LetterBatch findLetterBatchByNameOrgTag(String templateName, String language, String organizationOid, String tag) {
		EntityManager em = getEntityManager();
		
		String findTemplate = "SELECT a FROM LetterBatch a WHERE "
													+ "a.templateName=:templateName AND "
													+ "a.organizationOid=:organizationOid AND "
													+ "a.language=:language AND "
                                                    + "a.tag LIKE :tag "
								+ "ORDER BY a.timestamp DESC";
		
		TypedQuery<LetterBatch> query = em.createQuery(findTemplate, LetterBatch.class);
		query.setParameter("templateName", templateName);
		query.setParameter("language", language);
        query.setParameter("organizationOid", organizationOid);		
		query.setParameter("tag", tag);		
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
}
