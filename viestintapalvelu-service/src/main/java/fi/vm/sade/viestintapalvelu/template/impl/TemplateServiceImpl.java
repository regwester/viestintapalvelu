package fi.vm.sade.viestintapalvelu.template.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.DocumentException;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.model.Replacement;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.TemplateContent;
import fi.vm.sade.viestintapalvelu.template.TemplateService;

@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {
    private CurrentUserComponent currentUserComponent;
    private TemplateDAO templateDAO;

    @Autowired
    public TemplateServiceImpl(TemplateDAO templateDAO, CurrentUserComponent currentUserComponent) {
        this.templateDAO = templateDAO;
        this.currentUserComponent = currentUserComponent;
    }
    
    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateFromFiles(java.lang.String, java.lang.String)
     */
    @Override
    public Template getTemplateFromFiles(String languageCode, String... names) throws IOException {
        Template result = new Template();
        Set<TemplateContent> contents = new HashSet<TemplateContent>();
        result.setLanguage(languageCode);
        result.setName("template");
        result.setOrganizationOid("oid_org");
        result.setStoringOid("storingOid");
        result.setTimestamp(new Date());
        int i = 1;
        for (String name : names) {
            String templateName = Utils.resolveTemplateName(name, languageCode);
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
        storeTemplate(model);
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

        Template result = new Template();
        String templateName = Utils.resolveTemplateName(name, languageCode);
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
       return getTemplateByName(name, language, true);
    } 
    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.template.TemplateService#getTemplateByName(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public fi.vm.sade.viestintapalvelu.template.Template getTemplateByName(String name, String language, boolean content)  {
    	fi.vm.sade.viestintapalvelu.template.Template searchTempl = new fi.vm.sade.viestintapalvelu.template.Template();
    	
        Template template = templateDAO.findTemplateByName(name, language);
    	searchTempl.setId(template.getId());
    	searchTempl.setName(template.getName());
    	//searchTempl.setStyles(template.getStyles());
    	searchTempl.setLanguage(template.getLanguage());
    	searchTempl.setTimestamp(template.getTimestamp());
    	searchTempl.setStoringOid(template.getStoringOid());
    	searchTempl.setOrganizationOid(template.getOrganizationOid());
    	searchTempl.setTemplateVersio(template.getVersionro());
    	
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
    
}