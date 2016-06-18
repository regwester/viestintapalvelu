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
package fi.vm.sade.viestintapalvelu.externalinterface.organisaatio.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.viestintapalvelu.externalinterface.api.OrganisaatioResourceWithoutAuthentication;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyDto;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyResultsDto;

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
    @ComponentScan("fi.vm.sade.converter")
    public static class Config {
    }


}