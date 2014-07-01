package fi.vm.sade.viestintapalvelu.template.impl;

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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.DocumentException;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.dao.DraftDAO;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.model.Draft;
import fi.vm.sade.viestintapalvelu.model.DraftReplacement;
import fi.vm.sade.viestintapalvelu.model.Replacement;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.TemplateContent;
import fi.vm.sade.viestintapalvelu.template.TemplateService;

@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {
    private CurrentUserComponent currentUserComponent;
    private TemplateDAO templateDAO;
    private DraftDAO draftDAO;

    @Autowired
    public TemplateServiceImpl(TemplateDAO templateDAO, CurrentUserComponent currentUserComponent, DraftDAO draftDAO) {
        this.templateDAO = templateDAO;
        this.currentUserComponent = currentUserComponent;
        this.draftDAO = draftDAO;
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
        replacement.setName("$letterBodyText");
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
        return templateDAO.getAvailableTemplates();
    }
    
    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#storeTemplate(fi.vm.sade.viestintapalvelu.model.Template)
     */
    @Override
    public void storeTemplate(Template template) {
        Henkilo henkilo = currentUserComponent.getCurrentUser();
        template.setStoringOid(henkilo.getOidHenkilo());
        
        templateDAO.insert(template);
    }
    
    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#storeTemplateDTO(fi.vm.sade.viestintapalvelu.template.Template)
     */
    @Override
    public void storeTemplateDTO(fi.vm.sade.viestintapalvelu.template.Template template) {        
        Template model = new Template();
        //model.setId(template.getId());
        model.setName(template.getName());
        model.setTimestamp(new Date()); //template.getTimestamp());
        model.setStyles(template.getStyles());
        model.setLanguage(template.getLanguage());
        model.setOrganizationOid(template.getOrganizationOid());        
        model.setContents(parseContentModels(template.getContents(), model));
        model.setReplacements(parseReplacementModels(template.getReplacements(), model));
        model.setType(template.getType());
        storeTemplate(model);
    }
    
	@Override
	public void storeDraftDTO(fi.vm.sade.viestintapalvelu.template.Draft draft) {
		Draft model = new Draft();
		model.setTemplateName(draft.getTemplateName());
		model.setTemplateLanguage(draft.getLanguageCode());
		
		model.setStoringOid(currentUserComponent.getCurrentUser().getOidHenkilo());
		
		model.setOrganizationOid( (draft.getOrganizationOid()==null) ? "" : draft.getOrganizationOid() );
		model.setApplicationPeriod( (draft.getApplicationPeriod()==null) ? "" : draft.getApplicationPeriod());	// Haku
		model.setFetchTarget((draft.getFetchTarget()==null) ? "" : draft.getFetchTarget());						// Hakukohde id
		model.setTag((draft.getTag()==null) ? "" : draft.getTag());												// Vapaa teksti tunniste
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
            fi.vm.sade.viestintapalvelu.template.TemplateContent dto = new fi.vm.sade.viestintapalvelu.template.TemplateContent ();
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
            TemplateContent model = new TemplateContent ();
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
        replacement.setName("$letterBodyText");
        ArrayList<Replacement> rList = new ArrayList<Replacement>();
        rList.add(replacement);
        return result;
    }

    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateByName(java.lang.String, java.lang.String)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language)  {
       return getTemplateByName(name, language, true, null);
    } 
    
    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateByName(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, String type)  {
       return getTemplateByName(name, language, true, type);
    } 
    
    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateByName(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, boolean content){
    	return getTemplateByName(name, language, content, null);
    }
    
    
    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateByName(java.lang.String, java.lang.String, boolean, java.lang.String)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, boolean content, String type)  {
    	fi.vm.sade.viestintapalvelu.template.Template searchTempl = new fi.vm.sade.viestintapalvelu.template.Template();
    	
    	if (name == null) 
    	    return null;
    	
        Template template = templateDAO.findTemplateByName(name, language, type);

        if (template == null)
            return null;
        
        searchTempl.setId(template.getId());
    	searchTempl.setName(template.getName());
    	//searchTempl.setStyles(template.getStyles());
    	searchTempl.setLanguage(template.getLanguage());
    	searchTempl.setTimestamp(template.getTimestamp());
    	searchTempl.setStoringOid(template.getStoringOid());
    	searchTempl.setOrganizationOid(template.getOrganizationOid());
    	searchTempl.setTemplateVersio(template.getVersionro());
    	searchTempl.setType(template.getType());
    	
    	// Replacement
    	List<fi.vm.sade.viestintapalvelu.template.Replacement> replacement = new LinkedList<fi.vm.sade.viestintapalvelu.template.Replacement>();    	
    	for (Replacement rep : template.getReplacements()) {
    		fi.vm.sade.viestintapalvelu.template.Replacement repl = new fi.vm.sade.viestintapalvelu.template.Replacement();
    		repl.setId(rep.getId());
    		repl.setName(rep.getName());;
    		repl.setDefaultValue(rep.getDefaultValue());;
    		repl.setMandatory(rep.isMandatory());
    		repl.setTimestamp(rep.getTimestamp());
    		replacement.add(repl);
		}
    	searchTempl.setReplacements(replacement);
    	
    	// Content    	
    	if (content) { 
    	    // include style only with content
    	    searchTempl.setStyles(template.getStyles());
            
	    	List<fi.vm.sade.viestintapalvelu.template.TemplateContent> templateContent = new LinkedList<fi.vm.sade.viestintapalvelu.template.TemplateContent>();
	    	for (TemplateContent co : template.getContents()) {
	    	    
	    	    if (StringUtils.equalsIgnoreCase(type, Template.TYPE_EMAIL)) {
	                    // If type is email -> read only email content and subject from template
	                    // all other contents are ignored
	    	        if (!StringUtils.equalsIgnoreCase(co.getName(), TemplateContent.CONTENT_NAME_EMAIL_BODY))
	    	            continue;
	    	    } else {
	    	        // If type is doc -> read all template contents by ignore email subject or email content
                        if (StringUtils.equalsIgnoreCase(co.getName(), TemplateContent.CONTENT_NAME_EMAIL_BODY))
                            continue;
	    	    }
	    	    fi.vm.sade.viestintapalvelu.template.TemplateContent cont = new fi.vm.sade.viestintapalvelu.template.TemplateContent();
				cont.setId(co.getId());
				cont.setName(co.getName());
				cont.setContent(co.getContent());
				cont.setOrder(co.getOrder());
				cont.setTimestamp(co.getTimestamp());
				templateContent.add(cont);			
			}
	    	searchTempl.setContents(templateContent);
    	}
    	
    	return searchTempl;
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
     * @return	Draft
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Draft findDraftByNameOrgTag(String templateName,String languageCode, String oid, 
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
