package fi.vm.sade.viestintapalvelu.externalinterface.organisaatio.impl;

import fi.vm.sade.viestintapalvelu.externalinterface.api.OrganisaatioResourceWithoutAuthentication;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyDto;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyResultsDto;
import fi.vm.sade.viestintapalvelu.externalinterface.component.OrganizationComponent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = OrganisaatioServiceImplTest.Config.class)
public class OrganisaatioServiceImplTest {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;


    @Autowired
    private OrganisaatioResourceWithoutAuthentication organisaatioResourceWithoutAuthenticationSpy;

    public static final String ROOT_OID = "rootParentOid";


    @Before
    public void setupDeps() throws Exception {
        OrganisaatioHierarchyResultsDto resultsDto = new OrganisaatioHierarchyResultsDto();
        List<OrganisaatioHierarchyDto> organisaatiot = new ArrayList<OrganisaatioHierarchyDto>();
        OrganisaatioHierarchyDto hierarchy = getHierarchy();
        organisaatiot.add(hierarchy);
        resultsDto.setOrganisaatiot(organisaatiot);
        when(organisaatioResourceWithoutAuthenticationSpy.hierarchy(eq(true))).thenReturn(resultsDto);
    }

    @Test
    public void tryCorruptCache() throws Exception {
        OrganisaatioServiceImpl impl = new OrganisaatioServiceImpl();
        beanFactory.autowireBean(impl);

        OrganisaatioHierarchyDto organizationHierarchy = impl.getOrganizationHierarchy(ROOT_OID);
        assertNotNull(organizationHierarchy);
        organizationHierarchy.getChildren().clear();

        OrganisaatioHierarchyDto newDto = impl.getOrganizationHierarchy(ROOT_OID);
        assertEquals("Cache was corrupted, children count in the cache should stay the same even though the returned object is modified (return a copy)", 3,newDto.getChildren().size());


        newDto.getChildren().get(0).setParent(null);
        newDto.getNimi().clear();

        OrganisaatioHierarchyDto cachedDto = impl.getOrganizationHierarchy(ROOT_OID);
        assertNotNull("Parent in cache was corrupted", cachedDto.getChildren().get(0).getParent());
        assertFalse("Nimi map was corrupted", cachedDto.getNimi().isEmpty());
    }



    private OrganisaatioHierarchyDto getHierarchy() {
        OrganisaatioHierarchyDto hierarchy = new OrganisaatioHierarchyDto();
        hierarchy.getNimi().put("FI", "organisaatio");
        hierarchy.getNimi().put("EN", "organization");

        hierarchy.setOid(ROOT_OID);
        List<OrganisaatioHierarchyDto> children = new ArrayList<OrganisaatioHierarchyDto>();
        children.add(new OrganisaatioHierarchyDto());
        children.add(new OrganisaatioHierarchyDto());
        children.add(new OrganisaatioHierarchyDto());

        for (int i = 0; i < children.size(); i++) {
            children.get(i).setOid("oid".concat(String.valueOf(i)));
            children.get(i).setParentOid(hierarchy.getOid());
            children.get(i).setParent(hierarchy);
            children.get(i).getNimi().put("FI", "lapsiorganisaatio".concat(String.valueOf(i)));
            children.get(i).getNimi().put("EN", "child organisaatio".concat(String.valueOf(i)));
        }

        hierarchy.setChildren(children);
        return hierarchy;
    }


    @Configuration
    @ImportResource(value = "classpath:test-application-context.xml")
    public static class Config {
    }


}