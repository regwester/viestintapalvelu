/*
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

package fi.vm.sade.viestintapalvelu.structure;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.dao.dto.StructureListDto;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureViewDto;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 14:19
 */
@Component
@PreAuthorize("isAuthenticated()")
@Path(Urls.STRUCTURE_RESOURCE_PATH)
@Api(value = "/" + Urls.API_PATH + "/" + Urls.STRUCTURE_RESOURCE_PATH, description = "Pohjan rakenne")
public class StructureResource {

    @Autowired
    private StructureService structureService;

    @GET
    @Path("/")
    @Produces("application/json;charset=utf-8")
    @ApiOperation("Listaa uusimma rakenneversiot")
    public List<StructureListDto> listLatestVersions() throws IOException {
        return structureService.findLatestStructuresVersionsForList();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    @ApiOperation("Hakee rakenteen tiedot id:llä")
    public StructureViewDto getStructureById(@ApiParam(value = "id", name = "Rakenteen id") @PathParam("id") long id) {
        return structureService.getStructure(id);
    }

    @GET
    @Path("/{name}/{language}")
    @Produces("application/json;charset=utf-8")
    @ApiOperation("Hakee viimeisimmän rakenteen tiedot nimellä ja kielellä")
    public StructureViewDto getStructureByNameAndLanguage(
            @ApiParam(value = "name", name = "Rakenteen nimi") @PathParam("name") String name,
            @ApiParam(value = "language", name = "Rakenteen kieli") @PathParam("language") String language) {
        return structureService.getLatestStructureByNameAndLanguage(name, language);
    }

}
