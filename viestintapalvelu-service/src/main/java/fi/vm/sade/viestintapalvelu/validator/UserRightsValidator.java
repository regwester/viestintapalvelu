package fi.vm.sade.viestintapalvelu.validator;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.externalinterface.component.OrganizationComponent;

/**
 * Validator-luokka käyttäjäoikeuksien tarkistamisia varten
 * 
 * @author vehei1
 *
 */
@Component
public class UserRightsValidator {
    private CurrentUserComponent currentUserComponent;
    private OrganizationComponent organizationComponent;

    @Autowired
    public UserRightsValidator(CurrentUserComponent currentUserComponent, OrganizationComponent organizationComponent) {
        this.currentUserComponent = currentUserComponent;
        this.organizationComponent = organizationComponent;
    }
   
    /**
     * Tarkistaa käyttäjän oikeudet anettuun organisaatioon
     * 
     * @param oid Organisaation oid-tunnus
     * @return Response sisältää tilan OK (= 200), jos käyttäjällä oikeudet organisaatioon, muuten FORBIDDEN (= 403)
     */
    public Response checkUserRightsToOrganization(String oid) {
        if (oid == null) {
            return Response.status(Status.OK).build();
        }
        
        List<String> organizationParents = organizationComponent.getOrganizationParents(oid);        
        List<OrganisaatioHenkilo> organisaatioHenkiloList = currentUserComponent.getCurrentUserOrganizations();
        
        for (OrganisaatioHenkilo organisaatioHenkilo : organisaatioHenkiloList) {
            if (organizationParents.contains(organisaatioHenkilo.getOrganisaatioOid())) {
                return Response.status(Status.OK).build();
            }
        }
        
        return Response.status(Status.FORBIDDEN).entity("User is not authorized to the organization " + oid).build();
    }
}
