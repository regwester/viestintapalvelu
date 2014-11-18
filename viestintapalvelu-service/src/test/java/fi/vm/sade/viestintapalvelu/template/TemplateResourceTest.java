package fi.vm.sade.viestintapalvelu.template;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hk2.annotations.Service;
import org.mockito.Mockito;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.dao.StructureDAO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.model.Structure;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.model.types.ContentRole;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.model.types.ContentType;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureContentSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureSaveDto;
import fi.vm.sade.viestintapalvelu.template.impl.TemplateServiceImpl;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData.content;
import static fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData.contentStructure;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TemplateResourceTest.Config.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TemplateResourceTest {

    @Autowired
    private TemplateResource resource;
    
    @Autowired
    private TemplateService service;
    
    @Autowired
    private TransactionalActions actions;
    
    @Before
    public void before() throws Exception {
        final Henkilo testHenkilo = DocumentProviderTestData.getHenkilo();
        CurrentUserComponent currentUserComponent = new CurrentUserComponent() {
            @Override
            public Henkilo getCurrentUser() {
                return testHenkilo;
            }
        };
        Field currentUserComponentField = TemplateServiceImpl.class.getDeclaredField("currentUserComponent");
        currentUserComponentField.setAccessible(true);
        currentUserComponentField.set(((Advised)service).getTargetSource().getTarget(), currentUserComponent);
    }
    
    @Test
    public void insertsTemplate() throws Exception {
        assertNotNull(resource.insert(givenTemplateWithStructure()));
    }
    
    @Test
    public void doesNotReturnTemplateNamesThatAreNotPublished() throws Exception {
        resource.insert(givenTemplateWithStructure());
        assertEquals(0, resource.templateNames().size());
    }

    @Test
    public void storesStructureRelationByName() throws Exception {
        Structure structure = actions.createStructure();
        Template template = DocumentProviderTestData.getTemplate();
        template.setStructureId(null);
        template.setStructureName(structure.getName());
        resource.insert(template);
        assertEquals(0, resource.templateNames().size());
    }

    @Test
    public void storesNewStructure() throws Exception {
        Template template = DocumentProviderTestData.getTemplate();
        template.setStructureId(null);
        template.setStructureName(null);
        StructureSaveDto structureDto = new StructureSaveDto();
        structureDto.setName("rakenne");
        structureDto.setLanguage("FI");
        ContentStructureSaveDto contentStructure = new ContentStructureSaveDto();
        contentStructure.setType(ContentStructureType.letter);
        contentStructure.getContents().add(new ContentStructureContentSaveDto(
                ContentRole.body, "email_sisalto", ContentType.html,
                "<html><head><title>T</title></head><body>B</body></html>"));
        structureDto.getContentStructures().add(contentStructure);
        template.setStructure(structureDto);

        resource.insert(template);
        assertEquals(0, resource.templateNames().size());
    }

    @Test(expected = BadRequestException.class)
    public void structureShouldBeValidated() throws Exception {
        Template template = DocumentProviderTestData.getTemplate();
        template.setStructureId(null);
        template.setStructureName(null);
        template.setStructure(new StructureSaveDto());
        resource.insert(template);
    }

    @Test
    public void returnsTemplateNamesThatAreInDraftState() throws Exception {
        resource.insert(givenTemplateWithStructure());
        assertEquals(1, resource.templateNamesByState(State.luonnos).size());
    }
    
    @Test
    public void doesNotReturnTemplatesThatAreNotPublished() throws Exception {
        Template template = givenTemplateWithStructure();
        resource.insert(template);
        assertTrue(resource.listVersionsByName(constructRequest(template)).isEmpty());
    }
    
    @Test
    public void returnsTemplatesThatAreInDraftState() throws Exception {
        Template template = givenTemplateWithStructure();
        resource.insert(template);
        assertEquals(1, resource.listVersionsByNameUsingState(constructRequest(template), State.luonnos).size());
    }
    
    @Test
    public void publishesTemplate() throws Exception {
        Template template = givenSavedTemplateInDraftStatus();
        assertTrue(resource.templateNames().isEmpty());
        template.setState(State.julkaistu);
        assertEquals(Status.OK.getStatusCode(), resource.update(template).getStatus());
        assertEquals(1, resource.templateNames().size());
    }
    
    @Test
    public void closesTemplate() throws Exception {
        Template template = givenSavedTemplateInDraftStatus();
        assertTrue(resource.templateNamesByState(State.suljettu).isEmpty());
        template.setState(State.suljettu);
        assertEquals(Status.OK.getStatusCode(), resource.update(template).getStatus());
        assertEquals(1, resource.templateNamesByState(State.suljettu).size());
    }
    
    @Test
    public void fetchesOnlyPublishedTemplatesUsingNames() throws Exception {
        Template template = givenSavedTemplateInDraftStatus();
        assertNull(resource.templateByName(constructRequest(template)));
        template.setState(State.julkaistu);
        resource.update(template);
        assertNotNull(resource.templateByName(constructRequest(template)));
    }
    
    @Test
    public void fetchesClosedTemplatesUsingNamesAndState() throws Exception {
        Template template = givenSavedTemplateInDraftStatus();
        assertNull(resource.templateByNameAndState(constructRequest(template), State.suljettu));
        template.setState(State.suljettu);
        resource.update(template);
        assertNotNull(resource.templateByNameAndState(constructRequest(template), State.suljettu));
    }
    
    @Test
    public void fetchesDraftTemplateUsingNameAndState() throws Exception {
        Template template = givenSavedTemplateInDraftStatus();
        assertNotNull(resource.templateByNameAndState(constructRequest(template), State.luonnos));
    }
    
    private Template givenSavedTemplateInDraftStatus() throws Exception{
        Long id = (Long) resource.insert(givenTemplateWithStructure()).getEntity();
        return resource.getTemplateByIDAndState(id, State.luonnos, null);
    }
    
    private HttpServletRequest constructRequest(Template template) {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getParameter("templateName")).thenReturn(template.getName());
        when(req.getParameter("languageCode")).thenReturn(template.getLanguage());
        when(req.getParameter("type")).thenReturn(template.getType());
        when(req.getParameter("content")).thenReturn("true");
        return req;
    }
    
    private Template givenTemplateWithStructure() {
        Structure structure = actions.createStructure();
        Template template = DocumentProviderTestData.getTemplate();
        template.setStructureId(structure.getId());
        template.setStructureName(structure.getName());
        return template;
    }
    
    @Configuration
    @ImportResource(value = "classpath:test-application-context.xml")
    public static class Config {
        @Bean
        TransactionalActions transactionlActions() {
            return new TransactionalActions();
        }
    }

    @Service
    @Transactional
    public static class TransactionalActions {
        @Autowired
        private StructureDAO dao;

        public Structure createStructure() {
            return dao.insert(DocumentProviderTestData.getStructureWithGivenPrefixForName("somesuch",
                    contentStructure(ContentStructureType.letter,
                            content(ContentRole.body, ContentType.html))
            ));
        }
    }
}
