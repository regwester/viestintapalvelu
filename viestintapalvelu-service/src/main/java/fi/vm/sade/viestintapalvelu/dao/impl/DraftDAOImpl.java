package fi.vm.sade.viestintapalvelu.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.DraftDAO;
import fi.vm.sade.viestintapalvelu.model.Draft;
import fi.vm.sade.viestintapalvelu.model.QDraft;

@Repository
public class DraftDAOImpl extends AbstractJpaDAOImpl<Draft, Long> implements DraftDAO {

	public Draft findDraftByNameOrgTag(	String templateName, String templateLanguage, String organizationOid, 
										String applicationPeriod, String fetchTarget, String tag) {
		EntityManager em = getEntityManager();		
		
		boolean ap = true;
		boolean ft = true;
		boolean ta = true;
		
		if ((applicationPeriod == null) || ("".equals(applicationPeriod))) ap = false;		
		if ((fetchTarget == null) || ("".equals(fetchTarget))) ft = false;		
		if ((tag == null) || ("".equals(tag))) ta = false;		
		
		String findDraft = "SELECT a FROM Draft a WHERE "
				+ "a.templateName = :templateName AND "
				+ "a.templateLanguage = :templateLanguage AND "
				+ "a.organizationOid = :organizationOid ";
		
		if (ap) findDraft = findDraft + "AND a.applicationPeriod = :applicationPeriod ";
		if (ft) findDraft = findDraft + "AND a.fetchTarget = :fetchTarget ";
		if (ta) findDraft = findDraft + "AND a.tag = :tag ";
		
		findDraft = findDraft + "ORDER BY a.timestamp DESC";
		
//		String findDraft = "SELECT a FROM Draft a WHERE "
//													+ "a.templateName = :templateName AND "
//													+ "a.templateLanguage = :templateLanguage AND "
//													+ "a.organizationOid = :organizationOid AND "
//													
//													+ "a.applicationPeriod = :applicationPeriod AND "
//													+ "a.fetchTarget = :fetchTarget AND "
//													+ "a.tag = :tag "
//								+ "ORDER BY a.timestamp DESC";
		
		TypedQuery<Draft> query = em.createQuery(findDraft, Draft.class);
		query.setParameter("templateName", templateName);
		query.setParameter("templateLanguage", templateLanguage);
        query.setParameter("organizationOid", organizationOid);		        
        if (ap) query.setParameter("applicationPeriod", applicationPeriod);		        
        if (ft) query.setParameter("fetchTarget", fetchTarget);		                
        if (ta) query.setParameter("tag", tag);		
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

	public Draft findDraftByNameOrgTag2(String templateName, String templateLanguage, String organizationOid, 
										String applicationPeriod, String fetchTarget, String tag) {
        QDraft draft = QDraft.draft;
        
        BooleanExpression whereExpression = draft.templateName.eq(templateName)
        									.and(draft.templateLanguage.eq(templateLanguage)
        									.and(draft.organizationOid.eq(organizationOid)));
        
        if (!"".equals(applicationPeriod)) {        	
        	whereExpression = whereExpression.and(draft.applicationPeriod.eq(applicationPeriod));
        }
        if (!"".equals(fetchTarget)) {        	
        	whereExpression = whereExpression.and(draft.fetchTarget.eq(fetchTarget));
        }
        if (!"".equals(tag)) {        	
        	whereExpression = whereExpression.and(draft.tag.eq(tag));
        }
        
		JPAQuery findDraf = from(draft).where(whereExpression).orderBy(QDraft.draft.timestamp.desc()).limit(1);
                
		return findDraf.singleResult(draft);
	}
	
	protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }
	
	
}
