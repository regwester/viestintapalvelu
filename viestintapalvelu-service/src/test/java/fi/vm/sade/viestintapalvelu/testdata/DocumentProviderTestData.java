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
package fi.vm.sade.viestintapalvelu.testdata;

import fi.vm.sade.dto.HenkiloDto;
import java.util.*;

import fi.vm.sade.dto.PagingAndSortingDTO;
import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.dto.OrganizationDTO;
import fi.vm.sade.viestintapalvelu.letter.Letter;
import fi.vm.sade.viestintapalvelu.letter.LetterBatch;
import fi.vm.sade.viestintapalvelu.letter.LetterContent;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchDto;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchLetterDto;
import fi.vm.sade.viestintapalvelu.model.*;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.model.types.ContentRole;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.model.types.ContentType;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureContentSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureSaveDto;

public class DocumentProviderTestData {
    public static AddressLabel getAddressLabel() {
        AddressLabel addressLabel = new AddressLabel("Etunimi", "Sukunimi", "Testiosoite 1", "Testiosoite 2", 
            "Testiosoite 3", "00100", "Helsinki", "", "Suomi", "FI");
        
        return addressLabel;
    }
    
    public static HenkiloDto getHenkilo() {
        HenkiloDto henkilo = new HenkiloDto();
        
        henkilo.setOidHenkilo("1.2.246.562.24.34397748041");
        henkilo.setHetu("081181-9984");
        henkilo.setEtunimet("Etunimi");
        henkilo.setSukunimi("Sukunimi");
        henkilo.setKutsumanimi("Kutsumanimi");
        
        return henkilo;
    }

    public static List<IPosti> getIPosti(Long id, fi.vm.sade.viestintapalvelu.model.LetterBatch letterBatch) {
        return getIPosti(id, letterBatch, 1);
    }
    
    public static TemplateApplicationPeriod getTemplateApplicationPeriod(Template template, String hakuOid) {
        TemplateApplicationPeriod period = new TemplateApplicationPeriod(template, hakuOid);
        template.addApplicationPeriod(period);
        return period;
    }

    public static List<IPosti> getIPosti(Long id, fi.vm.sade.viestintapalvelu.model.LetterBatch letterBatch, int count) {
        List<IPosti> iPostis = new ArrayList<IPosti>();
        for (int i = 0; i < count; ++i) {
            IPosti iposti = new IPosti();

            byte[] content = {'i', 'p', 'o', 's', 't', 'c', 'o', 'n', 't', 'e', 'n', 't'};
            iposti.setContent(content);
            iposti.setContentName("Iposti content");
            iposti.setContentType("application/pdf");
            iposti.setCreateDate(new Date());
            iposti.setId(id);
            iposti.setLetterBatch(letterBatch);
            iposti.setSentDate(new Date());
            iposti.setVersion(new Long(0));

            iPostis.add(iposti);
        }
        return iPostis;
    }
    
    public static fi.vm.sade.viestintapalvelu.letter.LetterBatch getLetterBatch() {
        LetterBatch letterBatch = new LetterBatch();
        
        letterBatch.setApplicationPeriod("Test-2014");
        letterBatch.setFetchTarget("test-fetchTarget");
        letterBatch.setLanguageCode("FI");
        letterBatch.setOrganizationOid("1.2.246.562.10.00000000001");
        letterBatch.setStoringOid("1.2.246.562.24.00000000001");
        letterBatch.setTag("test-tag");
        letterBatch.setTemplateId(new Long(1));
        letterBatch.setTemplateName("test-templateName");
        letterBatch.setTemplate(getTemplate());
        letterBatch.setTemplateReplacements(getTemplateReplacements());
        letterBatch.setLetters(getLetters());
        
        return letterBatch;
    }

    public static AsyncLetterBatchDto getAsyncLetterBatch() {
        AsyncLetterBatchDto letterBatch = new AsyncLetterBatchDto();
        letterBatch.setApplicationPeriod("Test-2014");
        letterBatch.setFetchTarget("test-fetchTarget");
        letterBatch.setLanguageCode("FI");
        letterBatch.setOrganizationOid("1.2.246.562.10.00000000001");
        letterBatch.setStoringOid("1.2.246.562.24.00000000001");
        letterBatch.setTag("test-tag");
        letterBatch.setTemplateId(new Long(1));
        letterBatch.setTemplateName("test-templateName");
        letterBatch.setTemplate(getTemplate());
        letterBatch.setTemplateReplacements(getTemplateReplacements());
        letterBatch.setLetters(getAsyncLetterBatchLetters());

        return letterBatch;
    }


