package fi.vm.sade.viestintapalvelu.template;

import java.io.IOException;
import java.util.List;

import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;

public interface TemplateService {

    List<String> getTemplateNamesList();
    
    List<String> getTemplateNamesListByState(State state);

    long storeTemplateDTO(Template template);
    
    void updateTemplate(Template template);

    Template saveAttachedApplicationPeriods(ApplicationPeriodsAttachDto dto);

    void storeDraftDTO(Draft draft);


    Draft findDraftByNameOrgTag(String templateName, String templateLanguage, String organizationOid,
                                                                     String applicationPeriod, String fetchTarget, String tag);

    List<fi.vm.sade.viestintapalvelu.template.Replacement> findDraftReplacement(String templateName, String languageCode,
                                                                                String oid, String applicationPeriod, String fetchTarget, String tag);

    Template findById(long id, ContentStructureType structureType);

    Template findByIdAndState(long id, ContentStructureType structureType, State state);
    
    fi.vm.sade.viestintapalvelu.model.Template template(String name, String languageCode) throws IOException, DocumentException;

    fi.vm.sade.viestintapalvelu.model.Template template(String name, String languageCode, String type) throws IOException, DocumentException;

    Template getTemplateByName(TemplateCriteria criteria, boolean content);

    List<Template> listTemplateVersionsByName(TemplateCriteria templateCriteria, boolean content, boolean periods);

    List<Template> findByCriteria(TemplateCriteria criteria);
    
    TemplatesByApplicationPeriod findByApplicationPeriod(String applicationPeriod);

    List<Template> findByOrganizationOIDs(List<String> oids);
}