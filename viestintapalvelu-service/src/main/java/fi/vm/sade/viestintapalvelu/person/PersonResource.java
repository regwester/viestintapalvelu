/**
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */
package fi.vm.sade.viestintapalvelu.person;

import java.util.List;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.externalinterface.component.HenkiloComponent;

/**
 * @author risal1
 *
 */
@Component
@Path("person")
@PreAuthorize("isAuthenticated()")
public class PersonResource {

    private static final String OPH_ORGANIZATION_OID = "1.2.246.562.10.00000000001";
    
    @Autowired
    private HenkiloComponent henkiloComponent;
    
    @Autowired
    private CurrentUserComponent userComponent;

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{oid}")
    public Person getHenkiloByOid(@PathParam("oid") String oid) {
        return new Person(henkiloComponent.getHenkilo(oid));
    }
    
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/userRights")
    public Rights getUserRights(@Context HttpServletRequest request) {
        return buildRights(request, userComponent.getCurrentUserOrganizations());
    }

    private Rights buildRights(HttpServletRequest request, List<OrganisaatioHenkilo> currentUserOrganizations) {
        List<String> organizations = ImmutableList.copyOf(Lists.transform(currentUserOrganizations, new Function<OrganisaatioHenkilo, String>() {
            
            @Override
            public String apply(@Nonnull OrganisaatioHenkilo input) {
                return input.getOrganisaatioOid();
            }
            
        }));
        boolean ophUser = organizations.contains(OPH_ORGANIZATION_OID);
        boolean canRead = request.isUserInRole(Constants.ROLE_APP_ASIAKIRJAPALVELU_READ);
        boolean canEditTemplate = request.isUserInRole(Constants.ASIAKIRJAPALVELU_CREATE_TEMPLATE);
        boolean canEditDraft = request.isUserInRole(Constants.ASIAKIRJAPALVELU_CREATE_LETTER);
        return new Rights(organizations, ophUser, canRead, canEditTemplate, canEditDraft);
    }
}
