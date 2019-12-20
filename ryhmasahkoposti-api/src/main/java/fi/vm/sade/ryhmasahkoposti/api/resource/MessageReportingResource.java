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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.security.access.prepost.PreAuthorize;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fi.vm.sade.ryhmasahkoposti.api.constants.RestConstants;
import fi.vm.sade.ryhmasahkoposti.api.constants.SecurityConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessagesDTO;
import org.springframework.stereotype.Component;

/**
 * REST-rajapinta ryhmäsähköpostien selailua varten
 *
 * @author vehei1
 *
 */
@Component
@PreAuthorize(SecurityConstants.USER_IS_AUTHENTICATED)
@Path(RestConstants.PATH_REPORT_MESSAGES)
@Api(value = "reportMessages", description = "Ryhm&auml;s&auml;hk&ouml;postin raportointi")
public interface MessageReportingResource {
    /**
     * Hakee käyttäjän ja hänen organisaationsa lähettämät ryhmäshköpostiviestit
     *
     * @param organizationOid Organisaation oid-tunnus
     * @param nbrOfRows Haettavien ryhmäsähköpostiviestien lukumäärä
     * @param page Sivu, jolle halutaan siirtyä katselemaan viestejä
     * @param sortedBy Sarake, minkä mukaan lajittelu suoritetaan
     * @param order Lajittelujärjestys
     * @return Tiedot raportoitavista ryhmäsähköpostiviesteistä {@link ReportedMessagesDTO}
     */
    @PreAuthorize(SecurityConstants.READ)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(RestConstants.PATH_REPORT_MESSAGES_LIST)
    @GET
    @ApiOperation(value = "Hakee käyttäjän ja hänen organisaationsa lähettämät ryhmäshköpostiviestit",
        notes = "Hakee halutun määrän käyttäjän ja hänen organisaantionsa lähettämiä ryhmäshköpostiviestit. Haku voidaan"
            + "aloittaa tietystä kohtaa ja ne voidaan hakea lajiteltuna nousevasti tai laskevasti tietyn sarakkeen mukaan.",
        response = ReportedMessagesDTO.class, responseContainer = "List")
    @ApiResponses(value={@ApiResponse(code=500, message = "Internal service error tai ilmoitus liittymävirheestä")})
    Response getReportedMessages(@ApiParam(value = "Organisaation oid-tunnus", required = false)
                                 @QueryParam(RestConstants.PARAM_ORGANIZATION_OID) String organizationOid,
                                 @ApiParam(value = "Haettavien rivien lukumäärä", required = true)
                                 @QueryParam(RestConstants.PARAM_NUMBER_OF_ROWS) Integer nbrOfRows,
                                 @ApiParam(value = "Sivu, mistä kohdasta haluttu määrä rivejä haetaan", required = true)
                                 @QueryParam(RestConstants.PARAM_PAGE) Integer page,
                                 @ApiParam(value = "Taulun sarake, minkä mukaan tiedot lajitellaan", required = false)
                                 @QueryParam(RestConstants.PARAM_SORTED_BY) String sortedBy,
                                 @ApiParam(value = "Lajittelujärjestys", allowableValues = "asc, desc", required = false)
                                 @QueryParam(RestConstants.PARAM_ORDER) String order,
                                 @Context HttpServletRequest request) throws Exception;

