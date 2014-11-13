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
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.Template.State;
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
    public void testFindTemplateByNameFound() {
        Template storedTemplate = givenPublishedTemplate();
        templateDAO.insert(storedTemplate);
        
        Template template = templateDAO.findTemplateByName("test_template", "FI");
        
        assertNotNull(template);
        assertNotNull(template.getId());
        assertEquals(storedTemplate.getName(), template.getName());
        assertTrue(template.getContents().size() == 1);
        assertTrue(template.getReplacements().size() == 1);
        assertEquals(template.getType(), "doc");
    }

    @Test
    public void testFindTemplateByNameAndTypeFound() {
        Template storedTemplate = givenPublishedTemplate();
        templateDAO.insert(storedTemplate);
        
        Template template = templateDAO.findTemplateByName("test_template", "FI", "doc");
        
        assertNotNull(template);
        assertNotNull(template.getId());
        assertEquals(storedTemplate.getName(), template.getName());
        assertTrue(template.getContents().size() == 1);
        assertTrue(template.getReplacements().size() == 1);
        assertEquals(template.getType(), "doc");
    }

    @Test
    public void testFindTemplateByNameAndAndTypeAndHakuFound() {
        Template storedTemplate = givenPublishedTemplate();
        String testHakuOid = "1234.56789.154875";
        DocumentProviderTestData.getTemplateHaku(storedTemplate, "1234.56789.012345");
        DocumentProviderTestData.getTemplateHaku(storedTemplate, testHakuOid);
        templateDAO.persist(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl()
                        .withName("test_template")
                        .withLanguage("FI")
                        .withType("doc")
                        .withApplicationPeriod(testHakuOid));
        assertNotNull(template);
        assertNotNull(template.getId());
        assertEquals(storedTemplate.getName(), template.getName());
        assertTrue(template.getContents().size() == 1);
        assertTrue(template.getReplacements().size() == 1);
        assertEquals(template.getType(), "doc");
    }

    @Test
    public void testFindTemplateByNameAndAndTypeAndHakuNotFound() {
        Template storedTemplate = givenPublishedTemplate();
        String testHakuOid = "1234.56789.154875";
        templateDAO.insert(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl()
                .withName("test_template")
                .withLanguage("FI")
                .withType("doc")
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

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl()
                .withName("test_template")
                .withLanguage("FI")
                .withType("doc")
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
    public void testFindTemplateByNameNotFound() {
        Template storedTemplate = givenPublishedTemplate();
        templateDAO.insert(storedTemplate);
        
        Template template = templateDAO.findTemplateByName("test_template_not_found", "FI");
        
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

    private Template givenTemplateWithNameAndState(String templateNamePrefix, State state) {
        Template template = DocumentProviderTestData.getTemplateWithGivenNamePrefix(null, templateNamePrefix);
        template.setState(state);
        return templateDAO.insert(template);
    }

    private Template givenPublishedTemplate() {
        Template template = DocumentProviderTestData.getTemplate(null);
        template.setState(State.julkaistu);
        return template;
    }
}
