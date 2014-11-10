package fi.vm.sade.viestintapalvelu.dao;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.TemplateApplicationPeriod;

import java.util.List;

public interface TemplateDAO extends JpaDAO<Template, Long> {

    /**
     * @param criteria
     * @return the first Template matching the criteria or null if not found
     */
    Template findTemplate(TemplateCriteria criteria);

    List<Template> findTemplates(TemplateCriteria templateCriteria);

    Template persist(Template templateHaku);

    TemplateApplicationPeriod insert(TemplateApplicationPeriod templateApplicationPeriod);

    void remove(TemplateApplicationPeriod templateApplicationPeriod);

    Template findTemplateByName(String name, String language);

	Template findTemplateByName(String name, String language, String type);
	
	List<String> getAvailableTemplates();
	
	List<String> getAvailableTemplatesByType(Template.State state);
}
