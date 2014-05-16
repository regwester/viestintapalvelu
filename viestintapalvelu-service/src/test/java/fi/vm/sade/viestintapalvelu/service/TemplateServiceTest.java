package fi.vm.sade.viestintapalvelu.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.viestintapalvelu.dao.DraftDAO;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateService;
import fi.vm.sade.viestintapalvelu.template.impl.TemplateServiceImpl;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("/test-application-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class TemplateServiceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private TemplateDAO mockedTemplateDAO;
    @Mock
    private CurrentUserComponent mockedCurrentUserComponent;
    @Mock
    private DraftDAO mockedDraftDAO;
    private TemplateService templateService;

    @Before
    public void setup() {
        this.templateService = new TemplateServiceImpl(mockedTemplateDAO, mockedCurrentUserComponent, mockedDraftDAO);
    }
    
    @Test
    public void testStoreTemplateDTO() {
        when(mockedCurrentUserComponent.getCurrentUser()).thenReturn(DocumentProviderTestData.getHenkilo());
        mockedTemplateDAO.insert(DocumentProviderTestData.getTemplate(new Long(1)));
        
        fi.vm.sade.viestintapalvelu.template.Template template = DocumentProviderTestData.getTemplate();
        templateService.storeTemplateDTO(template);
        
        verify(mockedTemplateDAO).insert(DocumentProviderTestData.getTemplate(new Long(1)));
    }

    @Test
    public void testFindById() {
        List<Template> mockedTemplates = new ArrayList<Template>();
        Template mockedTemplate = DocumentProviderTestData.getTemplate(new Long(1));
        mockedTemplates.add(mockedTemplate);
        
        when(mockedTemplateDAO.findBy(eq("id"), any(Long.class))).thenReturn(mockedTemplates);
        
        fi.vm.sade.viestintapalvelu.template.Template templateFindByID = templateService.findById(1);
        
        assertNotNull(templateFindByID);
        assertTrue(templateFindByID.getId() == 1);
        assertNotNull(templateFindByID.getContents().size() == 1);
        assertNotNull(templateFindByID.getReplacements().size() == 1);
    }

    @Test
    public void testGetTemplateByName() {
        Template mockedTemplate = DocumentProviderTestData.getTemplate(new Long(1));
        when(mockedTemplateDAO.findTemplateByName(any(String.class), any(String.class), any(String.class))).thenReturn(mockedTemplate);
        
        fi.vm.sade.viestintapalvelu.template.Template templateFindByName = 
            templateService.getTemplateByName("test_template", "FI", true);
        
        assertNotNull(templateFindByName);
        assertTrue(templateFindByName.getId() == 1);
        assertNotNull(templateFindByName.getContents().size() == 1);
        assertNotNull(templateFindByName.getReplacements().size() == 1);
    }

    @Test
    public void testGetTemplateByNameAndType() {
        Template mockedTemplate = DocumentProviderTestData.getTemplate(new Long(1));
        when(mockedTemplateDAO.findTemplateByName(any(String.class), any(String.class), any(String.class))).thenReturn(mockedTemplate);
        
        fi.vm.sade.viestintapalvelu.template.Template templateFindByName = 
            templateService.getTemplateByName("test_template", "FI", true, "doc");
        
        assertNotNull(templateFindByName);
        assertTrue(templateFindByName.getId() == 1);
        assertNotNull(templateFindByName.getContents().size() == 1);
        assertNotNull(templateFindByName.getReplacements().size() == 1);
        assertTrue(templateFindByName.getType() != null);
        assertTrue(templateFindByName.getType().equals("doc"));
    }

}