    /**
     * Hakee hakuparametrin mukaiset viestit käyttäjän ja hänen organisaationsa lähettämistä ryhmäshköpostiviesteistä
     *
     * @param organizationOid Organisaation oid-tunnus
     * @param hakuKentta Käyttäjän antama hakuparametri
     * @param nbrOfRows Haettavien ryhmäsähköpostiviestien lukumäärä
     * @param page Sivu, jolle halutaan siirtyä katselemaan viestejä
     * @param sortedBy Sarake, minkä mukaan lajittelu suoritetaan
     * @param order Lajittelujärjestys
     * @return Tiedot raportoitavista ryhmäsähköpostiviesteistä {@link ReportedMessagesDTO}
     */
    @PreAuthorize(SecurityConstants.READ)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(RestConstants.PATH_REPORT_MESSAGES_SEARCH)
    @GET
    @ApiOperation(value = "Hakee hakuparametrin mukaiset käyttäjän ja hänen organisaationsa lähettämät ryhmäshköpostiviestit",
        notes = "Hakee halutun määrän  hakuparametrin mukaisia käyttäjän ja hänen organisaantionsa lähettämiä "
            + "ryhmäshköpostiviestejä. Haku voidaan aloittaa tietystä kohtaa ja ne voidaan hakea lajiteltuna nousevasti "
            + "tai laskevasti tietyn sarakkeen mukaan.",
        response = ReportedMessagesDTO.class, responseContainer = "List")
    @ApiResponses(value={@ApiResponse(code=500, message = "Internal service error tai ilmoitus liittymävirheestä")})
    Response getReportedMessages(@ApiParam(value = "Organisaation oid-tunnus", required = false)
                                 @QueryParam(RestConstants.PARAM_ORGANIZATION_OID) String organizationOid,
                                 @ApiParam(value = "Annettu hakuparametri esim. sosiaaliturvatunnus", required = false)
                                 @QueryParam(RestConstants.PARAM_SEARCH_ARGUMENT) String searchArgument,
                                 @ApiParam(value = "Haettavien rivien lukumäärä", required = true)
                                 @QueryParam(RestConstants.PARAM_NUMBER_OF_ROWS) Integer nbrOfRows,
                                 @ApiParam(value = "Sivu, mistä kohdasta haluttu määrä rivejä haetaan", required = true)
                                 @QueryParam(RestConstants.PARAM_PAGE) Integer page,
                                 @ApiParam(value = "Taulun sarake, minkä mukaan tiedot lajitellaan", required = false)
                                 @QueryParam(RestConstants.PARAM_SORTED_BY) String sortedBy,
                                 @ApiParam(value = "Lajittelujärjestys", allowableValues = "asc, desc", required = false)
                                 @QueryParam(RestConstants.PARAM_ORDER) String order,
                                 @Context HttpServletRequest request) throws Exception;

    /**
     * Hakee hakuparametrin mukaiset viestit käyttäjän ja hänen organisaationsa lähettämistä ryhmäshköpostiviesteistä
     *
     * @param process Prosessi, jonka kautta lähetettyihin viesteihin kysely halutaan rajata
     * @return Tiedot käyttäjän lähettämistä ryhmäsähköpostiviesteistä {@link ReportedMessagesDTO}
     */
    @PreAuthorize(SecurityConstants.READ)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(RestConstants.PATH_REPORT_MESSAGES_CURRENT_USER)
    @GET
    @ApiOperation(value = "Hakee tämänhetkisen käyttäjän ja lähettämät ryhmäshköpostiviestit",
        notes = "Hakee kaikki käyttäjän lähettämät ryhmäsähköpostiviestit.",
        response = ReportedMessagesDTO.class, responseContainer = "List")
    @ApiResponses(value={@ApiResponse(code=500, message = "Internal service error tai ilmoitus liittymävirheestä")})
    Response getReportedMessagesSentByCurrentUser(
            @ApiParam(value = "Viestin lähettänyt prosessi, esim. Osoitepalvelujarjestelma", required = false)
            @QueryParam(RestConstants.PARAM_PROCESS) String process) throws Exception;

