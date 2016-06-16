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
package fi.vm.sade.viestintapalvelu.letter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterReceiverLetterDTO;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchDto;

@Component("LetterResourceTrusted")
@Path(Urls.TRUSTED_PATH + "/" + Urls.LETTER_PATH)
public class LetterResourceTrusted extends AbstractLetterResource {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/async/letter")
    public Response asyncLetter(@ApiParam(value = "Muodostettavien kirjeiden tiedot (1-n)", required = true) final AsyncLetterBatchDto input) {
        return createAsyncLetter(input, true);
    }

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/async/letter/status/{letterBatchId}")
    @ApiOperation(value = "Palauttaa kirjelähetykseen kuuluvien käsiteltyjen kirjeiden määrän ja kokonaismäärän")
    public Response letterBatchStatus(@PathParam("letterBatchId") @ApiParam(value = "Kirjelähetyksen id") String prefixedLetterBatchId) {
        return getLetterBatchStatus(prefixedLetterBatchId);
    }

    @GET
    @Produces("application/json")
    @Path("/list/person/{personOid}")
    @ApiOperation(value="Tuottaa listan hakijan kirjeistä")
    public Response listByPerson( @PathParam("personOid") @ApiParam(name="Henkilön OID", required = true) String personOid) {
        return super.listByPerson(personOid);
    }

    @GET
    @Produces("application/json")
    @Path("/count/{hakuOid}/type/{type}/language/{language}")
    @ApiOperation(value="Laskee haun kirjeiden valmistusstatuksen")
    public Response countReadyLetters(@PathParam("hakuOid") @ApiParam(name="Haku OID", required = true) String hakuOid,
                                      @PathParam("type") @ApiParam(name="Haku OID", required = true) String type,
                                      @PathParam("language") @ApiParam(name="Haku OID", required = true) String language) {
        return super.countLetterStatuses(hakuOid, type, language);
    }

    @GET
    @Path("/receiverLetter/{id}")
    @Produces({ "application/pdf", "application/zip" })
    @ApiOperation(value = "Hakee vastaanottajan kirjeen")
    public Response getReceiversLetter(@ApiParam(value = "Vastaanottajan kirjeen avain") @PathParam(Constants.PARAM_ID) Long id,
                                       @Context HttpServletRequest request, @Context HttpServletResponse response) {
        try {
            LetterReceiverLetterDTO letterReceiverLetter = letterService.getLetterReceiverLetter(id);
            byte[] letterContents = letterReceiverLetter.getLetter();
            return LetterDownloadHelper.downloadPdfResponse(letterReceiverLetter.getTemplateName() +
                            LetterDownloadHelper.determineExtension(letterReceiverLetter.getContentType()), response,
                    letterContents);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Constants.INTERNAL_SERVICE_ERROR).build();
        }
    }

}
