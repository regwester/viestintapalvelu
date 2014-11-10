package fi.vm.sade.viestintapalvelu.dao;

import java.util.List;

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
        Template storedTemplate = DocumentProviderTestData.getTemplate(null);
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
        Template storedTemplate = DocumentProviderTestData.getTemplate(null);
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
        Template storedTemplate = DocumentProviderTestData.getTemplate(null);
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
        Template storedTemplate = DocumentProviderTestData.getTemplate(null);
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
        Template storedTemplate = DocumentProviderTestData.getTemplate(null);
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
        Template storedTemplate = DocumentProviderTestData.getTemplate(null);
        templateDAO.insert(storedTemplate);

        Template template = templateDAO.findTemplate(new TemplateCriteriaImpl()
                .withName("test_template")
                .withLanguage("FI")
                .withDefaultRequired());
        assertNull(template);
    }

    @Test
    public void testFindTemplateByNameAndWithoutDefaultFound() {
        Template storedTemplate = DocumentProviderTestData.getTemplate(null);
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
        Template storedTemplate = DocumentProviderTestData.getTemplate(null);
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
    public void testFindTemplateByNameNotFound() {
        Template storedTemplate = DocumentProviderTestData.getTemplate(null);
        templateDAO.insert(storedTemplate);
        
        Template template = templateDAO.findTemplateByName("test_template_not_found", "FI");
        
        assertNull(template);
    } 
    
    @Test
    public void testGetAvailableTemplatesFound() {
        Template storedTemplate = DocumentProviderTestData.getTemplate(null);
        templateDAO.insert(storedTemplate);

        List<String> availableTemplates = templateDAO.getAvailableTemplates();
        
        assertNotNull(availableTemplates);
        assertTrue(availableTemplates.size() == 1);
        assertTrue(availableTemplates.get(0).indexOf("::") != -1);
    }
    
    @Test
    public void returnsTemplatesUsingType() {
        String templateNamePrefix = "suljettu";
        Template closedTemplate = DocumentProviderTestData.getTemplateWithGivenNamePrefix(null, templateNamePrefix);
        closedTemplate.setState(State.CLOSED);
        templateDAO.insert(closedTemplate);
        templateDAO.insert(DocumentProviderTestData.getTemplate(null));
        List<String> availableTemplates = templateDAO.getAvailableTemplatesByType(State.CLOSED);
        assertEquals(1, availableTemplates.size());
        assertTrue(availableTemplates.get(0).contains(templateNamePrefix));
        assertTrue(availableTemplates.get(0).contains(closedTemplate.getLanguage()));
    }

}
