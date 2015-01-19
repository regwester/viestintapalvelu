/**
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
 **/
package fi.vm.sade.viestintapalvelu.template.impl;

import static com.google.common.base.Optional.fromNullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import fi.vm.sade.viestintapalvelu.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.lowagie.text.DocumentException;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.common.util.OptionalHelper;
import fi.vm.sade.viestintapalvelu.common.util.impl.BeanValidatorImpl;
import fi.vm.sade.viestintapalvelu.dao.DraftDAO;
import fi.vm.sade.viestintapalvelu.dao.StructureDAO;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.model.ContentReplacement;
import fi.vm.sade.viestintapalvelu.model.ContentStructure;
import fi.vm.sade.viestintapalvelu.model.Draft;
import fi.vm.sade.viestintapalvelu.model.DraftReplacement;
import fi.vm.sade.viestintapalvelu.model.Replacement;
import fi.vm.sade.viestintapalvelu.model.Structure;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.model.TemplateApplicationPeriod;
import fi.vm.sade.viestintapalvelu.model.TemplateContent;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.structure.StructureService;

@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {
    private static final Logger log = LoggerFactory.getLogger(TemplateServiceImpl.class);

    private CurrentUserComponent currentUserComponent;
    private TemplateDAO templateDAO;
    private DraftDAO draftDAO;
    private StructureDAO structureDAO;
    private StructureService structureService;
    private StructureConverter structureConverter;
    private TemplatesByApplicationPeriodConverter templatesByApplicationPeriodconverter;

    @Autowired
    public TemplateServiceImpl(TemplateDAO templateDAO, CurrentUserComponent currentUserComponent, DraftDAO draftDAO, StructureDAO structureDAO,
            StructureService structureService, StructureConverter structureConverter,
            TemplatesByApplicationPeriodConverter templatesByApplicationPeriodConverter) {
        this.templateDAO = templateDAO;
        this.currentUserComponent = currentUserComponent;
        this.draftDAO = draftDAO;
        this.structureDAO = structureDAO;
        this.structureService = structureService;
        this.structureConverter = structureConverter;
        this.templatesByApplicationPeriodconverter = templatesByApplicationPeriodConverter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateNamesList
     * ()
     */
    @Override
    public List<String> getTemplateNamesList() {
        return getTemplateNamesListByState(State.julkaistu);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#
     * getTemplateNamesListByState
     * (fi.vm.sade.viestintapalvelu.model.Template.State)
     */
    @Override
    public List<String> getTemplateNamesListByState(State state) {
        return templateDAO.getAvailableTemplatesByType(state);
    }

    protected long storeTemplate(Template template) {
        Henkilo henkilo = currentUserComponent.getCurrentUser();
        template.setStoringOid(henkilo.getOidHenkilo());
        long templateId = templateDAO.insert(template).getId();
        ensureNoOtherDefaults(template);
        return templateId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fi.vm.sade.viestintapalvelu.template.TemplateService#storeTemplateDTO
     * (fi.vm.sade.viestintapalvelu.template.Template)
     */
    @Override
    public long storeTemplateDTO(fi.vm.sade.viestintapalvelu.template.Template template) {
        Template model = new Template();
        model.setState(fromNullable(template.getState()).or(State.julkaistu));
        if (model.getState() == State.suljettu) {
            throw BeanValidatorImpl.badRequest("Storing a new template in closed (suljettu) state makes no sense, "
                    + "does it? Allowed states: draft (luonnos) or published (julkaistu, by default).");
        }
        convertTemplate(template, model, false);
        setTemplateStructure(template, model);
        updateApplicationPeriodRelation(template.getApplicationPeriods(), model);
        return storeTemplate(model);
    }

    private void setTemplateStructure(fi.vm.sade.viestintapalvelu.template.Template template, Template model) {
        if (template.getStructure() != null) {
            // new structure:
            long structureId = structureService.storeStructure(template.getStructure());
            template.setStructureId(structureId);
            model.setStructure(structureDAO.read(structureId));
        } else if (template.getStructureId() != null) {
            // When called upon update, does not require structure being defined
            // (in which case leaves it to existing one
            // against which only validation is done)
            model.setStructure(structureDAO.read(template.getStructureId()));
        } else if (template.getStructureName() != null && template.getLanguage() != null) {
            // If structure name specified, attach to latest structure with the
            // given name (and Template's language):
            model.setStructure(structureDAO.findLatestStructrueByNameAndLanguage(template.getStructureName(), template.getLanguage()).orNull());
        }
        // We should have a structure by now:
        if (model.getStructure() == null) {
            throw BeanValidatorImpl.badRequest("Structure required. Please specify structureId / structureName " + "or structure to create.");
        }
        validateTemplateAgainstStructure(template, model.getStructure());
    }

    private void convertTemplate(fi.vm.sade.viestintapalvelu.template.Template from, Template to, boolean isUpdate) {
        to.setName(from.getName());
        to.setDescription(from.getDescription());
        to.setTimestamp(new Date());
        to.setUsedAsDefault(from.isUsedAsDefault());
        to.setLanguage(from.getLanguage());
        to.setOrganizationOid(from.getOrganizationOid());
        if (from.getReplacements() != null) {
            if (isUpdate) {
                mapReplacements(from, to);
            } else {
                to.setReplacements(parseReplacementModels(from.getReplacements(), to, isUpdate));
            }
        }
    }

    private void mapReplacements(fi.vm.sade.viestintapalvelu.template.Template from, Template to) {
        for (fi.vm.sade.viestintapalvelu.template.Replacement replacement : from.getReplacements()) {
            final String name = replacement.getName();
            Optional<Replacement> repl = Iterables.tryFind(to.getReplacements(), new Predicate<Replacement>() {

                @Override
                public boolean apply(Replacement input) {
                    return name.equalsIgnoreCase(input.getName());
                }
            });
            if (repl.isPresent()) {
                Replacement toAdd = repl.get();
                to.getReplacements().remove(toAdd);
                toAdd.setDefaultValue(replacement.getDefaultValue());
                to.addReplacement(toAdd);
            } else {
                Replacement model = new Replacement();
                model.setDefaultValue(model.getDefaultValue());
                model.setMandatory(replacement.isMandatory());
                model.setName(replacement.getName());
                model.setTimestamp(new Date());
                model.setTemplate(to);
                to.addReplacement(model);
            }
        }
    }

    private void validateTemplateAgainstStructure(fi.vm.sade.viestintapalvelu.template.Template template, Structure structure) {
        if (!structure.getLanguage().equalsIgnoreCase(template.getLanguage())) {
            throw BeanValidatorImpl.badRequest("Template language " + template.getLanguage() + " differs from " + " structure (id=" + structure.getId()
                    + ") language " + structure.getLanguage());
        }
        for (ContentReplacement contentReplacement : structure.getReplacements()) {
            fi.vm.sade.viestintapalvelu.template.Replacement replacement = getReplacement(contentReplacement.getKey(), template.getReplacements());
            if (replacement == null) {
                throw BeanValidatorImpl.badRequest("Template does not contain replacement " + contentReplacement.getKey());
            }
            // Content validated against content type?
        }
    }

    private fi.vm.sade.viestintapalvelu.template.Replacement getReplacement(String key, List<fi.vm.sade.viestintapalvelu.template.Replacement> replacements) {
        for (fi.vm.sade.viestintapalvelu.template.Replacement replacement : replacements) {
            if (key.equals(replacement.getName())) {
                return replacement;
            }
        }
        return null;
    }

    @Override
    public fi.vm.sade.viestintapalvelu.template.Template saveAttachedApplicationPeriods(ApplicationPeriodsAttachDto dto) {
        Template model = templateDAO.read(dto.getTemplateId());
        updateApplicationPeriodRelation(dto.getApplicationPeriods(), model);
        model.setUsedAsDefault(dto.isUseAsDefault());
        templateDAO.update(model);
        ensureNoOtherDefaults(model);

        return convertBasicData(model, new fi.vm.sade.viestintapalvelu.template.Template());
    }

    protected void ensureNoOtherDefaults(Template model) {
        if (model.isUsedAsDefault()) {
            // Can not have multiple defaults for the templates with the same
            // name and language:
            List<Template> templates = templateDAO.findTemplates(new TemplateCriteriaImpl(model.getName(), model.getLanguage()).withState(null));
            for (Template otherTemplate : templates) {
                if (!otherTemplate.getId().equals(model.getId()) && otherTemplate.isUsedAsDefault()) {
                    otherTemplate.setUsedAsDefault(false);
                    templateDAO.update(otherTemplate);
                }
            }
        }
    }

    private void updateApplicationPeriodRelation(List<String> applicationPeriods, Template to) {
        if (applicationPeriods != null) {
            for (TemplateApplicationPeriod period : to.getApplicationPeriods()) {
                templateDAO.remove(period);
            }
            to.getApplicationPeriods().clear();
            templateDAO.update(to);

            for (String applicationPeriod : applicationPeriods) {
                if (applicationPeriod != null && applicationPeriod.length() > 0) {
                    TemplateApplicationPeriod period = new TemplateApplicationPeriod(to, applicationPeriod);
                    to.getApplicationPeriods().add(period);
                }
            }
        }
    }

    @Override
    public void storeDraftDTO(fi.vm.sade.viestintapalvelu.template.Draft draft) {
        Draft model = new Draft();
        model.setTemplateName(draft.getTemplateName());
        model.setTemplateLanguage(draft.getLanguageCode());

        model.setStoringOid(currentUserComponent.getCurrentUser().getOidHenkilo());

        model.setOrganizationOid((draft.getOrganizationOid() == null) ? "" : draft.getOrganizationOid());
        model.setApplicationPeriod((draft.getApplicationPeriod() == null) ? "" : draft.getApplicationPeriod()); // Haku
        model.setFetchTarget((draft.getFetchTarget() == null) ? "" : draft.getFetchTarget()); // Hakukohde
                                                                                              // id
        model.setTag((draft.getTag() == null) ? "" : draft.getTag()); // Vapaa
                                                                      // teksti
                                                                      // tunniste
        model.setTimestamp(new Date());
        model.setReplacements(parseDraftReplacementsModels(draft, model));

        draftDAO.insert(model);
    }
    
    @Override
    public void updateDraft(fi.vm.sade.viestintapalvelu.template.Draft draft) {
        if (draft.getDraftId() == null) {
            throw new IllegalArgumentException("Must provide id for draft being updated");
        }
        Draft model = draftDAO.read(draft.getDraftId());
        model.setReplacements(parseDraftReplacementsModels(draft, model));
        draftDAO.update(model);        
    }

    /*
     * kirjeet.luonnoskorvauskentat
     */
    private Set<DraftReplacement> parseDraftReplacementsModels(fi.vm.sade.viestintapalvelu.template.Draft draft, Draft draftB) {
        Set<DraftReplacement> replacements = new HashSet<DraftReplacement>();

        Object replKeys[] = draft.getReplacements().keySet().toArray();
        Object replVals[] = draft.getReplacements().values().toArray();

        for (int i = 0; i < replVals.length; i++) {
            fi.vm.sade.viestintapalvelu.model.DraftReplacement repl = new fi.vm.sade.viestintapalvelu.model.DraftReplacement();

            repl.setName(replKeys[i].toString());
            repl.setDefaultValue(replVals[i].toString());

            repl.setTimestamp(new Date());
            repl.setDraft(draftB);
            replacements.add(repl);
        }
        return replacements;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#findById(long)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template findById(long id, ContentStructureType structureType) {
        return findByIdAndState(id, structureType, State.julkaistu);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#findById(long)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template findByIdForEditing(long id, State state) {
        Template searchResult = state == null ? templateDAO.read(id) : templateDAO.findByIdAndState(id, state);
        if (searchResult == null) {
            throw new NotFoundException("Template not found by id: " + id + " and state=" + state);
        }
        return convertForEditing(searchResult);
    }

    private fi.vm.sade.viestintapalvelu.template.Template convertForEditing(Template template) {
        fi.vm.sade.viestintapalvelu.template.Template result = convert(template);
        result.setState(null); // julkaistu becomes the default when saving
                               // again.
        result.setStructure(structureService.getStructureForEditing(result.getStructureId()));
        return result;
    }

    @Override
    public fi.vm.sade.viestintapalvelu.template.Template findByIdForEditing(TemplateCriteria criteria) {
        Template template = findTemplate(criteria);
        if (template == null) {
            throw new NotFoundException("No template found.");
        }
        return convertForEditing(template);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#findById(long)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template findByIdAndState(long id, ContentStructureType structureType, State state) {
        Template searchResult = state == null ? templateDAO.read(id) : templateDAO.findByIdAndState(id, state);
        fi.vm.sade.viestintapalvelu.template.Template result = convert(searchResult);
        convertContent(searchResult, result, structureType);
        return result;
    }

    private fi.vm.sade.viestintapalvelu.template.Template convert(Template searchResult) {
        fi.vm.sade.viestintapalvelu.template.Template result = new fi.vm.sade.viestintapalvelu.template.Template();
        result.setId(searchResult.getId());
        result.setName(searchResult.getName());
        result.setStructureId(searchResult.getStructure().getId());
        result.setStructureName(searchResult.getStructure().getName());
        result.setLanguage(searchResult.getLanguage());
        result.setTimestamp(searchResult.getTimestamp());
        result.setReplacements(parseReplacementDTOs(searchResult.getReplacements()));
        result.setState(searchResult.getState());
        result.setUsedAsDefault(searchResult.isUsedAsDefault());
        result.setStoringOid(searchResult.getStoringOid());
        convertApplicationPeriods(searchResult, result);
        return result;
    }

    @Deprecated
    private List<fi.vm.sade.viestintapalvelu.template.TemplateContent> parseContentDTOs(Set<TemplateContent> contents) {
        List<fi.vm.sade.viestintapalvelu.template.TemplateContent> result = new ArrayList<fi.vm.sade.viestintapalvelu.template.TemplateContent>();

        for (TemplateContent tc : contents) {
            fi.vm.sade.viestintapalvelu.template.TemplateContent dto = new fi.vm.sade.viestintapalvelu.template.TemplateContent();
            dto.setContent(tc.getContent());
            dto.setId(tc.getId());
            dto.setName(tc.getName());
            dto.setOrder(tc.getOrder());
            dto.setTimestamp(tc.getTimestamp());
            result.add(dto);
        }
        Collections.sort(result);
        return result;
    }

    private List<fi.vm.sade.viestintapalvelu.template.Replacement> parseReplacementDTOs(Set<Replacement> replacements) {
        List<fi.vm.sade.viestintapalvelu.template.Replacement> result = new ArrayList<fi.vm.sade.viestintapalvelu.template.Replacement>();
        for (Replacement r : replacements) {
            fi.vm.sade.viestintapalvelu.template.Replacement dto = new fi.vm.sade.viestintapalvelu.template.Replacement();
            dto.setDefaultValue(r.getDefaultValue());
            dto.setId(r.getId());
            dto.setMandatory(r.isMandatory());
            dto.setName(r.getName());
            dto.setTimestamp(r.getTimestamp());
            result.add(dto);
        }

        return result;
    }

    @Deprecated
    private Set<TemplateContent> parseContentModels(List<fi.vm.sade.viestintapalvelu.template.TemplateContent> contents, Template template) {
        Set<TemplateContent> result = new HashSet<TemplateContent>();

        for (fi.vm.sade.viestintapalvelu.template.TemplateContent tc : contents) {
            TemplateContent model = new TemplateContent();
            model.setContent(tc.getContent());
            // model.setId(tc.getId());
            model.setName(tc.getName());
            model.setOrder(tc.getOrder());
            model.setTimestamp(new Date()); // tc.getTimestamp());
            model.setTemplate(template);
            result.add(model);
        }
        return result;
    }

    private Set<Replacement> parseReplacementModels(List<fi.vm.sade.viestintapalvelu.template.Replacement> replacements, Template template, boolean isUpdate) {
        Set<Replacement> result = new HashSet<Replacement>();
        for (fi.vm.sade.viestintapalvelu.template.Replacement r : replacements) {
            Replacement model = new Replacement();
            model.setDefaultValue(r.getDefaultValue());
            if (isUpdate) {
                model.setId(r.getId());
            }
            model.setMandatory(r.isMandatory());
            model.setName(r.getName());
            model.setTimestamp(new Date()); // r.getTimestamp());
            model.setTemplate(template);
            result.add(model);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fi.vm.sade.viestintapalvelu.template.TemplateService#template(java.lang
     * .String, java.lang.String)
     */
    @Override
    public Template template(String name, String languageCode) throws IOException, DocumentException {
        return template(name, languageCode, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fi.vm.sade.viestintapalvelu.template.TemplateService#template(java.lang
     * .String, java.lang.String, java.lang.String)
     */
    @Override
    public Template template(String name, String languageCode, String type) throws IOException, DocumentException {

        Template result = new Template();
        String templateName = Utils.resolveTemplateName(name, languageCode, type);
        BufferedReader buff = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(templateName)));
        StringBuilder sb = new StringBuilder();

        String line = buff.readLine();
        while (line != null) {
            sb.append(line);
            line = buff.readLine();
        }

        TemplateContent content = new TemplateContent();
        content.setName(templateName);
        content.setContent(sb.toString());
        List<TemplateContent> contents = new ArrayList<TemplateContent>();
        contents.add(content);

        Replacement replacement = new Replacement();
        replacement.setName("$sisalto");
        ArrayList<Replacement> rList = new ArrayList<Replacement>();
        rList.add(replacement);
        return result;
    }

    @Override
    public List<fi.vm.sade.viestintapalvelu.template.Template> listTemplateVersionsByName(TemplateCriteria templateCriteria, boolean content, boolean periods) {
        List<Template> templates = templateDAO.findTemplates(templateCriteria);

        List<fi.vm.sade.viestintapalvelu.template.Template> dtos = new ArrayList<fi.vm.sade.viestintapalvelu.template.Template>();
        for (Template template : templates) {
            fi.vm.sade.viestintapalvelu.template.Template dto = convertBasicData(template, new fi.vm.sade.viestintapalvelu.template.Template());
            if (periods) {
                convertApplicationPeriods(template, dto);
            }
            if (content) {
                convertReplacements(template, dto);
                convertContent(template, dto, templateCriteria.getType());
            }
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<fi.vm.sade.viestintapalvelu.template.Template> findByCriteria(TemplateCriteria criteria) {
        List<fi.vm.sade.viestintapalvelu.template.Template> templates = new ArrayList<fi.vm.sade.viestintapalvelu.template.Template>();
        for (Template t : templateDAO.findTemplates(criteria)) {
            fi.vm.sade.viestintapalvelu.template.Template convertedTemplate = getConvertedTemplate(t, true, false);
            templates.add(convertedTemplate);
        }
        return templates;
    }

    @Override
    public List<fi.vm.sade.viestintapalvelu.template.Template> findByOrganizationOIDs(List<String> oids) {
        List<Template> templates = templateDAO.findByOrganizationOIDs(oids);
        List<fi.vm.sade.viestintapalvelu.template.Template> convertedTemplates = new ArrayList<fi.vm.sade.viestintapalvelu.template.Template>();
        for (Template template : templates) {
            fi.vm.sade.viestintapalvelu.template.Template convertedTemplate = getConvertedTemplate(template, true, false);
            convertedTemplates.add(convertedTemplate);
        }
        return convertedTemplates;
    }

    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(TemplateCriteria criteria, boolean content) {
        Template template = findTemplate(criteria);
        if (template == null) {
            return null;
        }

        fi.vm.sade.viestintapalvelu.template.Template searchTempl = new fi.vm.sade.viestintapalvelu.template.Template();
        convertBasicData(template, searchTempl);
        convertReplacements(template, searchTempl);
        if (content) {
            convertContent(template, searchTempl, criteria.getType());
        }
        convertApplicationPeriods(template, searchTempl);
        searchTempl.setState(template.getState());
        return searchTempl;
    }

    private Template findTemplate(TemplateCriteria criteria) {
        if (criteria.getName() == null) {
            return null;
        }

        Template template;
        if (criteria.getApplicationPeriod() != null) {
            // First look-up with specified criteria with application period
            template = templateDAO.findTemplate(criteria);
            if (template == null) {
                // If not found, try the default-flagged without application
                // period
                template = resolveTemplatePreferringDefault(criteria.withApplicationPeriod(null));
            }
        } else {
            template = resolveTemplatePreferringDefault(criteria);
        }
        if (template == null) {
            return null;
        }
        return template;
    }

    private Template resolveTemplatePreferringDefault(TemplateCriteria criteria) {
        // First look-up for flagged default:
        Template template = templateDAO.findTemplate(criteria.withDefaultRequired());
        if (template == null) {
            // and fall back to last:
            template = templateDAO.findTemplate(criteria.withoutDefaultRequired());
        }
        return template;
    }

    // TODO: move to separate DTO converter:
    private fi.vm.sade.viestintapalvelu.template.Template getConvertedTemplate(Template from, boolean convertApplicationPeriods, boolean convertReplacements) {
        fi.vm.sade.viestintapalvelu.template.Template template = convertBasicData(from, new fi.vm.sade.viestintapalvelu.template.Template());

        if (convertApplicationPeriods) {
            template = convertApplicationPeriods(from, template);
        }

        if (convertReplacements) {
            template = convertReplacements(from, template);
        }
        return template;
    }

    // TODO: move to separate DTO converter:
    private fi.vm.sade.viestintapalvelu.template.Template convertBasicData(Template from, fi.vm.sade.viestintapalvelu.template.Template to) {
        to.setId(from.getId());
        to.setName(from.getName());
        to.setStructureId(from.getStructure().getId());
        to.setStructureName(from.getStructure().getName());
        to.setUsedAsDefault(from.isUsedAsDefault());
        // searchTempl.setStyles(template.getStyles());
        to.setLanguage(from.getLanguage());
        to.setTimestamp(from.getTimestamp());
        to.setStoringOid(from.getStoringOid());
        to.setOrganizationOid(from.getOrganizationOid());
        to.setTemplateVersio(from.getVersionro());
        to.setDescription(from.getDescription());
        to.setState(from.getState());
        return to;
    }

    // TODO: move to separate DTO converter:
    private fi.vm.sade.viestintapalvelu.template.Template convertApplicationPeriods(Template from, fi.vm.sade.viestintapalvelu.template.Template to) {
        List<String> periods = new ArrayList<String>();
        for (TemplateApplicationPeriod applicationPeriod : from.getApplicationPeriods()) {
            periods.add(applicationPeriod.getId().getApplicationPeriod());
        }
        Collections.sort(periods);
        to.setApplicationPeriods(periods);
        return to;
    }

    // TODO: move to separate DTO converter:
    private fi.vm.sade.viestintapalvelu.template.Template convertReplacements(Template from, fi.vm.sade.viestintapalvelu.template.Template to) {
        // Replacement
        List<fi.vm.sade.viestintapalvelu.template.Replacement> replacement = new LinkedList<fi.vm.sade.viestintapalvelu.template.Replacement>();
        for (Replacement rep : from.getReplacements()) {
            fi.vm.sade.viestintapalvelu.template.Replacement repl = new fi.vm.sade.viestintapalvelu.template.Replacement();
            repl.setId(rep.getId());
            repl.setName(rep.getName());
            ;
            repl.setDefaultValue(rep.getDefaultValue());
            ;
            repl.setMandatory(rep.isMandatory());
            repl.setTimestamp(rep.getTimestamp());
            replacement.add(repl);
        }
        to.setReplacements(replacement);
        return to;
    }

    private fi.vm.sade.viestintapalvelu.template.Template convertContent(Template from, fi.vm.sade.viestintapalvelu.template.Template to,
            ContentStructureType structureType) {
        Structure structure = from.getStructure();
        ContentStructure contentStructure = contentStructure(structure, structureType).or(
                OptionalHelper.<ContentStructure> notFound("Template id=" + from.getId() + " does not have ContentStructure for type=" + structureType));
        to.setStyles(contentStructure.getStyle() != null ? contentStructure.getStyle().getStyle() : null);
        to.setContents(structureConverter.toContents(contentStructure));
        to.setType(contentStructure.getType().name());
        return to;
    }

    private Optional<ContentStructure> contentStructure(Structure structure, ContentStructureType structureType) {
        for (ContentStructure contentStructure : structure.getContentStructures()) {
            if (contentStructure.getType().equals(structureType)) {
                return Optional.of(contentStructure);
            }
        }
        return Optional.absent();
    }

    /* ------------------------- */
    /* - findDraftByNameOrgTag - */
    /* ------------------------- */

    /**
     * Method findDraftByNameOrgTag
     *
     * @param templateName
     * @param languageCode
     * @param oid
     * @param applicationPeriod
     * @param fetchTarget
     * @param tag
     * @return Draft
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Draft findDraftByNameOrgTag(String templateName, String languageCode, String oid, String applicationPeriod,
            String fetchTarget, String tag) {

        fi.vm.sade.viestintapalvelu.template.Draft result = new fi.vm.sade.viestintapalvelu.template.Draft();
        Draft draft = draftDAO.findDraftByNameOrgTag(templateName, languageCode, oid, applicationPeriod, fetchTarget, tag);
        return getConvertedDraft(draft);
    }

    private fi.vm.sade.viestintapalvelu.template.Draft getConvertedDraft(Draft draft) {
        fi.vm.sade.viestintapalvelu.template.Draft result = new fi.vm.sade.viestintapalvelu.template.Draft();
        if (draft != null) {
            // kirjeet.luonnos
            result.setDraftId(draft.getId());
            result.setTemplateName(draft.getTemplateName());
            result.setLanguageCode(draft.getTemplateLanguage());
            result.setStoringOid(draft.getStoringOid());
            result.setOrganizationOid(draft.getOrganizationOid());

            result.setApplicationPeriod(draft.getApplicationPeriod());
            result.setFetchTarget(draft.getFetchTarget());
            result.setTag(draft.getTag());

            // kirjeet.luonnoskorvauskentat
            result.setReplacements(parseDraftReplDTOs(draft.getReplacements()));
        }

        return result;
    }

    private Map<String, Object> parseDraftReplDTOs(Set<DraftReplacement> draftReplacements) {
        Map<String, Object> replacements = new HashMap<String, Object>();

        for (DraftReplacement draftRepl : draftReplacements) {
            replacements.put(draftRepl.getName(), draftRepl.getDefaultValue());
        }
        return replacements;
    }

    /* ------------------------ */
    /* - findDraftReplacement - */
    /* ------------------------ */
    @Override
    public List<fi.vm.sade.viestintapalvelu.template.Replacement> findDraftReplacement(String templateName, String languageCode, String oid,
            String applicationPeriod, String fetchTarget, String tag) {

        List<fi.vm.sade.viestintapalvelu.template.Replacement> replacements = new LinkedList<fi.vm.sade.viestintapalvelu.template.Replacement>();

        Draft draft = draftDAO.findDraftByNameOrgTag(templateName, languageCode, oid, applicationPeriod, fetchTarget, tag);

        if (draft != null) {

            // kirjeet.luonnoskorvauskentat
            for (DraftReplacement draftRepl : draft.getReplacements()) {
                fi.vm.sade.viestintapalvelu.template.Replacement repl = new fi.vm.sade.viestintapalvelu.template.Replacement();
                repl.setId(draftRepl.getId());
                repl.setName(draftRepl.getName());
                repl.setDefaultValue(draftRepl.getDefaultValue());
                repl.setMandatory(draftRepl.isMandatory());
                repl.setTimestamp(draftRepl.getTimestamp());

                replacements.add(repl);
            }
        }

        return replacements;
    }

    @Override
    public List<fi.vm.sade.viestintapalvelu.template.Draft> getDraftsByOrgOidsAndApplicationPeriod(List<String> oids, String applicationPeriod) {
        List<Draft> drafts = draftDAO.findByOrgOidsAndApplicationPeriod(oids, applicationPeriod);
        List<fi.vm.sade.viestintapalvelu.template.Draft> convertedDrafts = new ArrayList<>();
        for (Draft draft : drafts) {
            convertedDrafts.add(getConvertedDraft(draft));
        }
        return convertedDrafts;
    }

    @Override
    public void updateTemplate(fi.vm.sade.viestintapalvelu.template.Template template) {
        Template model = templateDAO.read(template.getId());
        final State newState = template.getState();
        final State oldState = model.getState();
        verifyState(oldState, newState);
        final boolean editingAllowed = oldState == State.luonnos && newState != State.suljettu;
        if (editingAllowed) {
            convertTemplate(template, model, true);
            setTemplateStructure(template, model);
            updateApplicationPeriodRelation(template.getApplicationPeriods(), model);
        }
        model.setState(newState);
        templateDAO.update(model);
        if (editingAllowed) {
            ensureNoOtherDefaults(model);
        }
    }

    @Override
    public TemplatesByApplicationPeriod findByApplicationPeriod(String applicationPeriod) {
        TemplateCriteria criteria = new TemplateCriteriaImpl().withApplicationPeriod(applicationPeriod);
        List<Template> publisheds = templateDAO.findTemplates(criteria.withState(State.julkaistu));
        List<Template> drafts = templateDAO.findTemplates(criteria.withState(State.luonnos));
        List<Template> closeds = templateDAO.findTemplates(criteria.withState(State.suljettu));
        return templatesByApplicationPeriodconverter.convert(applicationPeriod, publisheds, drafts, closeds);
    }

    private void verifyState(State oldState, State newState) {
        if (oldState == State.suljettu && newState != State.julkaistu) {
            throw new IllegalArgumentException("Updating closed template to anything other than published is not supported");
        }
        if (oldState == State.julkaistu && newState != State.suljettu) {
            throw new IllegalArgumentException("Published template can only be closed via update");
        }
    }

    @Override
    public List<TemplateInfo> findTemplateInfoByCriteria(TemplateCriteria criteria) {
        List<Template> templates = templateDAO.findTemplates(criteria);
        return templatesByApplicationPeriodconverter.convert(templates);
    }

    @Override
    public List<TemplateListing> getTemplateIdsAndApplicationPeriodNames() {
        return templateDAO.getTemplateIdsAndApplicationPeriodNames();
    }

}
