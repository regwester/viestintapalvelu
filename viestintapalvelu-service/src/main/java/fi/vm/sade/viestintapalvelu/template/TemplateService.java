package fi.vm.sade.viestintapalvelu.template;

import java.io.IOException;
import java.util.List;

import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.model.Template;

public interface TemplateService {

    public Template getTemplateFromFiles(String languageCode, String... names) throws IOException;

    public List<String> getTemplateNamesList();

    public void storeTemplate(Template template);

    public void storeTemplateDTO(fi.vm.sade.viestintapalvelu.template.Template template);

    public fi.vm.sade.viestintapalvelu.template.Template findById(long id);

    public Template template(String name, String languageCode) throws IOException, DocumentException;

    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language);

    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, boolean content);

}