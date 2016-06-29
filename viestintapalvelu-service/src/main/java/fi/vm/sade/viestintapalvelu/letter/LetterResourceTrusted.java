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

import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.dao.dto.LetterBatchStatusDto;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterReceiverLetterDTO;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Component("LetterResourceTrusted")
@Path(Urls.TRUSTED_PATH + "/" + Urls.LETTER_PATH)
public class LetterResourceTrusted extends AbstractLetterResource {

    private final Logger LOG = LoggerFactory.getLogger(LetterResourceTrusted.class);

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

    @GET
    @Produces("text/plain")
    @Path("/publishLetterBatch/{letterBatchId}")
    @ApiOperation(value = "Julkaisee kirjelähetyksen")
    public Response publishLetterBatch(@PathParam("letterBatchId") @ApiParam(value = "Kirjelähetyksen id") String prefixedLetterBatchId) {
        long letterBatchId = getLetterBatchId(prefixedLetterBatchId);
        LOG.info("Publishing letter batch with batch id {}", letterBatchId);
        LetterBatchStatusDto status = letterService.getBatchStatus(letterBatchId);
        if(null == status) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if ( !isLetterBatchStatusReady(status)) {
            LOG.info("Batch with id {} is not ready for publish. ", letterBatchId);
            return Response.status(Response.Status.FORBIDDEN).entity("Batch is not ready for publish.").build();
        } else {
            int numberOfPublishedLetters = letterService.publishLetterBatch(letterBatchId);
            LOG.info("Published {} letters with batch id {}", numberOfPublishedLetters, letterBatchId);
            return Response.ok(numberOfPublishedLetters).build();
        }
    }

    @GET
    @Produces("text/plain")
    @Path("/getBatchIdReadyForPublish")
    @ApiOperation(value = "Palauttaa viimeisimmän kirjelähetyksen ID:n, jos sitä ei ole vielä julkaistu")
    public Response getLetterBatchIdReadyForPublish(@QueryParam("hakuOid") @ApiParam(value = "Haku OID", required = true) String hakuOid,
                                                    @QueryParam("type") @ApiParam(value = "Kirjelähetyksen tyyppi (hyvaksymiskirje/jalkiohjauskirje)", required = true) String type,
                                                    @QueryParam("language") @ApiParam(value = "Kirjelähetyksen kieli", required = true) String language) {
        Optional<Long> batchId = letterService.getLetterBatchIdReadyForPublish(hakuOid, type, language);
        return batchId.isPresent() ? Response.ok(batchId.get()).build() : Response.status(Response.Status.NOT_FOUND).entity("Unable to find batch id.").build();
    }

    @GET
    @Produces("text/plain")
    @Path("/getBatchIdReadyForEPosti")
    @ApiOperation(value = "Palauttaa viimeisimmän kirjelähetyksen ID:n, jos se on jo julkaistu")
    public Response getLetterBatchIdReadyForEPosti(@QueryParam("hakuOid") @ApiParam(value = "Haku OID", required = true) String hakuOid,
                                                   @QueryParam("type") @ApiParam(value = "Kirjelähetyksen tyyppi (hyvaksymiskirje/jalkiohjauskirje)", required = true) String type,
                                                   @QueryParam("language") @ApiParam(value = "Kirjelähetyksen kieli", required = true) String language) {
        Optional<Long> batchId = letterService.getLetterBatchIdReadyForEPosti(hakuOid, type, language);
        return batchId.isPresent() ? Response.ok(batchId.get()).build() : Response.status(Response.Status.NOT_FOUND).entity("Unable to find batch id.").build();
    }

    @GET
    @Produces("application/json")
    @Path("/getEPostiAddressesForLetterBatch/{letterBatchId}")
    @ApiOperation(value = "Palauttaa kirjelähetyksen vastaanottajien ePosti-osoitteet (vain julkaistuille kirjeille)")
    public Response getEPostiEmailAddresses(@PathParam("letterBatchId") @ApiParam(value = "Kirjelähetyksen id") String prefixedLetterBatchId) {
        long letterBatchId = getLetterBatchId(prefixedLetterBatchId);
        Map<String, String> applicationOidToEmailAddress = letterService.getEPostiEmailAddresses(letterBatchId);
        return Response.ok(applicationOidToEmailAddress).build();
    }
}
