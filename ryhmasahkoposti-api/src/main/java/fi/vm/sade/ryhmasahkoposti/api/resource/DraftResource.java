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
package fi.vm.sade.ryhmasahkoposti.api.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import fi.vm.sade.ryhmasahkoposti.api.constants.SecurityConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.Draft;

@Component
@Path("drafts")
@PreAuthorize(SecurityConstants.USER_IS_AUTHENTICATED)
@Api(value = "/drafts", description = "S&auml;hk&oumlpostien luonnosten tallentaminen")
public interface DraftResource {

    /**
     * Hakee käyttäjän tallentaman luonnoksen annetun tunnisteen perusteella.
     *
     * @param id
     *            Luonnoksen tunniste
     * @return Tunnistetta vastaavan luonnoksen
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/{draftId}")
    @ApiOperation(value = "Palauttaa halutun luonnoksen tiedot", response = Draft.class)
    Draft getDraft(@PathParam(value = "draftId") Long id);

    /**
     * Hakee kaikki käyttäjän tallentamat luonnokset.
     *
     * @return Listan luonnoksista
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    List<Draft> getAllDrafts();

    /**
     * Hakee käyttäjän tallentamien luonnoksien lukumäärän.
     *
     * @return Luonnoksen tiedot
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/count")
    String getCount();

    /**
     * Poistaa käyttäjän tallentaman luonnoksen annetun tunnisteen perusteella.
     *
     * @param id
     *            Luonnoksen tunniste
     * @return Tiedon poistamisen onnistumisesta
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/{draftId}")
    String deleteDraft(@PathParam(value = "draftId") Long id) throws Exception;

    /**
     * Tallentaa annetun luonnoksen.
     *
     * @param draft
     *            Luonnos objekti
     * @return Tiedon tallentamisen onnistumisesta
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    Long saveDraft(Draft draft);

    /**
     * Muokkaa tallennettua luonnosta annetun tunnisteen ja luonnos objektin
     * perusteella.
     *
     * @param id
     *            Luonnoksen tunniste
     * @param draft
     *            Luonnos objekti
     * @return Tiedon muokkauksen onnistumisesta.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    @Path("/{draftId}")
    String updateDraft(@PathParam(value = "draftId") Long id, Draft draft) throws Exception;
}
