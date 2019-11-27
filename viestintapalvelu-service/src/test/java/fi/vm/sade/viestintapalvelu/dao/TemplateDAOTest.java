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
package fi.vm.sade.viestintapalvelu.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.model.ContentStructure;
import fi.vm.sade.viestintapalvelu.model.Structure;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class TemplateDAOTest {
    @Autowired
    private TemplateDAO templateDAO;

    @Test
    public void testFindTemplateByNameAndAndTypeAndHakuFound() {
        Template storedTemplate = givenPublishedTemplate("foo");
        String testHakuOid = "1234.56789.154875";
        DocumentProviderTestData.getTemplateHaku(storedTemplate, "1234.56789.012345");
        DocumentProviderTestData.getTemplateHaku(storedTemplate, testHakuOid);
        templateDAO.persist(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl("test_template", "FI",
                                ContentStructureType.letter)
                        .withApplicationPeriod(testHakuOid));
        assertNotNull(template);
        assertNotNull(template.getId());
        assertEquals(storedTemplate.getName(), template.getName());
        assertTrue(template.getReplacements().size() == 1);
    }

    @Test
    public void testFindTemplateByNameAndAndTypeAndHakuNotFound() {
        Template storedTemplate = givenPublishedTemplate("foo");
        String testHakuOid = "1234.56789.154875";
        templateDAO.insert(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl("test_template", "FI",
                        ContentStructureType.letter)
                .withApplicationPeriod(testHakuOid));
        assertNull(template);
    }

    @Test
    public void testFindTemplateByNameAndDefaultFound() {
        Template storedTemplate = givenPublishedTemplate("foo");
        storedTemplate.setUsedAsDefault(true);
        templateDAO.insert(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl()
                .withName("test_template")
                .withLanguage("FI")
                .withDefaultRequired());
        assertNotNull(template);
        assertEquals(template.getId(), storedTemplate.getId());
    }

    @Test
    public void testFindTemplateByNameAndDefaultNotFound() {
        Template storedTemplate = givenPublishedTemplate("foo");
        templateDAO.insert(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl()
                .withName("test_template")
                .withLanguage("FI")
                .withDefaultRequired());
        assertNull(template);
    }

    @Test
    public void testFindTemplateByNameAndWithoutDefaultFound() {
        Template storedTemplate = givenPublishedTemplate("foo");
        templateDAO.insert(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl()
                .withName("test_template")
                .withLanguage("FI")
                // test the criteria as well:
                .withDefaultRequired().withoutDefaultRequired());
        assertNotNull(template);
        assertEquals(template.getId(), storedTemplate.getId());
    }

    @Test
    public void testFindTemplateByNameAndAndTypeAndHakuNotFound2() {
        Template storedTemplate = givenPublishedTemplate("foo");
        String testHakuOid = "1234.56789.154875";
        DocumentProviderTestData.getTemplateHaku(storedTemplate, "1234.56789.012345");
        templateDAO.insert(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl("test_template", "FI",
                        ContentStructureType.letter)
                .withApplicationPeriod(testHakuOid));
        assertNull(template);
    }
    
    @Test
    public void findsTemplateByNameAndState() {
        Template storedTemplate = givenPublishedTemplate("foo");
        storedTemplate.setState(State.julkaistu);
        templateDAO.insert(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl()
                .withName("test_template")
                .withLanguage("FI")
                .withState(State.julkaistu));
        assertNotNull(template);
        assertEquals(template.getId(), storedTemplate.getId());
    }
    
    @Test
    public void doesNotFindTemplateByNameAndState() {
        Template storedTemplate = givenPublishedTemplate("foo");
        storedTemplate.setState(State.suljettu);
        templateDAO.insert(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl()
                .withName("test_template")
                .withLanguage("FI")
                .withState(State.julkaistu));
        assertNull(template);
    }

    @Test
    public void testGetAvailableTemplatesFound() {
        Template storedTemplate = givenPublishedTemplate("foo");
        templateDAO.insert(storedTemplate);

        List<String> availableTemplates = templateDAO.getAvailableTemplates();
        
        assertNotNull(availableTemplates);
        assertTrue(availableTemplates.size() == 1);
        assertTrue(availableTemplates.get(0).indexOf("::") != -1);
    }
    
    @Test
    public void returnsTemplatesUsingState() {
        String templateNamePrefix = "suljettu";
        Template closedTemplate = givenTemplateWithNameAndState(templateNamePrefix, State.suljettu);
        templateDAO.insert(givenPublishedTemplate("foo"));
        List<String> availableTemplates = templateDAO.getAvailableTemplatesByType(State.suljettu);
        assertEquals(1, availableTemplates.size());
        assertTrue(availableTemplates.get(0).contains(templateNamePrefix));
        assertTrue(availableTemplates.get(0).contains(closedTemplate.getLanguage()));
    }
    
    @Test (expected = NoResultException.class)
    public void returnsClosedTemplateUsingState() {
        Template template = givenTemplateWithNameAndState("suljettu", State.suljettu);
        assertNotNull(templateDAO.findByIdAndState(template.getId(), State.suljettu));
        templateDAO.findByIdAndState(template.getId(), State.julkaistu);
    }
    
    @Test (expected = NoResultException.class)
    public void returnsPublishedTemplateUsingState() {
        Template template = givenTemplateWithNameAndState("julkaistu", State.julkaistu);
        assertNotNull(templateDAO.findByIdAndState(template.getId(), State.julkaistu));
        templateDAO.findByIdAndState(template.getId(), State.luonnos);
    }
    
    @Test (expected = NoResultException.class)
    public void returnsDraftTemplateUsingState() {
        Template template = givenTemplateWithNameAndState("luonnos", State.luonnos);
        assertNotNull(templateDAO.findByIdAndState(template.getId(), State.luonnos));
        templateDAO.findByIdAndState(template.getId(), State.suljettu);
    }
    
    @Test
    public void returnsTemplateUsingHakuOid() {
        String hakuOid = "1.9.2.1234";
        givenPublishedTemplateWithApplicationPeriod(hakuOid);
        assertEquals(1, templateDAO.findTemplates(new TemplateCriteriaImpl().withApplicationPeriod(hakuOid)).size());
        assertTrue(templateDAO.findTemplates(new TemplateCriteriaImpl().withApplicationPeriod("1323")).isEmpty());
    }

    @Test
    public void findByOrganizationOID() {
        Template template1 = givenPublishedTemplate("foo");
        template1.setOrganizationOid("org1");
        templateDAO.insert(template1);

        Template template2 = givenPublishedTemplate("bar");
        template2.setOrganizationOid("org2");
        templateDAO.insert(template2);

        Template template3 = givenPublishedTemplate("baz");
        template3.setOrganizationOid("org3");
        templateDAO.insert(template3);

        List<String> oids = new ArrayList<String>();
        oids.add("org1");
        oids.add("org3");

        List<Template> byOrganizationOIDs = templateDAO.findByOrganizationOIDs(oids);
        assertEquals(2, byOrganizationOIDs.size());


        oids = new ArrayList<String>();
        oids.add("org1");
        oids.add("org2");
        oids.add("org3");
        byOrganizationOIDs = templateDAO.findByOrganizationOIDs(oids);
        assertEquals(3, byOrganizationOIDs.size());

        oids = new ArrayList<String>();
        oids.add("org2");
        oids.add("org3");

        byOrganizationOIDs = templateDAO.findByOrganizationOIDs(oids);
        assertEquals(2, byOrganizationOIDs.size());
        
        oids = new ArrayList<>();
        templateDAO.findByOrganizationOIDs(oids);

    }

    private Template givenTemplateWithNameAndState(String templateNamePrefix, State state) {
        Template template = DocumentProviderTestData.getTemplateWithGivenNamePrefix(null, templateNamePrefix);
        template.setState(state);
        return templateDAO.insert(template);
    }
    
    private Template givenPublishedTemplateWithApplicationPeriod(String hakuOid) {
        Template template = givenPublishedTemplate("foo");
        DocumentProviderTestData.getTemplateApplicationPeriod(template, hakuOid);
        return templateDAO.insert(template);
    }

    private Template givenPublishedTemplate(String structureNameSuffix) {
        Template template = DocumentProviderTestData.getTemplate(null);
        template.setState(State.julkaistu);
        Structure structure = new Structure();
        structure.setName("test_structure_" + structureNameSuffix);
        structure.setLanguage(template.getLanguage());
        ContentStructure contentStructure = new ContentStructure();
        contentStructure.setStructure(structure);
        contentStructure.setType(ContentStructureType.letter);
        structure.getContentStructures().add(contentStructure);
        template.setStructure(structure);
        return template;
    }
}
