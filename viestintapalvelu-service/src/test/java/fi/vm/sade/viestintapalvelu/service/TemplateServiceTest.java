package fi.vm.sade.viestintapalvelu.service;

import fi.vm.sade.viestintapalvelu.dao.DraftDAO;
import fi.vm.sade.viestintapalvelu.dao.StructureDAO;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.structure.StructureService;
import fi.vm.sade.viestintapalvelu.template.TemplateService;
import fi.vm.sade.viestintapalvelu.template.impl.TemplateServiceImpl;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    @Mock
    private StructureDAO structureDAO;
    @Mock
    private StructureService structureService;
    private TemplateService templateService;

    @Before
    public void setup() {
        this.templateService = new TemplateServiceImpl(mockedTemplateDAO, mockedCurrentUserComponent, mockedDraftDAO,
                structureDAO, structureService);
    }

    @Test
    public void testStoreTemplateDTO() {
        when(mockedCurrentUserComponent.getCurrentUser()).thenReturn(DocumentProviderTestData.getHenkilo());
        mockedTemplateDAO.insert(DocumentProviderTestData.getTemplate(1l));
        
        fi.vm.sade.viestintapalvelu.template.Template template = DocumentProviderTestData.getTemplate();
        templateService.storeTemplateDTO(template);
        
        verify(mockedTemplateDAO).insert(DocumentProviderTestData.getTemplate(1l));
    }

    @Test
    public void testFindById() {
        List<Template> mockedTemplates = new ArrayList<Template>();
        Template mockedTemplate = DocumentProviderTestData.getTemplate(1l);
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
        Template mockedTemplate = DocumentProviderTestData.getTemplate(1l);
        when(mockedTemplateDAO.findTemplate(any(TemplateCriteria.class))).thenReturn(mockedTemplate);

        fi.vm.sade.viestintapalvelu.template.Template templateFindByName = 
            templateService.getTemplateByName("test_template", "FI", true);
        
        assertNotNull(templateFindByName);
        assertTrue(templateFindByName.getId() == 1);
        assertNotNull(templateFindByName.getContents().size() == 1);
        assertNotNull(templateFindByName.getReplacements().size() == 1);
    }

    @Test
    public void testGetTemplateByNameAndType() {
        Template mockedTemplate = DocumentProviderTestData.getTemplate(1l);
        when(mockedTemplateDAO.findTemplate(any(TemplateCriteria.class))).thenReturn(mockedTemplate);

        fi.vm.sade.viestintapalvelu.template.Template templateFindByName = 
            templateService.getTemplateByName("test_template", "FI", true, "doc");
        
        assertNotNull(templateFindByName);
        assertTrue(templateFindByName.getId() == 1);
        assertNotNull(templateFindByName.getContents().size() == 1);
        assertNotNull(templateFindByName.getReplacements().size() == 1);
        assertTrue(templateFindByName.getType() != null);
        assertTrue(templateFindByName.getType().equals("doc"));
    }

    @Test
    public void testGetTemplateByCriteriaWithApplicationPeriod() {
        Template mockedTemplate = DocumentProviderTestData.getTemplate(1l);
        String applicationPeriodOid = "1234.5678910.12345";
        DocumentProviderTestData.getTemplateHaku(mockedTemplate, applicationPeriodOid);
        when(mockedTemplateDAO.findTemplate(any(TemplateCriteria.class))).thenReturn(mockedTemplate);

        fi.vm.sade.viestintapalvelu.template.Template templateFindByName =
                templateService.getTemplateByName(new TemplateCriteriaImpl()
                        .withName("test_template")
                        .withLanguage("FI")
                        .withType("doc")
                        .withApplicationPeriod(applicationPeriodOid), true);

        assertNotNull(templateFindByName);
        assertTrue(templateFindByName.getId() == 1);
        assertNotNull(templateFindByName.getContents().size() == 1);
        assertNotNull(templateFindByName.getReplacements().size() == 1);
        assertTrue(templateFindByName.getType() != null);
        assertTrue(templateFindByName.getType().equals("doc"));
    }


    @Test
    public void testGetTemplateByCriteriaWithApplicationPeriodWithFallback() {
        Template mockedTemplate = DocumentProviderTestData.getTemplate(1l),
                defaultTemplate = DocumentProviderTestData.getTemplate(2l),
                lastTemplate = DocumentProviderTestData.getTemplate(3l);
        String applicationPeriodOid = "1234.5678910.12345";
        DocumentProviderTestData.getTemplateHaku(mockedTemplate, applicationPeriodOid + "OTHER");
        defaultTemplate.setUsedAsDefault(true);
        String name = "test_template",
                lang = "FI",
                type = "doc";

        // The default should not be found:
        when(mockedTemplateDAO.findTemplate(eq(
                new TemplateCriteriaImpl()
                        .withName(name)
                        .withLanguage(lang)
                        .withType(type)
                        .withApplicationPeriod(applicationPeriodOid)
        ))).thenReturn(null);

        // The default should be found with default requirement:
        when(mockedTemplateDAO.findTemplate(eq(
                new TemplateCriteriaImpl()
                    .withName(name)
                    .withLanguage(lang)
                    .withType(type)
                    .withDefaultRequired()
        ))).thenReturn(defaultTemplate);

        // Without the default requirement, the last should be found:
        when(mockedTemplateDAO.findTemplate(eq(
                new TemplateCriteriaImpl()
                        .withName(name)
                        .withLanguage(lang)
                        .withType(type)
                        .withoutDefaultRequired()
        ))).thenReturn(lastTemplate);

        fi.vm.sade.viestintapalvelu.template.Template templateFindByName =
                templateService.getTemplateByName(new TemplateCriteriaImpl()
                        .withName(name)
                        .withLanguage(lang)
                        .withType(type)
                        .withApplicationPeriod(applicationPeriodOid), true);

        assertNotNull(templateFindByName);
        assertTrue(templateFindByName.getId() == 2);
        assertNotNull(templateFindByName.getContents().size() == 1);
        assertNotNull(templateFindByName.getReplacements().size() == 1);
        assertTrue(templateFindByName.getType() != null);
        assertTrue(templateFindByName.getType().equals("doc"));
    }


    @Test
    public void testGetTemplateByCriteriaWithoutApplicationPeriodWithFallback() {
        Template defaultTemplate = DocumentProviderTestData.getTemplate(2l),
                lastTemplate = DocumentProviderTestData.getTemplate(3l);
        defaultTemplate.setUsedAsDefault(true);
        String name = "test_template",
                lang = "FI",
                type = "doc";

        // The default should be found with default requirement:
        when(mockedTemplateDAO.findTemplate(eq(
                new TemplateCriteriaImpl(name, lang)
                        .withType(type)
                        .withDefaultRequired()
        ))).thenReturn(defaultTemplate);

        // Without the default requirement, the last should be found:
        when(mockedTemplateDAO.findTemplate(eq(
                new TemplateCriteriaImpl(name, lang)
                        .withType(type)
                        .withoutDefaultRequired()
        ))).thenReturn(lastTemplate);

        fi.vm.sade.viestintapalvelu.template.Template templateFindByName =
                templateService.getTemplateByName(new TemplateCriteriaImpl(name, lang)
                        .withType(type), true);

        assertNotNull(templateFindByName);
        assertTrue(templateFindByName.getId() == 2);
        assertNotNull(templateFindByName.getContents().size() == 1);
        assertNotNull(templateFindByName.getReplacements().size() == 1);
        assertTrue(templateFindByName.getType() != null);
        assertTrue(templateFindByName.getType().equals("doc"));
    }

    @Test
    public void testGetTemplateByCriteriaWithoutApplicationPeriodWithoutFallback() {
        Template lastTemplate = DocumentProviderTestData.getTemplate(3l);
        String name = "test_template",
                lang = "FI",
                type = "doc";

        // The default should be found with default requirement:
        when(mockedTemplateDAO.findTemplate(eq(
                new TemplateCriteriaImpl(name, lang)
                        .withDefaultRequired()
        ))).thenReturn(null);

        // Without the default requirement, the last should be found:
        when(mockedTemplateDAO.findTemplate(eq(
                new TemplateCriteriaImpl(name, lang)
                        .withType(type)
                        .withoutDefaultRequired()
        ))).thenReturn(lastTemplate);

        fi.vm.sade.viestintapalvelu.template.Template templateFindByName =
                templateService.getTemplateByName(new TemplateCriteriaImpl(name, lang)
                        .withType(type), true);

        assertNotNull(templateFindByName);
        assertTrue(templateFindByName.getId() == 3);
        assertNotNull(templateFindByName.getContents().size() == 1);
        assertNotNull(templateFindByName.getReplacements().size() == 1);
        assertTrue(templateFindByName.getType() != null);
        assertTrue(templateFindByName.getType().equals("doc"));
    }
    
    @Test
    public void fetchesOnlyPublishedTemplatesByDefault() {
        templateService.getTemplateNamesList();
        verify(mockedTemplateDAO).getAvailableTemplatesByType(State.julkaistu);
    }
    
    @Test
    public void fetchesOnlyClosedTemplates() {
        templateService.getTemplateNamesListByState(State.suljettu);
        verify(mockedTemplateDAO).getAvailableTemplatesByType(State.suljettu);
    }
    
    @Test
    public void fetchesOnlyDrafts() {
        templateService.getTemplateNamesListByState(State.julkaistu);
        verify(mockedTemplateDAO).getAvailableTemplatesByType(State.julkaistu);
    }
    
    @Test
    public void closesPublishedTemplate() {
        verifyTemplateStateChange(State.julkaistu, State.suljettu);
    }
    
    @Test
    public void closesTemplateThatIsInDraftState() {
        verifyTemplateStateChange(State.luonnos, State.suljettu);
    }
    
    @Test
    public void publishesClosedTemplate() {
        verifyTemplateStateChange(State.suljettu, State.julkaistu);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void closedTemplateCannotBeUpdatedToAnythingBesidesPublished() {
        verifyTemplateStateChange(State.suljettu, State.luonnos);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void cannotUpdatePublishedTemplateToAnythingBesidesClosed() {
        verifyTemplateStateChange(State.julkaistu, State.luonnos);
    }
    
    @Test
    public void publishesTemplateThatIsInDraftState() {
        verifyTemplateStateChange(State.luonnos, State.julkaistu);
    }
    
    @Test
    public void findsOnlyPublishedTemplateByDefautl() {
        Long id = 1l;
        when(mockedTemplateDAO.findByIdAndState(id, State.julkaistu)).thenReturn(DocumentProviderTestData.getTemplate(id));
        templateService.findById(id);
        verify(mockedTemplateDAO).findByIdAndState(id, State.julkaistu);
    }
    

    @Test
    public void updatesTemplateThatIsInDraftState() {
        ArgumentCaptor<Template> captor = ArgumentCaptor.forClass(Template.class);
        fi.vm.sade.viestintapalvelu.template.Template template = DocumentProviderTestData.getTemplate();
        template.setId(5l);
        template.setUsedAsDefault(true);
        verifyTemplateStateChange(State.luonnos, State.luonnos, captor, template);
        assertTrue(captor.getValue().isUsedAsDefault());
    }
    
    private void verifyTemplateStateChange(State oldState, State newState) {
        ArgumentCaptor<Template> captor = ArgumentCaptor.forClass(Template.class);
        fi.vm.sade.viestintapalvelu.template.Template template = DocumentProviderTestData.getTemplate();
        template.setId(7l);
        verifyTemplateStateChange(oldState, newState, captor, template);
    }

    private void verifyTemplateStateChange(State oldState, State newState, ArgumentCaptor<Template> captor, fi.vm.sade.viestintapalvelu.template.Template template) {
        template.setState(newState);
        when(mockedTemplateDAO.read(template.getId())).thenReturn(givenTemplateWithStateAndId(template.getId(), oldState));
        templateService.updateTemplate(template);
        verify(mockedTemplateDAO).update(captor.capture());
        assertEquals(newState, captor.getValue().getState());
    }
    
    private Template givenTemplateWithStateAndId(Long id, State state) {
        Template template = DocumentProviderTestData.getTemplate(id);
        template.setState(state);
        return template;
    }

}
