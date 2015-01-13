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
package fi.vm.sade.viestintapalvelu.person;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersonResourceTest {
    
    private static final String OPH_ORGANIZATION_OID = "1.2.246.562.10.00000000001";
    
    @Mock
    private CurrentUserComponent userComponent;
   
    @InjectMocks
    private PersonResource resource = new PersonResource();
    
    @Test
    public void returnsRightsOfCurrentUser() {
        final String orgOid = "1.2.3.45434";
        when(userComponent.getCurrentUserOrganizations()).thenReturn(Arrays.asList(givenOrganisaatioHenkiloWithOid(orgOid)));
        assertRights(false, orgOid, resource.getUserRights(givenMockedRequest()));
    }

    @Test
    public void returnsRightsOfCurrentOphUser() {
        when(userComponent.getCurrentUserOrganizations()).thenReturn(Arrays.asList(givenOrganisaatioHenkiloWithOid(OPH_ORGANIZATION_OID)));
        assertRights(true, OPH_ORGANIZATION_OID, resource.getUserRights(givenMockedRequest()));
    }

    private void assertRights(boolean isOphUser, String orgOid, Rights rights) {
        assertTrue(rights.rightToEditDrafts && rights.rightToEditTemplates && rights.rightToViewTemplates);
        assertTrue(rights.organizationOids.contains(orgOid));
        assertTrue(isOphUser == rights.ophUser);
    }

    private HttpServletRequest givenMockedRequest() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.isUserInRole(any(String.class))).thenReturn(true);
        return req;
    }

    private OrganisaatioHenkilo givenOrganisaatioHenkiloWithOid(String orgOid) {
        OrganisaatioHenkilo orgHen = new OrganisaatioHenkilo();
        orgHen.setOrganisaatioOid(orgOid);
        return orgHen;
    }
    
    
}
