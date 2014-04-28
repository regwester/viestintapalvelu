package fi.vm.sade.viestintapalvelu.testdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.letter.Letter;
import fi.vm.sade.viestintapalvelu.letter.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.Replacement;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.TemplateContent;

public class DocumentProviderTestData {
    public static Template getTemplate(Long id) {
        Template template = new Template();
        
        if (id != null) {
            template.setId(id);
        }
        
        template.setLanguage("FI");
        template.setName("test_template");
        template.setOrganizationOid("1.2.246.562.10.00000000001");
        template.setStoringOid("1.2.246.562.24.00000000001");
        template.setStyles("test-styles");
        template.setTimestamp(new Date());
        template.setVersionro("1.0");
        
        Set<Replacement> replacements = new HashSet<Replacement>();
        replacements.add(getReplacement(id, template));
        
        Set<TemplateContent> templateContents = new HashSet<TemplateContent>();
        templateContents.add(getTemplateContent(id, template));
        
        template.setContents(templateContents);
        template.setReplacements(replacements);
        
        return template;
    }
    
    public static Replacement getReplacement(Long id, Template template) {
        Replacement replacement = new Replacement();
        
        if (id != null) {
            replacement.setId(id);
        }
        
        replacement.setDefaultValue("test");
        replacement.setMandatory(false);
        replacement.setName("test_replacement");
        replacement.setTemplate(template);
        replacement.setTimestamp(new Date());
        
        return replacement;
    }   
    
    public static TemplateContent getTemplateContent(Long id, Template template) {
        TemplateContent templateContent = new TemplateContent();
        
        if (id != null) {
            templateContent.setId(id);
        }
        
        templateContent.setContent("test_content");
        templateContent.setContentType("pdf");
        templateContent.setName("test_template_content");
        templateContent.setOrder(1);
        templateContent.setTemplate(template);
        templateContent.setTimestamp(new Date());
        
        return templateContent;
    }

    public static Henkilo getHenkilo() {
        Henkilo henkilo = new Henkilo();
        
        henkilo.setOidHenkilo("1.2.246.562.24.34397748041");
        henkilo.setHetu("081181-9984");
        henkilo.setEtunimet("Etunimi");
        henkilo.setSukunimi("Sukunimi");
        henkilo.setKutsumanimi("Kutsumanimi");
        
        return henkilo;
    }
 }