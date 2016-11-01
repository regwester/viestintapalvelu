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
package fi.vm.sade.viestintapalvelu.organization;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.externalinterface.api.OrganisaatioResourceWithoutAuthentication;
import fi.vm.sade.viestintapalvelu.externalinterface.api.TarjontaHakuResource;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuDetailsDto;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuRDTO;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakukohdeDTO;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyDto;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyResultsDto;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;


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
    OrganisaatioResourceWithoutAuthentication organisaatioResourceWithoutAuthenticationClientMock;

    @Autowired
    TarjontaHakuResource tarjontaHakuResourceMock;


    @Test
    public void testGetOrganizationHierarchy() throws Exception {

        OrganisaatioHierarchyResultsDto result = new OrganisaatioHierarchyResultsDto();
        ArrayList<OrganisaatioHierarchyDto> organisaatioHierarchyDtos = new ArrayList<OrganisaatioHierarchyDto>();
        OrganisaatioHierarchyDto organisaatioHierarchyDto = new OrganisaatioHierarchyDto();
        organisaatioHierarchyDto.setOid(organisaatioOid);

        ArrayList<OrganisaatioHierarchyDto> children = new ArrayList<OrganisaatioHierarchyDto>();
        final OrganisaatioHierarchyDto child1 = new OrganisaatioHierarchyDto();
        final OrganisaatioHierarchyDto child2 = new OrganisaatioHierarchyDto();

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

        //TODO this necessarily doesn't reflect the methods that tarjonta v1 api will provide
        HakuDetailsDto details = new HakuDetailsDto();
        final ArrayList<String> strings = new ArrayList<String>();
        strings.add("child1");
        details.setOrganisaatioOids(strings);
        details.setTarjoajaOids(strings);
        final HakuRDTO<HakuDetailsDto> rdto = new HakuRDTO<HakuDetailsDto>();
        rdto.setResult(details);

        HakuRDTO<HakukohdeDTO> kohdeRDTO = new HakuRDTO<HakukohdeDTO>();
        HakukohdeDTO kohde = new HakukohdeDTO();
        Set<String> tarjoajaoids = new HashSet<>();
        tarjoajaoids.add(child1.getOid());
        kohde.setTarjoajaOids(tarjoajaoids);
        kohdeRDTO.setResult(kohde);

        when(tarjontaHakuResourceMock.getHakuhdeByOid(eq("child1"))).thenReturn(kohdeRDTO);
        final HakuRDTO<List<String>> listHakuRDTO = new HakuRDTO<List<String>>();
        List<String> providerOrgOids = new ArrayList<>();
        providerOrgOids.addAll(tarjoajaoids);
        listHakuRDTO.setResult(providerOrgOids);
        when(tarjontaHakuResourceMock.getHakuOrganizationOids(eq(applicationPeriod))).thenReturn(listHakuRDTO);

        when(tarjontaHakuResourceMock.hakuByOid(eq(applicationPeriod))).thenReturn(rdto);
        when(organisaatioResourceWithoutAuthenticationClientMock.hierarchy(eq(true))).thenReturn(result);

        List<OrganisaatioHierarchyDto> applicationPeriodHere = organizationResource.getOrganizationHierarchy(applicationPeriod);
        assertEquals("resource should return one organization hierarchy", 1,applicationPeriodHere.size());

        assertEquals("Child count was wrong, filtering didn't work", 1, applicationPeriodHere.get(0).getChildren().size());
        assertEquals("Child name was wrong", name, applicationPeriodHere.get(0).getChildren().get(0).getNimi());
    }


    @Configuration
    @ImportResource(value = "classpath:test-application-context.xml")
    @ComponentScan(value = { "fi.vm.sade.converter", "fi.vm.sade.externalinterface" })
    public static class Config {

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