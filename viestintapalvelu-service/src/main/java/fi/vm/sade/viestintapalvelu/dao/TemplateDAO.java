package fi.vm.sade.viestintapalvelu.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.model.Template;

public interface TemplateDAO extends JpaDAO<Template, Long> {

	public Template findTemplateByName(String name, String language);

	public Template findTemplateByName(String name, String language, String type);
	
	public List<String> getAvailableTemplates();
}
