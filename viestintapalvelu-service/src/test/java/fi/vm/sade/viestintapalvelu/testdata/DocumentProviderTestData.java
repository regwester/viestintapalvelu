package fi.vm.sade.viestintapalvelu.testdata;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverAddress;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.LetterReplacement;
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

    public static fi.vm.sade.viestintapalvelu.model.LetterBatch getLetterBatch() {
        fi.vm.sade.viestintapalvelu.model.LetterBatch letterBatch = new fi.vm.sade.viestintapalvelu.model.LetterBatch();
        
        letterBatch.setApplicationPeriod("Tradenomi 2014");
        letterBatch.setFetchTarget("fetchTarget");
        letterBatch.setId(new Long(1));
        letterBatch.setLanguage("FI");
        letterBatch.setOrganizationOid("1.2.246.562.10.00000000001");
        letterBatch.setStoringOid("1.2.246.562.24.00000000001");
        letterBatch.setTag("test-tag");
        letterBatch.setTemplateId(new Long(1));
        letterBatch.setTemplateName("test-templateName");
        letterBatch.setTimestamp(new Date());
        letterBatch.setVersion(new Long(0));
        
        letterBatch.setLetterReceivers(getLetterReceivers(letterBatch));
        letterBatch.setLetterReplacements(getLetterReplacements(letterBatch));
        
        return letterBatch;
    }

    public static Set<LetterReplacement> getLetterReplacements(fi.vm.sade.viestintapalvelu.model.LetterBatch letterBatch) {
        Set<LetterReplacement> letterReplacements = new HashSet<LetterReplacement>();
        
        LetterReplacement letterReplacement = new LetterReplacement();
        letterReplacement.setDefaultValue("");
        letterReplacement.setId(new Long(1));
        letterReplacement.setLetterBatch(letterBatch);
        letterReplacement.setMandatory(false);
        letterReplacement.setName("Nimi");
        letterReplacement.setTimestamp(new Date());
        letterReplacement.setVersion(new Long(0));
        
        return letterReplacements;
    }

    public static Set<LetterReceivers> getLetterReceivers(fi.vm.sade.viestintapalvelu.model.LetterBatch letterBatch) {
        Set<LetterReceivers> letterReceiversSet = new HashSet<LetterReceivers>();
        
        LetterReceivers letterReceivers = new LetterReceivers();
        letterReceivers.setId(new Long(1));
        letterReceivers.setLetterBatch(letterBatch);
        letterReceivers.setTimestamp(new Date());
        letterReceivers.setVersion(new Long(0));
        letterReceivers.setLetterReceiverAddress(getLetterReceiverAddress(letterReceivers));
        letterReceivers.setLetterReceiverLetter(getLetterReceiverLetter(letterReceivers));
        letterReceivers.setLetterReceiverReplacement(getLetterReceiverReplacement(letterReceivers));
        
        return letterReceiversSet;
    }

    public static Set<LetterReceiverReplacement> getLetterReceiverReplacement(LetterReceivers letterReceivers) {
        Set<LetterReceiverReplacement> letterReceiverReplacements = new HashSet<LetterReceiverReplacement>();
        
        LetterReceiverReplacement letterReceiverReplacement = new LetterReceiverReplacement();
        letterReceiverReplacement.setDefaultValue("");
        letterReceiverReplacement.setId(new Long(1));
        letterReceiverReplacement.setLetterReceivers(letterReceivers);
        letterReceiverReplacement.setMandatory(false);
        letterReceiverReplacement.setName("Nimi");
        letterReceiverReplacement.setTimestamp(new Date());
        letterReceiverReplacement.setVersion(new Long(0));
        
        return letterReceiverReplacements;
    }

    public static LetterReceiverLetter getLetterReceiverLetter(LetterReceivers letterReceivers) {
        LetterReceiverLetter letterReceiverLetter = new LetterReceiverLetter();
        
        byte[] letter = {'l','e','t','t','e','r'}; 
        
        letterReceiverLetter.setContentType("application/msword");
        letterReceiverLetter.setId(new Long(1));
        letterReceiverLetter.setLetter(letter);
        letterReceiverLetter.setLetterReceivers(letterReceivers);
        letterReceiverLetter.setOriginalContentType("application/msword");
        letterReceiverLetter.setTimestamp(new Date());
        letterReceiverLetter.setVersion(new Long(0));
        
        return letterReceiverLetter;
    }

    public static LetterReceiverAddress getLetterReceiverAddress(LetterReceivers letterReceivers) {
        LetterReceiverAddress letterReceiverAddress = new LetterReceiverAddress();
        
        letterReceiverAddress.setAddressline("Testiosoite 1");
        letterReceiverAddress.setAddressline2("Testiosoite 2");
        letterReceiverAddress.setAddressline3("Testiosoite 3");
        letterReceiverAddress.setCity("Helsinki");
        letterReceiverAddress.setCountry("Suomi");
        letterReceiverAddress.setCountryCode("FI");
        letterReceiverAddress.setFirstName("Etunimi");
        letterReceiverAddress.setId(new Long(1));
        letterReceiverAddress.setLastName("Sukunimi");
        letterReceiverAddress.setLetterReceivers(letterReceivers);
        letterReceiverAddress.setPostalCode("00100");
        letterReceiverAddress.setRegion("");
        letterReceiverAddress.setVersion(new Long(1));
        
        return letterReceiverAddress;
    }
 }