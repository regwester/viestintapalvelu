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
package fi.vm.sade.viestintapalvelu.externalinterface.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonView;
import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.JsonViews;
import org.springframework.stereotype.Component;

/**
 * Rajapinta oppijanumerorekisterin henkilötietoihin.
 */
@Component
@Path("henkilo")
public interface OppijanumerorekisteriHenkiloResource {

    /**
     * Hakee käyttäjän tiedot oid:n perusteella
     *
     * @param oid Henkilön OID-tunnus
     * @return Henkilön tiedot
     */
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(JsonViews.Basic.class)
    @GET
    @Path("/{oid}")
    Henkilo getHenkiloByOid(@PathParam("oid") String oid);

}
