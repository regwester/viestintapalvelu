/*
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
 */
package fi.vm.sade.viestintapalvelu.externalinterface.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.*;
import org.springframework.stereotype.Component;

/**
 * User: ratamaa Date: 7.10.2014 Time: 12:49
 */
@Component
public interface TarjontaHakuResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/haku/find")
    HakuRDTO<List<HakuDetailsDto>> hakus(@QueryParam(value = "addHakukohdes") Boolean addHakukohdes) throws Exception;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/haku/{oid}")
    HakuRDTO<HakuDetailsDto> hakuByOid(@PathParam("oid") String oid) throws Exception;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/hakukohde/{oid}")
    HakuRDTO<HakukohdeDTO> getHakuhdeByOid(@PathParam("oid") String oid) throws Exception;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/haku/{oid}/hakukohteidenOrganisaatiot")
    HakuRDTO<List<String>> getHakuOrganizationOids(@PathParam("oid") String oid);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/hakukohde/search")
    HakuRDTO<HakutuloksetRDTO<HakukohdeTuloksetRDTO>> getHakukohteetByHakuOid(@QueryParam("hakuOid") String hakuOid);
}
