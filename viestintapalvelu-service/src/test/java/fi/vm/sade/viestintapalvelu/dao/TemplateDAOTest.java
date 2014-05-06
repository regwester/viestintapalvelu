package fi.vm.sade.viestintapalvelu.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

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
}
