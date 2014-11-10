package fi.vm.sade.viestintapalvelu.template.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.DocumentException;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.dao.DraftDAO;
import fi.vm.sade.viestintapalvelu.dao.StructureDAO;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.model.*;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.template.ApplicationPeriodsAttachDto;
import fi.vm.sade.viestintapalvelu.template.Contents;
import fi.vm.sade.viestintapalvelu.template.TemplateService;

@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {
    private CurrentUserComponent currentUserComponent;
    private TemplateDAO templateDAO;
    private DraftDAO draftDAO;
    private StructureDAO structureDAO;

    @Autowired
    public TemplateServiceImpl(TemplateDAO templateDAO, CurrentUserComponent currentUserComponent, DraftDAO draftDAO,
                               StructureDAO structureDAO) {
        this.templateDAO = templateDAO;
        this.currentUserComponent = currentUserComponent;
        this.draftDAO = draftDAO;
        this.structureDAO = structureDAO;
    }

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateFromFiles(java.lang.String, java.lang.String)
     */
    @Override
    public Template getTemplateFromFiles(String languageCode, String... names) throws IOException {
        return getTemplateFromFiles(languageCode, null, names);
    }

    /* (non-Javadoc)
 * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateFromFiles(java.lang.String, java.lang.String, java.lang.String)
 */
    @Override
    public Template getTemplateFromFiles(String languageCode, String type, String... names) throws IOException {
        Template result = new Template();
        Set<TemplateContent> contents = new HashSet<TemplateContent>();
        result.setLanguage(languageCode);
        result.setName("template");
        result.setUsedAsDefault(false);
        result.setOrganizationOid("oid_org");
        result.setStoringOid("storingOid");
        result.setTimestamp(new Date());
        result.setType(type);

        int i = 1;
        for (String name : names) {
            String templateName = Utils.resolveTemplateName(name, languageCode, type);
            if (templateName != null) {
                BufferedReader buff = new BufferedReader(new InputStreamReader(
                        getClass().getResourceAsStream(templateName)));
                StringBuilder sb = new StringBuilder();

                String line = buff.readLine();
                while (line != null) {
                    sb.append(line);
                    line = buff.readLine();
                }
                TemplateContent content = new TemplateContent();
                content.setName(name);
                content.setContent(sb.toString());
                content.setOrder(i++);
                content.setTimestamp(new Date());
                content.setTemplate(result);
                contents.add(content);
            }
        }
        result.setContents(contents);
        Replacement replacement = new Replacement();
        replacement.setName("$sisalto");
        replacement.setDefaultValue("default value");
        Set<Replacement> replacements = new HashSet<Replacement>();
        replacement.setTemplate(result);
        replacements.add(replacement);
        result.setReplacements(replacements);
        return result;
    }

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateNamesList()
     */
    @Override
    public List<String> getTemplateNamesList() {
        return getTemplateNamesListByState(State.julkaistu);
    }
    
    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateNamesListByState(fi.vm.sade.viestintapalvelu.model.Template.State)
     */
    @Override
    public List<String> getTemplateNamesListByState(State state) {
        return templateDAO.getAvailableTemplatesByType(state);
    }

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#storeTemplate(fi.vm.sade.viestintapalvelu.model.Template)
     */
    @Override
    public void storeTemplate(Template template) {
        Henkilo henkilo = currentUserComponent.getCurrentUser();
        template.setStoringOid(henkilo.getOidHenkilo());
        templateDAO.insert(template);
        ensureNoOtherDefaults(template);
    }

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#storeTemplateDTO(fi.vm.sade.viestintapalvelu.template.Template)
     */
    @Override
    public void storeTemplateDTO(fi.vm.sade.viestintapalvelu.template.Template template) {
        Template model = new Template();
        //model.setId(template.getId());
        model.setName(template.getName());
        model.setDescription(template.getDescription());
        model.setTimestamp(new Date()); //template.getTimestamp());
        model.setStyles(template.getStyles());
        model.setUsedAsDefault(template.isUsedAsDefault());
        model.setLanguage(template.getLanguage());
        model.setOrganizationOid(template.getOrganizationOid());
        model.setContents(parseContentModels(template.getContents(), model));
        model.setReplacements(parseReplacementModels(template.getReplacements(), model));
        model.setType(template.getType());
        if (template.getStructureId() != null) {
            model.setStructure(structureDAO.read(template.getStructureId()));
        } else if (template.getStructureName() != null
                && template.getLanguage() != null) {
            model.setStructure(structureDAO.findLatestStructrueByNameAndLanguage(template.getStructureName(),
                    template.getLanguage()).orNull());
        }
        updateApplicationPeriodRelation(template.getApplicationPeriods(), model);
        storeTemplate(model);
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
            // Can not have multiple defaults for the templates with the same name, language and type:
            List<Template> templates = templateDAO.findTemplates(
                    new TemplateCriteriaImpl(model.getName(), model.getLanguage())
                            .withType(model.getType()));
            for (Template otherTemplate : templates) {
                if (!otherTemplate.getId().equals(model.getId())
                        && otherTemplate.isUsedAsDefault()) {
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
        model.setApplicationPeriod((draft.getApplicationPeriod() == null) ? "" : draft.getApplicationPeriod());    // Haku
        model.setFetchTarget((draft.getFetchTarget() == null) ? "" : draft.getFetchTarget());                        // Hakukohde id
        model.setTag((draft.getTag() == null) ? "" : draft.getTag());                                                // Vapaa teksti tunniste
        model.setTimestamp(new Date());
        model.setReplacements(parseDraftReplacementsModels(draft, model));

        draftDAO.insert(model);
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


    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#findById(long)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template findById(long id) {
        Template searchResult = null;
        List<Template> templates = templateDAO.findBy("id", id);
        if (templates != null && !templates.isEmpty()) {
            searchResult = templates.get(0);
        }
        fi.vm.sade.viestintapalvelu.template.Template result = new fi.vm.sade.viestintapalvelu.template.Template();
        result.setId(searchResult.getId());
        result.setName(searchResult.getName());
        result.setStructureId(searchResult.getStructure().getId());
        result.setStructureName(searchResult.getStructure().getName());
        result.setLanguage(searchResult.getLanguage());
        result.setTimestamp(searchResult.getTimestamp());
        result.setStyles(searchResult.getStyles());
        result.setContents(parseContentDTOs(searchResult.getContents()));
        result.setReplacements(parseReplacementDTOs(searchResult.getReplacements()));
        result.setType(searchResult.getType());
        return result;
    }

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

    private Set<TemplateContent> parseContentModels(List<fi.vm.sade.viestintapalvelu.template.TemplateContent> contents, Template template) {
        Set<TemplateContent> result = new HashSet<TemplateContent>();

        for (fi.vm.sade.viestintapalvelu.template.TemplateContent tc : contents) {
            TemplateContent model = new TemplateContent();
            model.setContent(tc.getContent());
            //model.setId(tc.getId());
            model.setName(tc.getName());
            model.setOrder(tc.getOrder());
            model.setTimestamp(new Date()); //tc.getTimestamp());
            model.setTemplate(template);
            result.add(model);
        }
        return result;
    }

    private Set<Replacement> parseReplacementModels(List<fi.vm.sade.viestintapalvelu.template.Replacement> replacements, Template template) {
        Set<Replacement> result = new HashSet<Replacement>();
        for (fi.vm.sade.viestintapalvelu.template.Replacement r : replacements) {
            Replacement model = new Replacement();
            model.setDefaultValue(r.getDefaultValue());
            // model.setId(r.getId());
            model.setMandatory(r.isMandatory());
            model.setName(r.getName());
            model.setTimestamp(new Date()); //r.getTimestamp());
            model.setTemplate(template);
            result.add(model);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#template(java.lang.String, java.lang.String)
     */
    @Override
    public Template template(String name, String languageCode)
            throws IOException, DocumentException {
        return template(name, languageCode, null);
    }

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#template(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Template template(String name, String languageCode, String type)
            throws IOException, DocumentException {

        Template result = new Template();
        String templateName = Utils.resolveTemplateName(name, languageCode, type);
        BufferedReader buff = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(templateName)));
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

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateByName(java.lang.String, java.lang.String)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language) {
        return getTemplateByName(name, language, true, null);
    }

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateByName(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, String type) {
        return getTemplateByName(name, language, true, type);
    }

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateByName(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, boolean content) {
        return getTemplateByName(name, language, content, null);
    }

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateByName(java.lang.String, java.lang.String, boolean, java.lang.String)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, boolean content, String type) {
        return getTemplateByName(new TemplateCriteriaImpl(name, language).withType(type), content);
    }

    @Override
    public List<fi.vm.sade.viestintapalvelu.template.Template> listTemplateVersionsByName(
            TemplateCriteria templateCriteria, boolean content, boolean periods) {
        List<Template> templates = templateDAO.findTemplates(templateCriteria);

        List<fi.vm.sade.viestintapalvelu.template.Template> dtos = new ArrayList<fi.vm.sade.viestintapalvelu.template.Template>();
        for (Template template : templates) {
            fi.vm.sade.viestintapalvelu.template.Template dto = convertBasicData(template,
                    new fi.vm.sade.viestintapalvelu.template.Template());
            if (periods) {
                convertApplicationPeriods(template, dto);
            }
            if (content) {
                convertReplacements(template, dto);
                convertContent(template, dto, templateCriteria);
            }
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<fi.vm.sade.viestintapalvelu.template.Template> getByApplicationPeriod(TemplateCriteria criteria) {
        List<Template> templates = templateDAO.findTemplates(criteria);
        List<fi.vm.sade.viestintapalvelu.template.Template> convertedTemplates = new ArrayList<fi.vm.sade.viestintapalvelu.template.Template>(templates.size());
        for (Template template : templates) {
            convertedTemplates.add(getConvertedTemplate(template));
        }
        return convertedTemplates;
    }

    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(TemplateCriteria criteria, boolean content) {
        fi.vm.sade.viestintapalvelu.template.Template searchTempl = new fi.vm.sade.viestintapalvelu.template.Template();
        if (criteria.getName() == null) {
            return null;
        }

        Template template;
        if (criteria.getApplicationPeriod() != null) {
            // First look-up with specified criteria with application period
            template = templateDAO.findTemplate(criteria);
            if (template == null) {
                // If not found, try the default-flagged without application period
                template = resolveTemplatePreferringDefault(criteria
                        .withApplicationPeriod(null));
            }
        } else {
            template = resolveTemplatePreferringDefault(criteria);
        }
        if (template == null) {
            return null;
        }

        convertBasicData(template, searchTempl);
        convertReplacements(template, searchTempl);
        if (content) {
            convertContent(template, searchTempl, criteria);
        }
        return searchTempl;
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
    private fi.vm.sade.viestintapalvelu.template.Template getConvertedTemplate(Template from) {
        return convertBasicData(from, new fi.vm.sade.viestintapalvelu.template.Template());
    }


        // TODO: move to separate DTO converter:
    private fi.vm.sade.viestintapalvelu.template.Template convertBasicData(Template from, fi.vm.sade.viestintapalvelu.template.Template to) {
        to.setId(from.getId());
        to.setName(from.getName());
        to.setStructureId(from.getStructure().getId());
        to.setStructureName(from.getStructure().getName());
        to.setUsedAsDefault(from.isUsedAsDefault());
        //searchTempl.setStyles(template.getStyles());
        to.setLanguage(from.getLanguage());
        to.setTimestamp(from.getTimestamp());
        to.setStoringOid(from.getStoringOid());
        to.setOrganizationOid(from.getOrganizationOid());
        to.setTemplateVersio(from.getVersionro());
        to.setType(from.getType());
        to.setDescription(from.getDescription());
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

    // TODO: move to separate DTO converter:
    private fi.vm.sade.viestintapalvelu.template.Template convertContent(Template from, fi.vm.sade.viestintapalvelu.template.Template to, TemplateCriteria criteria) {
        // include style only with content
        to.setStyles(from.getStyles());

        List<fi.vm.sade.viestintapalvelu.template.TemplateContent> templateContent = new LinkedList<fi.vm.sade.viestintapalvelu.template.TemplateContent>();
        for (TemplateContent co : from.getContents()) {
            if (StringUtils.equalsIgnoreCase(criteria.getType(), Template.TYPE_EMAIL)) {
                // If type is email -> read only email content and subject from template
                // all other contents are ignored
                if (!StringUtils.equalsIgnoreCase(co.getName(), Contents.EMAIL_BODY)) {
                    continue;
                }
            } else {
                // If type is doc -> read all template contents by ignore email subject or email content
                if (StringUtils.equalsIgnoreCase(co.getName(), Contents.EMAIL_BODY)) {
                    continue;
                }
            }
            fi.vm.sade.viestintapalvelu.template.TemplateContent cont = new fi.vm.sade.viestintapalvelu.template.TemplateContent();
            cont.setId(co.getId());
            cont.setName(co.getName());
            cont.setContent(co.getContent());
            cont.setOrder(co.getOrder());
            cont.setTimestamp(co.getTimestamp());
            templateContent.add(cont);
        }
        to.setContents(templateContent);
        return to;
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
    public fi.vm.sade.viestintapalvelu.template.Draft findDraftByNameOrgTag(String templateName, String languageCode, String oid,
                                                                            String applicationPeriod, String fetchTarget, String tag) {

        fi.vm.sade.viestintapalvelu.template.Draft result = new fi.vm.sade.viestintapalvelu.template.Draft();

        Draft draft = draftDAO.findDraftByNameOrgTag(templateName, languageCode, oid, applicationPeriod, fetchTarget, tag);
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
    public List<fi.vm.sade.viestintapalvelu.template.Replacement> findDraftReplacement(String templateName, String languageCode,
                                                                                       String oid, String applicationPeriod, String fetchTarget, String tag) {

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


}