    public static fi.vm.sade.viestintapalvelu.model.LetterBatch getLetterBatch(Long id) {
        fi.vm.sade.viestintapalvelu.model.LetterBatch letterBatch = new fi.vm.sade.viestintapalvelu.model.LetterBatch();
        letterBatch.setApplicationPeriod("Tradenomi 2014");
        letterBatch.setFetchTarget("fetchTarget");

        if (id != null) {
            letterBatch.setId(id);
        }

        letterBatch.setLanguage("FI");
        letterBatch.setOrganizationOid("1.2.246.562.10.00000000001");
        letterBatch.setStoringOid("1.2.246.562.24.00000000001");
        letterBatch.setTag("test-tag");
        letterBatch.setTemplateId(new Long(1));
        letterBatch.setTemplateName("test-templateName");
        letterBatch.setTimestamp(new Date());
        letterBatch.setVersion(new Long(0));
        letterBatch.setLetterReceivers(getLetterReceivers(id, letterBatch));
        letterBatch.setLetterReplacements(getLetterReplacements(id, letterBatch));
        letterBatch.setBatchStatus(fi.vm.sade.viestintapalvelu.model.LetterBatch.Status.processing);

        return letterBatch;
    }

    public static fi.vm.sade.viestintapalvelu.model.LetterBatch getLetterBatch(Long id, int count) {
        fi.vm.sade.viestintapalvelu.model.LetterBatch letterBatch = new fi.vm.sade.viestintapalvelu.model.LetterBatch();
        
        letterBatch.setApplicationPeriod("Tradenomi 2014");
        letterBatch.setFetchTarget("fetchTarget");
        
        if (id != null) {
            letterBatch.setId(id);
        }
        
        letterBatch.setLanguage("FI");
        letterBatch.setOrganizationOid("1.2.246.562.10.00000000001");
        letterBatch.setStoringOid("1.2.246.562.24.00000000001");
        letterBatch.setTag("test-tag");
        letterBatch.setTemplateId(new Long(1));
        letterBatch.setTemplateName("test-templateName");
        letterBatch.setTimestamp(new Date());
        letterBatch.setVersion(new Long(0));
        
        letterBatch.setLetterReceivers(getLetterReceivers(id, letterBatch, count));
        letterBatch.setLetterReplacements(getLetterReplacements(id, letterBatch));
        
        return letterBatch;
    }
    
    public static LetterContent getLetterContent() {
        LetterContent letterContent = new LetterContent();
        
        byte[] testContent = {'t','e','s','t','c','o','n','t','e','n','t'};
        letterContent.setContent(testContent);
        letterContent.setContentType("application/msword");
        letterContent.setTimestamp(new Date());
           
        return letterContent;
    }   
    
    public static LetterReceiverAddress getLetterReceiverAddress(Long id, LetterReceivers letterReceivers) {
        LetterReceiverAddress letterReceiverAddress = new LetterReceiverAddress();
        
        letterReceiverAddress.setAddressline("Testiosoite 1");
        letterReceiverAddress.setAddressline2("Testiosoite 2");
        letterReceiverAddress.setAddressline3("Testiosoite 3");
        letterReceiverAddress.setCity("Helsinki");
        letterReceiverAddress.setCountry("Suomi");
        letterReceiverAddress.setCountryCode("FI");
        letterReceiverAddress.setFirstName("Etunimi");
        
        if (id != null) {
            letterReceiverAddress.setId(id);
        }
        
        letterReceiverAddress.setLastName("Sukunimi");
        letterReceiverAddress.setLetterReceivers(letterReceivers);
        letterReceiverAddress.setPostalCode("00100");
        letterReceiverAddress.setRegion("");
        letterReceiverAddress.setVersion(new Long(1));
        
        return letterReceiverAddress;
    }

    public static LetterReceiverLetter getLetterReceiverLetter(Long id, LetterReceivers letterReceivers) {
        LetterReceiverLetter letterReceiverLetter = new LetterReceiverLetter();
        
        byte[] letter = {'l','e','t','t','e','r'}; 
        
        letterReceiverLetter.setContentType("application/msword");
        
        if (id != null) {
            letterReceiverLetter.setId(id);
        }
        
        letterReceiverLetter.setLetter(letter);
        letterReceiverLetter.setLetterReceivers(letterReceivers);
        letterReceiverLetter.setOriginalContentType("application/msword");
        letterReceiverLetter.setTimestamp(new Date());
        letterReceiverLetter.setVersion(new Long(0));
        
        return letterReceiverLetter;
    }

