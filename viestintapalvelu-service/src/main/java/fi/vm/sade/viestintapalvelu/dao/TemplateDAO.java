package fi.vm.sade.viestintapalvelu.dao;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.model.Template;

public interface TemplateDAO extends JpaDAO<Template, Long> {

	public Template findTemplateByName(String name, String language);
}
