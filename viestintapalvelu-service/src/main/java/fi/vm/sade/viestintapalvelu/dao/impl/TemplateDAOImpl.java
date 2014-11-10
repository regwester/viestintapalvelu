package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.model.Structure;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.TemplateApplicationPeriod;

@Repository
public class TemplateDAOImpl extends AbstractJpaDAOImpl<Template, Long>
        implements TemplateDAO {

    @Override
    public Template insert(Template entity) {
        // TODO: a temporal solution: REMOVE ME (the whole method) after Structure building implemented in the app
        boolean oldStyle = false;
        if (entity.getStructure() == null) {
            oldStyle = true;
            // select a random template for the null constraint:
            List<Structure> structures = getEntityManager().createQuery("select s from Structure s order by s.id", Structure.class)
                    .setMaxResults(1).getResultList();
            if (structures.isEmpty()) {
                throw new IllegalStateException("No structure specified (and no existing structures in the system " +
                        "allowing to perform an old style migration)");
            }
            entity.setStructure(structures.get(0));
        }
        super.insert(entity);
        if (oldStyle) {
            // create the new template structure based on the old one:
            getEntityManager().createNativeQuery(
                "update kirjeet.kirjepohja set rakenne = kirjeet.luoRakenneVanhastaPohjasta(id) where " +
                        " id = ?;").setParameter(1, entity.getId()).executeUpdate();
            entity.setStructure(getEntityManager()
                    .createQuery("select t.structure from Template t where t.id=:id", Structure.class)
                    .setParameter("id", entity.getId()).getResultList().get(0));
        }
        return entity;
    }

    @Override
    public Template findTemplate(TemplateCriteria criteria) {
        TypedQuery<Template> query = buildTemplateQuery(criteria, "timestamp DESC");
        query.setFirstResult(0); // LIMIT 1
        query.setMaxResults(1); //

        Template templ = new Template();
        try {
            templ = query.getSingleResult();
        } catch (Exception e) {
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
            and(where).append("a.type = :type");
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

    /**
     * Find template by name
     */
    public Template findTemplateByName(String name, String language) {
        return findTemplate(new TemplateCriteriaImpl(name, language));
    }

    /**
     * Find template by name
     */
    public Template findTemplateByName(String name, String language, String type) {
        return findTemplate(new TemplateCriteriaImpl(name, language).withType(type));
    }

    public List<String> getAvailableTemplates() {
        EntityManager em = getEntityManager();
        Query q = em
                .createQuery("SELECT DISTINCT name, language from Template");
        @SuppressWarnings("unchecked")
        List<Object[]> qResult = (List<Object[]>) q.getResultList();
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
}
