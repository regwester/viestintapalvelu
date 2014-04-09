package fi.vm.sade.viestintapalvelu.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.model.Replacement;
import fi.vm.sade.viestintapalvelu.model.Template;

@Repository
public class TemplateDAOImpl extends AbstractJpaDAOImpl<Template, Long> implements TemplateDAO {

	public Template findTemplateByName(String name, String language) {
		EntityManager em = getEntityManager();
		
		String findTemplate = "SELECT a FROM Template a where a.name = :name AND a.language = :language ORDER BY a.timestamp DESC";		 
				
		TypedQuery<Template> query = em.createQuery(findTemplate, Template.class);
		query.setParameter("name", name);
		query.setParameter("language", language);		
		query.setFirstResult(0);  // LIMIT 1
		query.setMaxResults(1);  
			
		Template templ = query.getSingleResult();
		
		String findRepl = "SELECT a FROM Replacement a where a.template = :template";		 
		
		TypedQuery<Replacement> query2 = em.createQuery(findRepl, Replacement.class);
		query2.setParameter("template", templ);
		query.setFirstResult(0);  // LIMIT 1
		query.setMaxResults(1);  
		
		return query.getSingleResult();
	}
    
	
}
