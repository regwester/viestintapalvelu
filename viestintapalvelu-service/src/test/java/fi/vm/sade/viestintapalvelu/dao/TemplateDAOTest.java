package fi.vm.sade.viestintapalvelu.dao;

import java.util.List;

import javax.persistence.NoResultException;

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

import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.model.ContentStructure;
import fi.vm.sade.viestintapalvelu.model.Structure;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import static org.junit.Assert.*;

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
        Template storedTemplate = givenPublishedTemplate();
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
        Template storedTemplate = givenPublishedTemplate();
        String testHakuOid = "1234.56789.154875";
        templateDAO.insert(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl("test_template", "FI",
                        ContentStructureType.letter)
                .withApplicationPeriod(testHakuOid));
        assertNull(template);
    }

    @Test
    public void testFindTemplateByNameAndDefaultFound() {
        Template storedTemplate = givenPublishedTemplate();
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
        Template storedTemplate = givenPublishedTemplate();
        templateDAO.insert(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl()
                .withName("test_template")
                .withLanguage("FI")
                .withDefaultRequired());
        assertNull(template);
    }

    @Test
    public void testFindTemplateByNameAndWithoutDefaultFound() {
        Template storedTemplate = givenPublishedTemplate();
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
        Template storedTemplate = givenPublishedTemplate();
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
        Template storedTemplate = givenPublishedTemplate();
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
        Template storedTemplate = givenPublishedTemplate();
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
        Template storedTemplate = givenPublishedTemplate();
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
        templateDAO.insert(givenPublishedTemplate());
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

    private Template givenTemplateWithNameAndState(String templateNamePrefix, State state) {
        Template template = DocumentProviderTestData.getTemplateWithGivenNamePrefix(null, templateNamePrefix);
        template.setState(state);
        return templateDAO.insert(template);
    }
    
    private Template givenPublishedTemplateWithApplicationPeriod(String hakuOid) {
        Template template = givenPublishedTemplate();
        DocumentProviderTestData.getTemplateApplicationPeriod(template, hakuOid);
        return templateDAO.insert(template);
    }

    private Template givenPublishedTemplate() {
        Template template = DocumentProviderTestData.getTemplate(null);
        template.setState(State.julkaistu);
        Structure structure = new Structure();
        structure.setName("test_structure");
        structure.setLanguage(template.getLanguage());
        ContentStructure contentStructure = new ContentStructure();
        contentStructure.setStructure(structure);
        contentStructure.setType(ContentStructureType.letter);
        structure.getContentStructures().add(contentStructure);
        template.setStructure(structure);
        return template;
    }
}
