package fi.vm.sade.viestintapalvelu.organization;

import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.externalinterface.api.OrganisaatioResourceWithoutAuthentication;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.LOPDto;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyDto;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyResultsDto;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.externalinterface.component.LearningOpportunityProviderComponent;
import fi.vm.sade.viestintapalvelu.externalinterface.component.OrganizationComponent;
import fi.vm.sade.viestintapalvelu.externalinterface.organisaatio.OrganisaatioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {OrganizationResourceTest.Config.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrganizationResourceTest {

    public static final String applicationPeriod = "appPeriodHere";
    public static final String organisaatioOid = "organisaatioOid";

    @Autowired
    OrganizationResource organizationResource;

    @Autowired
    OrganisaatioService organisaatioServiceSpy;

    @Autowired
    OrganizationComponent organizationComponentSpy;

    @Autowired
    OrganisaatioResourceWithoutAuthentication organisaatioResourceWithoutAuthenticationClientSpy;


    @Test
    public void testGetOrganizationHierarchy() throws Exception {

        OrganisaatioHierarchyResultsDto result = new OrganisaatioHierarchyResultsDto();
        ArrayList<OrganisaatioHierarchyDto> organisaatioHierarchyDtos = new ArrayList<OrganisaatioHierarchyDto>();
        OrganisaatioHierarchyDto organisaatioHierarchyDto = new OrganisaatioHierarchyDto();
        organisaatioHierarchyDto.setOid(organisaatioOid);

        ArrayList<OrganisaatioHierarchyDto> children = new ArrayList<OrganisaatioHierarchyDto>();
        OrganisaatioHierarchyDto child1 = new OrganisaatioHierarchyDto();
        OrganisaatioHierarchyDto child2 = new OrganisaatioHierarchyDto();

        child1.setOid("child1");
        Map<String, String> name = new HashMap<String, String>();
        name.put("fi", "test org name");
        child1.setNimi(name);
        child2.setOid("child2");
        children.add(child1);
        children.add(child2);

        organisaatioHierarchyDto.setChildren(children);

        organisaatioHierarchyDtos.add(organisaatioHierarchyDto);
        result.setOrganisaatiot(organisaatioHierarchyDtos);

        when(organisaatioResourceWithoutAuthenticationClientSpy.hierarchy(eq(true))).thenReturn(result);

        List<OrganisaatioHierarchyDto> applicationPeriodHere = organizationResource.getOrganizationHierarchy(applicationPeriod);
        assertEquals("resource should return one organization hierarchy", 1,applicationPeriodHere.size());

        assertEquals("Child count was wrong, filtering didn't work", 1, applicationPeriodHere.get(0).getChildren().size());
        assertEquals("Child name was wrong", name, applicationPeriodHere.get(0).getChildren().get(0).getNimi());
    }


    @Configuration
    @ImportResource(value = "classpath:test-application-context.xml")
    public static class Config {

        @Bean
        LearningOpportunityProviderComponent learningOpportunityProviderComponent() {
            LearningOpportunityProviderComponent mock = mock(LearningOpportunityProviderComponent.class);
            List<LOPDto> list = new ArrayList<LOPDto>();
            LOPDto lopDto = new LOPDto();
            lopDto.setId("child1");
            lopDto.setName("test org name");
            lopDto.setProviderOrg(true);
            list.add(lopDto);
            when(mock.searchProviders(eq(applicationPeriod), any(Locale.class))).thenReturn(list);
            return mock;
        }

        @Bean
        CurrentUserComponent currentUserComponent() {
            CurrentUserComponent mock = mock(CurrentUserComponent.class);
            List<OrganisaatioHenkilo> orgList = new ArrayList<OrganisaatioHenkilo>();
            OrganisaatioHenkilo org = mock(OrganisaatioHenkilo.class);
            when(org.getOrganisaatioOid()).thenReturn(organisaatioOid);
            orgList.add(org);
            when(mock.getCurrentUserOrganizations()).thenReturn(orgList);
            return mock;
        }
    }
}