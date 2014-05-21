package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.model.Template;

@Repository
public class TemplateDAOImpl extends AbstractJpaDAOImpl<Template, Long>
        implements TemplateDAO {

    /**
     * Find template by name
     */
    public Template findTemplateByName(String name, String language) {
        return findTemplateByName(name, language, null);
    }

    /**
     * Find template by name
     */
    public Template findTemplateByName(String name, String language, String type) {
        EntityManager em = getEntityManager();

        String findTemplate = "SELECT a FROM Template a WHERE a.name = :name AND a.language = :language ORDER BY a.timestamp DESC";
        if (type != null)
            findTemplate = "SELECT a FROM Template a WHERE a.name = :name AND a.language = :language AND a.type = :type ORDER BY a.timestamp DESC";

        TypedQuery<Template> query = em.createQuery(findTemplate,
                Template.class);
        query.setParameter("name", name);
        query.setParameter("language", language);
        if (type != null) {
            query.setParameter("type", type);
        }
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
