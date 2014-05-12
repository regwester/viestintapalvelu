package fi.vm.sade.viestintapalvelu.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.DraftDAO;
import fi.vm.sade.viestintapalvelu.model.Draft;

@Repository
public class DraftDAOImpl extends AbstractJpaDAOImpl<Draft, Long> implements DraftDAO {

	public Draft findDraftByNameOrgTag(	String templateName, String templateLanguage, String organizationOid, 
										String applicationPeriod, String fetchTarget, String tag) {
		EntityManager em = getEntityManager();
		
//		SELECT * FROM kirjeet.luonnos WHERE kirjepohjan_nimi = 'koekutsukirje' AND kirjepohjan_kielikoodi='FI'
//				AND oid_organisaatio LIKE '%%'
//				AND haku LIKE '%%' AND hakukohde LIKE '%%' AND tunniste LIKE '%%';
		
		
		String findDraft = "SELECT a FROM Draft a WHERE "
													+ "a.templateName = :templateName AND "
													+ "a.templateLanguage = :templateLanguage AND "
													+ "a.organizationOid = :organizationOid AND "
													
													+ "a.applicationPeriod = :applicationPeriod AND "
													+ "a.fetchTarget = :fetchTarget AND "
													+ "a.tag = :tag "
								+ "ORDER BY a.timestamp DESC";
		
		TypedQuery<Draft> query = em.createQuery(findDraft, Draft.class);
		query.setParameter("templateName", templateName);
		query.setParameter("templateLanguage", templateLanguage);
        query.setParameter("organizationOid", organizationOid);		        
        query.setParameter("applicationPeriod", applicationPeriod);		        
        query.setParameter("fetchTarget", fetchTarget);		                
		query.setParameter("tag", tag);		
		query.setFirstResult(0);	// LIMIT 1
		query.setMaxResults(1);  	//
			
		Draft draft = new Draft();
		try {
			draft = query.getSingleResult();
		} catch (Exception e) {
			draft = null;
		}
		
		return draft;
	}

}
