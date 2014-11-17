package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.model.TemplateApplicationPeriod;

@Repository
public class TemplateDAOImpl extends AbstractJpaDAOImpl<Template, Long> implements TemplateDAO {

    @Override
    public Template findTemplate(TemplateCriteria criteria) {
        TypedQuery<Template> query = buildTemplateQuery(criteria, "timestamp DESC");
        query.setFirstResult(0); // LIMIT 1
        query.setMaxResults(1); //

        Template templ = new Template();
        try {
            templ = query.getSingleResult();
        } catch (NoResultException e) {
            templ = null;
        }
        return templ;
    }

    @Override
    public List<Template> findTemplates(TemplateCriteria templateCriteria) {
        TypedQuery<Template> query = buildTemplateQuery(templateCriteria, "timestamp ASC");
        return query.getResultList();
    }

    protected TypedQuery<Template> buildTemplateQuery(TemplateCriteria criteria, String orderBy) {
        EntityManager em = getEntityManager();

        StringBuffer queryStr = new StringBuffer(),
            where = new StringBuffer();
        queryStr.append("SELECT a FROM Template a ");
        if (criteria.getName() != null) {
            and(where).append("a.name = :name");
        }
        if (criteria.getLanguage() != null) {
            and(where).append("a.language = :language");
        }
        if (criteria.getType() != null) {
            queryStr.append(" INNER JOIN a.structure s INNER JOIN s.contentStructures cs");
            and(where).append("cs.type = :type");
        }
        
        if (criteria.getState() != null) {
            and(where).append("a.state = :state");
        }
        
        if (criteria.getApplicationPeriod() != null) {
            queryStr.append(" INNER JOIN a.applicationPeriods templateApplicationPeriod ");
            and(where).append("templateApplicationPeriod.id.applicationPeriod = :applicationPeriodOid");
        }
        if (criteria.isDefaultRequired()) {
            and(where).append("a.usedAsDefault = true");
        }
        if (where.length() > 0) {
            queryStr.append(" WHERE ").append(where);
        }
        if (orderBy != null) {
            queryStr.append(" ORDER BY a.").append(orderBy);
        }

        TypedQuery<Template> query = em.createQuery(queryStr.toString(), Template.class);
        if (criteria.getName() != null) {
            query.setParameter("name", criteria.getName());
        }
        if (criteria.getLanguage() != null) {
            query.setParameter("language", criteria.getLanguage());
        }
        if (criteria.getType() != null) {
            query.setParameter("type", criteria.getType());
        }
        if (criteria.getApplicationPeriod() != null) {
            query.setParameter("applicationPeriodOid", criteria.getApplicationPeriod());
        }
        
        if (criteria.getState() != null) {
            query.setParameter("state", criteria.getState());
        }
        return query;
    }

    @Override
    public Template persist(Template templateHaku) {
        getEntityManager().persist(templateHaku);
        return templateHaku;
    }

    @Override
    public TemplateApplicationPeriod insert(TemplateApplicationPeriod templateApplicationPeriod) {
        getEntityManager().persist(templateApplicationPeriod);
        return templateApplicationPeriod;
    }

    @Override
    public void remove(TemplateApplicationPeriod templateApplicationPeriod) {
        getEntityManager().remove(templateApplicationPeriod);
    }

    protected static StringBuffer and(StringBuffer b) {
        if (b.length() > 0) {
            b.append(" AND ");
        }
        return b;
    }

    public List<String> getAvailableTemplates() {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT DISTINCT name, language from Template");
        @SuppressWarnings("unchecked")
        List<String> result = resultsToNameLanguage((List<Object[]>) q.getResultList());
        return result;
    }

    public List<String> getAvailableTemplatesByType(Template.State state) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT DISTINCT name, language from Template WHERE state = :state");
        query.setParameter("state", state);
        @SuppressWarnings("unchecked")
        List<String> result = resultsToNameLanguage((List<Object[]>) query.getResultList());
        return result;
    }
    
    private List<String> resultsToNameLanguage(List<Object[]> qResult) {
        List<String> result = new ArrayList<String>();
        for (Object[] o : qResult) {
            StringBuilder current = new StringBuilder();
            for (Object ob : o) {
                if (ob != null) {
                    if (current.length() > 0) {
                        current.append("::");
                    }
                    current.append(ob.toString());
                }
            }
            result.add(current.toString());

        }
        return result;
    }

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.dao.TemplateDAO#findByIdAndState(java.lang.Long, fi.vm.sade.viestintapalvelu.model.Template.State)
     */
    @Override
    public Template findByIdAndState(Long id, State state) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT templ from Template templ WHERE id = :id AND state = :state");
        query.setParameter("id", id);
        query.setParameter("state", state);
        return (Template) query.getSingleResult();
    }
    
}