    public static Set<LetterReceiverReplacement> getLetterReceiverReplacement(Long id, LetterReceivers letterReceivers) {
        Set<LetterReceiverReplacement> letterReceiverReplacements = new HashSet<LetterReceiverReplacement>();
        
        LetterReceiverReplacement letterReceiverReplacement = new LetterReceiverReplacement();
        letterReceiverReplacement.setDefaultValue("");
        
        if (id != null) {
            letterReceiverReplacement.setId(id);
        }
        
        letterReceiverReplacement.setLetterReceivers(letterReceivers);
        letterReceiverReplacement.setMandatory(false);
        letterReceiverReplacement.setName("Nimi");
        letterReceiverReplacement.setTimestamp(new Date());
        letterReceiverReplacement.setVersion(new Long(0));
        
        letterReceiverReplacements.add(letterReceiverReplacement);
        
        return letterReceiverReplacements;
    }

    public static Set<LetterReceivers> getLetterReceivers(Long id,
                                                          fi.vm.sade.viestintapalvelu.model.LetterBatch letterBatch, int count) {
        Set<LetterReceivers> letterReceiversSet = new HashSet<LetterReceivers>();
        for (int i = 0; i < count ; i++) {
            LetterReceivers letterReceivers = new LetterReceivers();

            if (id != null) {
                letterReceivers.setId(id+i);
            }

            letterReceivers.setLetterBatch(letterBatch);
            letterReceivers.setTimestamp(new Date());
            letterReceivers.setVersion(new Long(0));
            letterReceivers.setLetterReceiverAddress(getLetterReceiverAddress(id, letterReceivers));
            letterReceivers.setLetterReceiverLetter(getLetterReceiverLetter(null, letterReceivers));
            letterReceivers.setLetterReceiverReplacement(getLetterReceiverReplacement(id ,letterReceivers));

            letterReceiversSet.add(letterReceivers);
        }
        return letterReceiversSet;
    }

    public static Set<LetterReceivers> getLetterReceivers(Long id, 
        fi.vm.sade.viestintapalvelu.model.LetterBatch letterBatch) {
        Set<LetterReceivers> letterReceiversSet = new HashSet<LetterReceivers>();
        
        LetterReceivers letterReceivers = new LetterReceivers();
        
        if (id != null) {
            letterReceivers.setId(id);
        }
        
        letterReceivers.setLetterBatch(letterBatch);
        letterReceivers.setTimestamp(new Date());
        letterReceivers.setVersion(new Long(0));
        letterReceivers.setLetterReceiverAddress(getLetterReceiverAddress(id, letterReceivers));
        letterReceivers.setLetterReceiverLetter(getLetterReceiverLetter(id, letterReceivers));
        letterReceivers.setLetterReceiverReplacement(getLetterReceiverReplacement(id ,letterReceivers));
        
        letterReceiversSet.add(letterReceivers);
        
        return letterReceiversSet;
    }

    public static Set<LetterReplacement> getLetterReplacements(Long id, 
        fi.vm.sade.viestintapalvelu.model.LetterBatch letterBatch) {
        Set<LetterReplacement> letterReplacements = new HashSet<LetterReplacement>();
        
        LetterReplacement letterReplacement = new LetterReplacement();
        letterReplacement.setDefaultValue("");
        
        if (id != null) {
            letterReplacement.setId(id);
        }
        
        letterReplacement.setLetterBatch(letterBatch);
        letterReplacement.setMandatory(false);
        letterReplacement.setName("test-replacement-name");
        letterReplacement.setTimestamp(new Date());
        letterReplacement.setVersion(new Long(0));
        
        letterReplacements.add(letterReplacement);
        
        return letterReplacements;
    }

    public static List<Letter> getLetters() {
        List<Letter> letters = new ArrayList<Letter>();
        
        Letter letter = new Letter();
        letter.setAddressLabel(getAddressLabel());
        letter.setLanguageCode("FI");
        letter.setLetterContent(getLetterContent());
        letter.setTemplateReplacements(getTemplateReplacements());
        
        letters.add(letter);
        
        return letters;
    }

    public static List<AsyncLetterBatchLetterDto> getAsyncLetterBatchLetters() {
        List<AsyncLetterBatchLetterDto> letters = new ArrayList<AsyncLetterBatchLetterDto>();

        AsyncLetterBatchLetterDto letter = new AsyncLetterBatchLetterDto();
        letter.setAddressLabel(getAddressLabel());
        letter.setLanguageCode("FI");
        letter.setTemplateReplacements(getTemplateReplacements());

        letters.add(letter);

        return letters;
    }

