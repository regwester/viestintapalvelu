package fi.vm.sade.viestintapalvelu.template;

import java.io.IOException;
import java.util.List;

import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.model.Template;

public interface TemplateService {

	public Template getTemplateFromFiles(String languageCode, String... names) throws IOException;

	public Template getTemplateFromFiles(String languageCode, String type, String... names) throws IOException;

	public List<String> getTemplateNamesList();

	public void storeTemplate(Template template);

	public void storeTemplateDTO(fi.vm.sade.viestintapalvelu.template.Template template);

	public void storeDraftDTO(fi.vm.sade.viestintapalvelu.template.Draft draft);

	public fi.vm.sade.viestintapalvelu.template.Draft findDraftByNameOrgTag(String templateName,String templateLanguage, String organizationOid, 
			String applicationPeriod, String fetchTarget, String tag);    

	public fi.vm.sade.viestintapalvelu.template.Template findById(long id);

	public Template template(String name, String languageCode) throws IOException, DocumentException;

	public Template template(String name, String languageCode, String type) throws IOException, DocumentException;

	public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language);

	public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, String type);

	public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, boolean content);

	public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, boolean content, String type);

}