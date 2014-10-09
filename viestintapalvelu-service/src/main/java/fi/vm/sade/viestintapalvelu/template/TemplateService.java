package fi.vm.sade.viestintapalvelu.template;

import com.lowagie.text.DocumentException;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.model.Template;

import java.io.IOException;
import java.util.List;

public interface TemplateService {

    Template getTemplateFromFiles(String languageCode, String... names) throws IOException;

    Template getTemplateFromFiles(String languageCode, String type, String... names) throws IOException;

    List<String> getTemplateNamesList();

    void storeTemplate(Template template);

    void storeTemplateDTO(fi.vm.sade.viestintapalvelu.template.Template template);

    fi.vm.sade.viestintapalvelu.template.Template saveAttachedApplicationPeriods(ApplicationPeriodsAttachDto dto);

    void storeDraftDTO(fi.vm.sade.viestintapalvelu.template.Draft draft);


    fi.vm.sade.viestintapalvelu.template.Draft findDraftByNameOrgTag(String templateName, String templateLanguage, String organizationOid,
                                                                     String applicationPeriod, String fetchTarget, String tag);

    List<fi.vm.sade.viestintapalvelu.template.Replacement> findDraftReplacement(String templateName, String languageCode,
                                                                                String oid, String applicationPeriod, String fetchTarget, String tag);

    fi.vm.sade.viestintapalvelu.template.Template findById(long id);

    Template template(String name, String languageCode) throws IOException, DocumentException;

    Template template(String name, String languageCode, String type) throws IOException, DocumentException;

    fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language);

    fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, String type);

    fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, boolean content);

    fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, boolean content, String type);

    fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(TemplateCriteria criteria, boolean content);

    List<fi.vm.sade.viestintapalvelu.template.Template> listTemplateVersionsByName(TemplateCriteria templateCriteria, boolean content, boolean periods);
}