    public static OrganisaatioRDTO getOrganisaatioRDTO() {
        OrganisaatioRDTO organisaatio = new OrganisaatioRDTO();

        organisaatio.setOid("1.2.246.562.10.00000000001");
        Map<String, String> nimet = new HashMap<String, String>();
        nimet.put("fi", "Oppilaitos");
        organisaatio.setNimi(nimet);

        return organisaatio;
    }

    public static OrganizationDTO getOrganizationDTO() {
        OrganizationDTO organizationDTO = new OrganizationDTO();

        organizationDTO.setName("OPH");
        organizationDTO.setOid("1.2.246.562.10.00000000001");

        return organizationDTO;
    }

    public static PagingAndSortingDTO getPagingAndSortingDTO() {
        PagingAndSortingDTO pagingAndSorting = new PagingAndSortingDTO();
        
        pagingAndSorting.setFromIndex(1);
        pagingAndSorting.setNumberOfRows(1);
        
        return pagingAndSorting;
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

    public static List<fi.vm.sade.viestintapalvelu.template.Replacement> getReplacements() {
        List<fi.vm.sade.viestintapalvelu.template.Replacement> replacements = 
            new ArrayList<fi.vm.sade.viestintapalvelu.template.Replacement>();
        
        fi.vm.sade.viestintapalvelu.template.Replacement replacement = 
            new fi.vm.sade.viestintapalvelu.template.Replacement();
        replacement.setDefaultValue("");
        replacement.setMandatory(false);
        replacement.setName("test-name");
        replacement.setTimestamp(new Date());
        
        replacements.add(replacement);
        
        return replacements;
    }

    public static fi.vm.sade.viestintapalvelu.template.Template getTemplate() {
        return getTemplateWithType(ContentStructureType.letter);
    }

    public static fi.vm.sade.viestintapalvelu.template.Template getTemplateWithType(ContentStructureType type) {
        fi.vm.sade.viestintapalvelu.template.Template template = new fi.vm.sade.viestintapalvelu.template.Template();
        
        template.setLanguage("FI");
        template.setName("test_template");
        template.setStructure(structureSaveDto(
                contentStructureSaveDto(ContentStructureType.letter,
                        contentSaveDto("name", ContentRole.body, ContentType.html)
                ),
                contentStructureSaveDto(ContentStructureType.email,
                        contentSaveDto("name", ContentRole.header, ContentType.plain),
                        contentSaveDto("name2", ContentRole.body, ContentType.html)
                )
        ));
        template.setOrganizationOid("1.2.246.562.10.00000000001");
        template.setStoringOid("1.2.246.562.24.00000000001");
        template.setStyles("test-styles");
        template.setTimestamp(new Date());
        template.setContents(getTemplateContents());
        template.setReplacements(getReplacements());
        template.setType(type.name());
        template.setState(State.luonnos);

        return template;
    }
    
    public static Template getTemplateWithGivenNamePrefix(Long id, String prefix) {
        Template template = new Template();
        template.setStructure(getStructureWithGivenPrefixForName(prefix,
                contentStructure(ContentStructureType.letter, content(ContentRole.body, ContentType.html))));
        
        if (id != null) {
            template.setId(id);
        }
        
        template.setLanguage("FI");
        template.setName(prefix + "_template");
        template.setOrganizationOid("1.2.246.562.10.00000000001");
        template.setStoringOid("1.2.246.562.24.00000000001");
        template.setTimestamp(new Date());
        template.setVersionro("1.0");
        
        Set<Replacement> replacements = new HashSet<Replacement>();
        replacements.add(getReplacement(id, template));
        template.setReplacements(replacements);
        
        return template;
    }

    public static Structure getStructureWithGivenPrefixForName(String prefix, ContentStructure ... contentStructures) {
        Structure structure = structure(contentStructures);
        structure.setName(prefix + "_structure");
        structure.setLanguage("FI");
        structure.setDescription("Structure description");
        return structure;
    }

    public static Template getTemplate(Long id) {
        return getTemplateWithGivenNamePrefix(id, "test");
    }

    public static TemplateApplicationPeriod getTemplateHaku(Template template, String hakuOid) {
        TemplateApplicationPeriod templateApplicationPeriod = new TemplateApplicationPeriod(template, hakuOid);
        template.getApplicationPeriods().add(templateApplicationPeriod);
        return templateApplicationPeriod;
    }

    public static List<fi.vm.sade.viestintapalvelu.template.TemplateContent> getTemplateContents() {
        List<fi.vm.sade.viestintapalvelu.template.TemplateContent> templateContents = 
            new ArrayList<fi.vm.sade.viestintapalvelu.template.TemplateContent>();
        
        fi.vm.sade.viestintapalvelu.template.TemplateContent templateContent = 
            new fi.vm.sade.viestintapalvelu.template.TemplateContent();
        templateContent.setContent("test_content");
        templateContent.setName("test_template_content");
        templateContent.setOrder(1);
        templateContent.setTimestamp(new Date());
        
        templateContents.add(templateContent);
        
        return templateContents;
    }

    public static Map<String, Object> getTemplateReplacements() {
        Map<String, Object> templateReplacements = new HashMap<String, Object>();
        templateReplacements.put("key-1", "templatereplacemet-1");
        templateReplacements.put("key-2", "templatereplacemet-2");
        
        return templateReplacements;
    }

    public static LetterReceiverLetterAttachment getLetterReceiverLetterAttachment(LetterReceiverLetter letter) {
        LetterReceiverLetterAttachment attachment = new LetterReceiverLetterAttachment();
        attachment.setLetterReceiverLetter(letter);
        attachment.setName("Liite");
        attachment.setContentType("application/pdf");
        attachment.setContents("Test data".getBytes());
        attachment.setVersion(1l);
        return attachment;
    }

    public static Structure structure(ContentStructure ...contentStructures) {
        Structure structure = new Structure();
        structure.setName("test_structure");
        structure.setLanguage("FI");
        for (ContentStructure contentStructure : contentStructures) {
            contentStructure.setStructure(structure);
            structure.getContentStructures().add(contentStructure);
        }
        return structure;
    }

    public static Structure with(Structure structure, ContentReplacement ...contentReplacements) {
        int orderNumber = 0;
        for (ContentReplacement replacement : contentReplacements) {
            replacement.setOrderNumber(++orderNumber);
            replacement.setStructure(structure);
            structure.getReplacements().add(replacement);
        }
        return structure;
    }

    public static ContentReplacement replacement(String key) {
        return replacement(key, ContentType.plain, 1);
    }

    public static ContentReplacement replacement(String key, ContentType contentType, int numberOfRows) {
        ContentReplacement replacement = new ContentReplacement();
        replacement.setKey(key);
        replacement.setName(key);
        replacement.setDescription(key);
        replacement.setContentType(contentType);
        replacement.setNumberOfRows(numberOfRows);
        return replacement;
    }

    public static ContentStructure contentStructure(ContentStructureType type, ContentStructureContent... contents) {
        ContentStructure contentStructure = new ContentStructure();
        contentStructure.setType(type);
        int orderNumber = 0;
        for (ContentStructureContent content : contents) {
            content.setOrderNumber(++orderNumber);
            content.setContentStructure(contentStructure);
            contentStructure.getContents().add(content);
        }
        return contentStructure;
    }

    public static ContentStructureContent content(ContentRole role, ContentType type) {
        ContentStructureContent csc = new ContentStructureContent();
        csc.setRole(role);
        Content content = new Content();
        content.setContent(type == ContentType.html ? "<html><head><title>T</title></head><body>B</body></html>"
                : "content");
        content.setName("name_"+role.name()+"_"+type.name());
        content.setContentType(type);
        csc.setContent(content);
        return csc;
    }

    public static StructureSaveDto structureSaveDto(ContentStructureSaveDto... contentStructures) {
        StructureSaveDto saveDto = new StructureSaveDto();
        saveDto.setName("test_structure");
        saveDto.setLanguage("FI");
        saveDto.setDescription("Testirakenne");
        for (ContentStructureSaveDto contentStructure : contentStructures) {
            saveDto.getContentStructures().add(contentStructure);
        }
        return saveDto;
    }

    public static ContentStructureSaveDto contentStructureSaveDto(ContentStructureType type, ContentStructureContentSaveDto... contents) {
        ContentStructureSaveDto structure = new ContentStructureSaveDto();
        structure.setType(type);
        for (ContentStructureContentSaveDto content : contents) {
            structure.getContents().add(content);
        }
        return structure;
    }

    public static ContentStructureContentSaveDto contentSaveDto(ContentRole role, ContentType type) {
        return contentSaveDto("name_"+role.name(), role, type);
    }

    public static ContentStructureContentSaveDto contentSaveDto(String name, ContentRole role, ContentType type) {
        return new ContentStructureContentSaveDto(role, name, type,
                type == ContentType.html ? "<html><head><title>T</title></head><body>B</body></html>"
                        : "content");
    }
}
