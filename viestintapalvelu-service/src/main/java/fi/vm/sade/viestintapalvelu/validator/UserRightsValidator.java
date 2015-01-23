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
     * @param oid
     *            Organisaation oid-tunnus
     * @return Response sisältää tilan OK (= 200), jos käyttäjällä oikeudet
     *         organisaatioon, muuten FORBIDDEN (= 403)
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