    /**
     * Hakee yksittäisen ryhmäsähköpostiviestin tiedot
     *
     * @param messageID Ryhmäsähköpostiviestin tunnus
     * @return Raportoitavan ryhmäsähköpostin tiedot {@link ReportedMessageDTO}}
     */
    @PreAuthorize(SecurityConstants.READ)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(RestConstants.PATH_REPORT_MESSAGE_VIEW)
    @GET
    @ApiOperation(value = "Hakee halutun ryhmäsähköpostiviestin tiedot",
        notes = "Hakee avainta vastaavaa ryhmäsähköpostiviestin tiedot.", response = ReportedMessageDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "Internal service error")})
    Response getReportedMessage(@ApiParam(value = "Ryhmäsähköpostiviestin avain", required = true)
                                @PathParam(RestConstants.PARAM_MESSAGE_ID) Long messageID) throws Exception;

    /**
     * Hakee yksittäisen ryhmäsähköpostiviestin tunnuksen siihen liittyvän kirjelähetyksen avulla
     *
     * @param letterID Kirjelähetyksen tunnus
     * @return Raportoitavan ryhmäsähköpostin tunnus
     */
    @PreAuthorize(SecurityConstants.READ)
    @Produces(MediaType.TEXT_PLAIN)
    @Path(RestConstants.PATH_REPORT_MESSAGE_BY_LETTER_VIEW)
    @GET
    @ApiOperation(value = "Hakee halutun ryhmäsähköpostiviestin tiedot",
            notes = "Hakee kirjeavainta vastaavaa ryhmäsähköpostiviestin tiedot.", response = ReportedMessageDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "Internal service error")})
    Response getReportedMessageByLetter(@ApiParam(value = "Kirjelähetyksen avain", required = true)
                                        @PathParam(RestConstants.PARAM_LETTER_ID) Long letterID,
                                        @Context HttpServletRequest request) throws Exception;

    /**
     * Hakee yksittäisen ryhmäsähköpostiviestin tiedot. Palauttaa tietyn määrän vastaanottajien tietoja
     *
     * @param messageID Ryhmäsähköpostin tunnus
     * @param nbrOfRows Palautettavien vastaanottajien lukumäärä
     * @param page Sivu, jolle halutaan siirtyä katselemaan vastaanottajia
     * @param sortedBy Kenttä, minkä mukaan lajitellaan
     * @param order Nouseva (=asc) vai laskeva (= desc) lajittelujärjestys
     * @param request
     * @return Yksittäisen ryhmäsähköpostiviestin tiedot ja vastaanottajien tiedot
     */
    @PreAuthorize(SecurityConstants.READ)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(RestConstants.PATH_REPORT_MESSAGE_VIEW_WITH_PAGING)
    @GET
    @ApiOperation(value = "Hakee halutun ryhmäsähköpostiviestin ja viestin vastaanottajien tiedot",
        notes = "Hakee avainta vastaavaa ryhmäsähköpostiviestin ja vastaanottajien tiedot. Palauttaa halutun määrän "
            + "vastaanottajia lajiteltuna halutun sarakkeen mukaisesti", response = ReportedMessageDTO.class)
    @ApiResponses(value={@ApiResponse(code = 500, message = "Internal service error tai liittymävirhe")})
    Response getReportedMessageAndRecipients(@ApiParam(value = "Ryhmäsähköpostiviestin avain", required = true)
                                             @PathParam(RestConstants.PARAM_MESSAGE_ID) Long messageID,
                                             @ApiParam(value = "Haettavien rivien lukumäärä", required = true)
                                             @QueryParam(RestConstants.PARAM_NUMBER_OF_ROWS) Integer nbrOfRows,
                                             @ApiParam(value = "Sivu, mistä kohdasta haluttu määrä rivejä haetaan", required = true)
                                             @QueryParam(RestConstants.PARAM_PAGE) Integer page,
                                             @ApiParam(value = "Taulun sarake, minkä mukaan tiedot lajitellaan", required = false)
                                             @QueryParam(RestConstants.PARAM_SORTED_BY) String sortedBy,
                                             @ApiParam(value = "Lajittelujärjestys", allowableValues = "asc, desc", required = false)
                                             @QueryParam(RestConstants.PARAM_ORDER) String order, @Context HttpServletRequest request) throws Exception;

    /**
     * Hakee yksittäisen ryhmäsähköpostiviestin tiedot. Palauttaa vastaanottajien tiedot, joille lähetys epäonnistui
     *
     * @param messageID Ryhmäsähköpostin tunnus
     * @param nbrOfRows Palautettavien vastaanottajien lukumäärä
     * @param page Sivu, jolle halutaan siirtyä katselemaan vastaanottajia
     * @param sortedBy Kenttä, minkä mukaan lajitellaan
     * @param order Nouseva (=asc) vai laskeva (= desc) lajittelujärjestys
     * @param request
     * @return Yksittäisen ryhmäsähköpostiviestin tiedot ja vastaanottajien tiedot, joille lähetys epäonnistui
     */
    @PreAuthorize(SecurityConstants.READ)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(RestConstants.PATH_REPORT_MESSAGE_FAILED_VIEW_WITH_PAGING)
    @GET
    @ApiOperation(value = "Hakee ryhmäsähköpostiviestin ja viestin vastaanottajien tiedot, joille lähetys epäonnistui",
        notes = "Hakee avainta vastaavaa ryhmäsähköpostiviestin ja vastaanottajien tiedot, joille lähetys epäonnistui. "
            + "Palauttaa halutun määrän vastaanottajia lajiteltuna halutun sarakkeen mukaisesti",
        response = ReportedMessageDTO.class)
    @ApiResponses(value={@ApiResponse(code = 500, message = "Internal service error tai liittymävirhe")})
    Response getReportedMessageAndRecipientsSendingUnsuccessful(
            @ApiParam(value = "Ryhmäsähköpostiviestin avain", required = true)
            @PathParam(RestConstants.PARAM_MESSAGE_ID) Long messageID,
            @ApiParam(value = "Haettavien rivien lukumäärä", required = true)
            @QueryParam(RestConstants.PARAM_NUMBER_OF_ROWS) Integer nbrOfRows,
            @ApiParam(value = "Sivu, mistä kohdasta haluttu määrä rivejä haetaan", required = true)
            @QueryParam(RestConstants.PARAM_PAGE) Integer page,
            @ApiParam(value = "Taulun sarake, minkä mukaan tiedot lajitellaan", required = false)
            @QueryParam(RestConstants.PARAM_SORTED_BY) String sortedBy,
            @ApiParam(value = "Lajittelujärjestys", allowableValues = "asc, desc", required = false)
            @QueryParam(RestConstants.PARAM_ORDER) String order,
            @Context HttpServletRequest request) throws Exception;

    /**
     * Hakee yksittäisen ryhmäsähköpostiviestin tiedot. Palauttaa vastaanottajien tiedot, joille lähetys palautuis
     *
     * @param messageID Ryhmäsähköpostin tunnus
     * @param nbrOfRows Palautettavien vastaanottajien lukumäärä
     * @param page Sivu, jolle halutaan siirtyä katselemaan vastaanottajia
     * @param sortedBy Kenttä, minkä mukaan lajitellaan
     * @param order Nouseva (=asc) vai laskeva (= desc) lajittelujärjestys
     * @return Yksittäisen ryhmäsähköpostiviestin tiedot ja vastaanottajien tiedot, joille lähetys epäonnistui
     */
    @PreAuthorize(SecurityConstants.READ)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(RestConstants.PATH_REPORT_MESSAGE_BOUNCED_VIEW_WITH_PAGING)
    @GET
    @ApiOperation(value = "Hakee ryhmäsähköpostiviestin ja viestin vastaanottajien tiedot, joille lähetys palautui",
            notes = "Hakee avainta vastaavaa ryhmäsähköpostiviestin ja vastaanottajien tiedot, joille lähetys palautui. "
                    + "Palauttaa halutun määrän vastaanottajia lajiteltuna halutun sarakkeen mukaisesti",
            response = ReportedMessageDTO.class)
    @ApiResponses(value={@ApiResponse(code = 500, message = "Internal service error tai liittymävirhe")})
    Response getReportedMessageAndRecipientsSendingBounced(
            @ApiParam(value = "Ryhmäsähköpostiviestin avain", required = true)
            @PathParam(RestConstants.PARAM_MESSAGE_ID) Long messageID,
            @ApiParam(value = "Haettavien rivien lukumäärä", required = true)
            @QueryParam(RestConstants.PARAM_NUMBER_OF_ROWS) Integer nbrOfRows,
            @ApiParam(value = "Sivu, mistä kohdasta haluttu määrä rivejä haetaan", required = true)
            @QueryParam(RestConstants.PARAM_PAGE) Integer page,
            @ApiParam(value = "Taulun sarake, minkä mukaan tiedot lajitellaan", required = false)
            @QueryParam(RestConstants.PARAM_SORTED_BY) String sortedBy,
            @ApiParam(value = "Lajittelujärjestys", allowableValues = "asc, desc", required = false)
            @QueryParam(RestConstants.PARAM_ORDER) String order,
            @Context HttpServletRequest request) throws Exception;

    /**
     * Palauttaa yksittäisen ryhmäsähköpostiviestin tiedot. Palauttaa vastaanottajien tiedot, joille lähetys epäonnistui
     *
     * @param attachmentID Liitetiedoston tunnus
     */
    @PreAuthorize(SecurityConstants.READ)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path(RestConstants.PATH_REPORT_MESSAGE_DOWNLOAD_ATTACHMENT)
    @GET
    @ApiOperation(value = "Hakee yksittäisen ryhmäsähköpostin liitteen ja palauttaa tiedoston binäärimuodossa",
        notes = "Hakee avainta vastaavan ryhmäsähköpostiviestin liitteen ja palauttaa liitetiedoston ladattavassa muodossa.")
    @ApiResponses(value={@ApiResponse(code = 500, message = "Internal service error tai liittymävirhe")})
    Response downloadReportedMessageAttachment(
            @ApiParam(value = "Ryhmäsähköpostiviestin liitteen avain", required = true)
            @PathParam(RestConstants.PARAM_ATTACHMENT_ID) Long attachmentID) throws Exception;
}
