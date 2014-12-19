package fi.vm.sade.viestintapalvelu.person;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.when;

import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;

@RunWith(MockitoJUnitRunner.class)
public class PersonResourceTest {
    
    @Mock
    private CurrentUserComponent userComponent;
   
    @InjectMocks
    private PersonResource resource = new PersonResource();
    
    @Test
    public void returnsRightsOfCurrentUser() {
        HttpServletRequest request = givenMockedRequest();
        final String orgOid = "1.2.3.45434";
        when(userComponent.getCurrentUserOrganizations()).thenReturn(Arrays.asList(givenOrganisaatioHenkiloWithOid(orgOid)));
        Rights rights = resource.getUserRights(request);
        assertTrue(rights.rightToEditDrafts && rights.rightToEditTemplates && rights.rightToViewTemplates);
        assertTrue(rights.organizationOids.contains(orgOid));
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
