/*
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 */
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

    Draft findDraftByNameOrgTag(String templateName, String templateLanguage, String organizationOid, String applicationPeriod, String fetchTarget, String tag);

    List<fi.vm.sade.viestintapalvelu.template.Replacement> findDraftReplacement(String templateName, String languageCode, String oid, String applicationPeriod,
            String fetchTarget, String tag);

    Template findById(long id, ContentStructureType structureType);

    /*
     * (non-Javadoc)
     * 
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#findById(long)
     */
    Template findByIdForEditing(TemplateCriteria criteria);

    /*
     * (non-Javadoc)
     * 
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#findById(long)
     */
    Template findByIdForEditing(long id, State state);

    Template findByIdAndState(long id, ContentStructureType structureType, State state);

    fi.vm.sade.viestintapalvelu.model.Template template(String name, String languageCode) throws IOException, DocumentException;

    fi.vm.sade.viestintapalvelu.model.Template template(String name, String languageCode, String type) throws IOException, DocumentException;

    Template getTemplateByName(TemplateCriteria criteria, boolean content);

    List<Template> listTemplateVersionsByName(TemplateCriteria templateCriteria, boolean content, boolean periods);

    List<Template> findByCriteria(TemplateCriteria criteria);

    TemplatesByApplicationPeriod findByApplicationPeriod(String applicationPeriod);

    List<Template> findByOrganizationOIDs(List<String> oids);

    List<TemplateInfo> findTemplateInfoByCriteria(TemplateCriteria withState);

    List<Draft> getDraftsByOrgOidsAndApplicationPeriod(List<String> oids, String applicationPeriod);

    List<TemplateListing> getTemplateIdsAndApplicationPeriodNames();

    List<Draft> getDraftsByTags(List<String> tags);

    void updateDraft(DraftUpdateDTO draft);
}