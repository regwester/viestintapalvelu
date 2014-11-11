package fi.vm.sade.viestintapalvelu.template;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.dao.StructureDAO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.model.Structure;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.template.impl.TemplateServiceImpl;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TemplateResourceTest.Config.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class TemplateResourceTest {

    @Autowired
    private TemplateResource resource;
    
    @Autowired
    private TemplateService service;
    
    @Autowired
    private TransactionalActions actions;
    
    private Structure structure;
    
    @Before
    public void before() throws Exception {
        structure = actions.createStructure();
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
        assertNotNull(resource.store(givenTemplateWithStructure()));
    }
    
    @Test
    public void doesNotReturnTemplateNamesThatAreNotPublished() throws Exception {
        resource.store(givenTemplateWithStructure());
        assertEquals(0, resource.templateNames().size());
    }
    
    @Test
    public void returnsTemplateNamesThatAreInDraftState() throws Exception {
        resource.store(givenTemplateWithStructure());
        assertEquals(1, resource.templateNamesByState(State.luonnos).size());
    }
    
    @Test
    public void doesNotReturnTemplatesThatAreNotPublished() throws Exception {
        Template template = givenTemplateWithStructure();
        resource.store(template);
        assertTrue(resource.listVersionsByName(constructRequest(template)).isEmpty());
    }
    
    @Test
    public void returnsTemplatesThatAreInDraftState() throws Exception {
        Template template = givenTemplateWithStructure();
        resource.store(template);
        assertEquals(1, resource.listVersionsByNameUsingState(constructRequest(template), State.luonnos).size());
    }
    
    private HttpServletRequest constructRequest(Template template) {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getParameter("templateName")).thenReturn(template.getName());
        when(req.getParameter("languageCode")).thenReturn(template.getLanguage());
        when(req.getParameter("type")).thenReturn(template.getType());
        return req;
    }
    
    private Template givenTemplateWithStructure() {
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
            return dao.insert(DocumentProviderTestData.getStructureWithGivenPrefixForName("somesuch"));
        }
    }
}
