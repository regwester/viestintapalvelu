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
package fi.vm.sade.viestintapalvelu.template;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fi.vm.sade.externalinterface.KayttooikeusRestClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hk2.annotations.Service;
import org.mockito.Mockito;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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

import com.lowagie.text.DocumentException;
import fi.vm.sade.dto.HenkiloDto;

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
import fi.vm.sade.viestintapalvelu.validator.UserRightsValidator;
import static fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData.content;
import static fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData.contentStructure;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

    @Autowired
    private KayttooikeusRestClient kayttooikeusRestClient;
    
    @Before
    public void before() throws Exception {
        final HenkiloDto testHenkilo = DocumentProviderTestData.getHenkilo();
        CurrentUserComponent currentUserComponent = new CurrentUserComponent(kayttooikeusRestClient) {
            @Override
            public String getCurrentUser() {
                return testHenkilo.getOidHenkilo();
            }
        };
        Field currentUserComponentField = TemplateServiceImpl.class.getDeclaredField("currentUserComponent");
        currentUserComponentField.setAccessible(true);
        currentUserComponentField.set(((Advised)service).getTargetSource().getTarget(), currentUserComponent);
        UserRightsValidator validator = new UserRightsValidator(null, null) {
            @Override
            public Response checkUserRightsToOrganization(String oid) {
                return Response.status(Status.OK).build();
            }
        };
        Field validatorField = TemplateResource.class.getDeclaredField("userRightsValidator");
        validatorField.setAccessible(true);
        validatorField.set(((Advised)resource).getTargetSource().getTarget(), validator);
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
    
    @Test
    public void fetchesDefaultTemplates() throws Exception {
        givenSavedDefaultTemplateWithStatus(State.julkaistu, true);
        List<TemplateInfo> templates = resource.getDefaultTemplates(State.julkaistu);
        assertEquals(1, templates.size());
        assertEquals(State.julkaistu, templates.get(0).state);
        assertTrue(resource.getDefaultTemplates(State.luonnos).isEmpty());
    }
    
    @Test
    public void fetchesDefaultTemplatesUsingDraftState() throws Exception {
        givenSavedDefaultTemplateWithStatus(State.luonnos, true);
        List<TemplateInfo> templates = resource.getDefaultTemplates(State.luonnos);
        assertEquals(1, templates.size());
        assertEquals(State.luonnos, templates.get(0).state);
    }
    
    @Test
    public void fetchesTemplatesByUsingApplicationPeriod() throws Exception {
        String hakuOid = "1.9.3.4.200";
        givenSavedTemplateWIthApplicationPeriodAndState(hakuOid, State.julkaistu);
        assertEquals(1, resource.getTemplatesByApplicationPeriodAndState(hakuOid, State.julkaistu).size());
        assertTrue(resource.getTemplatesByApplicationPeriodAndState(hakuOid, State.luonnos).isEmpty());
        assertTrue(resource.getTemplatesByApplicationPeriodAndState("12334.23", State.julkaistu).isEmpty());
    }
    
    @Test
    public void listsTemplatesByApplicationPeriod() throws Exception {
        String hakuOid = "1.9.3.4.213323";
        givenSavedTemplateWithApplicationPeriodAndStatus(hakuOid, State.julkaistu);
        TemplatesByApplicationPeriod dto = resource.listTemplatesByApplicationPeriod(hakuOid);
        assertEquals(1, dto.publishedTemplates.size());
        assertTrue(dto.closedTemplates.isEmpty());
        assertTrue(dto.draftTemplates.isEmpty());
        assertEquals(hakuOid, dto.hakuOid);
    }
    
    private Template givenSavedTemplateInDraftStatus() throws Exception{
        return givenSavedTemplateWithStatus(State.luonnos);
    }

    private Template givenSavedTemplateWithStatus(State state) throws IOException, DocumentException {
        return givenSavedDefaultTemplateWithStatus(state, false);
    }

    private Template givenSavedDefaultTemplateWithStatus(State state, boolean usedAsDefault) throws IOException, DocumentException {
        Template template = givenTemplateWithStructure();
        template.setUsedAsDefault(usedAsDefault);
        template.setState(state);
        Long id = (Long) resource.insert(template).getEntity();
        return resource.getTemplateByIDAndState(id, state, null);
    }
    
    private Template givenSavedTemplateWithApplicationPeriodAndStatus(String applicationPeriod, State state) throws IOException, DocumentException {
        Template template = givenTemplateWithStructure();
        template.setApplicationPeriods(Arrays.asList(applicationPeriod));
        template.setState(state);
        Long id = (Long) resource.insert(template).getEntity();
        return resource.getTemplateByIDAndState(id, state, null);
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
        template.setStructure(null);
        template.setStructureId(structure.getId());
        template.setStructureName(structure.getName());
        return template;
    }
    
    private Template givenSavedTemplateWIthApplicationPeriodAndState(String hakuOid, State state) throws IOException, DocumentException {
        Template template = givenTemplateWithStructure();
        template.setApplicationPeriods(Arrays.asList(hakuOid));
        template.setState(state);
        Long id = (Long) resource.insert(template).getEntity();
        return resource.getTemplateByIDAndState(id, state, null);
    }
    
    @Configuration
    @ImportResource(value = "classpath:test-application-context.xml")
    @ComponentScan(value = { "fi.vm.sade.converter", "fi.vm.sade.externalinterface" })
    public static class Config {
        @Bean
        public TransactionalActions transactionalActions() {
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
