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
package fi.vm.sade.viestintapalvelu.asiontitili;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import fi.vm.sade.ryhmasahkoposti.api.constants.SecurityConstants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.common.util.BeanValidator;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliAsyncResponseDto;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliSendBatchDto;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.AsiointitiliCommunicationService;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.*;

/**
 * User: ratamaa
 * Date: 10.10.2014
 * Time: 16:15
 */
@Component
@Path(Urls.ASIOINTITILI)
@Api(value= Urls.ASIOINTITILI, description = "Kansalaisen asiointitilin tominnot")
@PreAuthorize("isAuthenticated()")
public class AsiointitiliResource {
    private static final Logger logger = LoggerFactory.getLogger(AsiointitiliResource.class);

//    @Autowired
//    private AsiointitiliCommunicationService asiointitiliCommunicationService;

    @Autowired
    private AsiointitiliService asiointitiliService;

    @Autowired
    private BeanValidator beanValidator;

    @POST
    @PreAuthorize(SecurityConstants.ASIOINTITILI)
    @Consumes(MediaType.APPLICATION_JSON+";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
    @Path("/send")
    @ApiOperation(value="Viestin lähetys asiointitilipalveluun",
            notes= AsiointitiliResource.SEND_NOTES, response = AsiointitiliAsyncResponseDto.class)
    public Response send(@ApiParam("Viestien lähetyspyyntö") AsiointitiliSendBatchDto sendBatch) {
        logger.info("Ajastuspalvelu send start");
        beanValidator.validate(sendBatch);
        AsiointitiliAsyncResponseDto response = asiointitiliService.send(sendBatch);
        logger.info("Ajastuspalvelu send end");
        return Response.status(statusCode(response.getStatusCode())).entity(response).build();
    }

//    @POST
//    @Deprecated
//    @Consumes(MediaType.APPLICATION_JSON+";charset=utf-8")
//    @Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
//    @Path("/haeAsiakasTiloja")
//    @ApiOperation(value="EI KÄYTÖSSÄ. Asiointitlin kysely WS1 / HaeAsiakasTiloja Kaikki-kyselylaji",
//            notes= AsiointitiliResource.HAE_ASIAKAS_TILOJA_NOTES, response = AsiakasTilaKyselyVastausDto.class)
//    @PreAuthorize(SecurityConstants.ASIOINTITILI)
//    public Response haeAsiakasTiloja(@ApiParam("Kysely") HaeAsiakasTilojaKyselyDto kyselyWS1) {
//        beanValidator.validate(kyselyWS1);
//        AsiakasTilaKyselyVastausDto vastaus = asiointitiliCommunicationService.haeAsiakasTiloja(kyselyWS1);
//        return Response.status(statusCode(vastaus.getTilaKoodi())).entity(vastaus).build();
//    }
//
//    @POST
//    @Deprecated
//    @PreAuthorize(SecurityConstants.ASIOINTITILI)
//    @Consumes(MediaType.APPLICATION_JSON+";charset=utf-8")
//    @Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
//    @Path("/tarkistaAsiointitilinTila")
//    @ApiOperation(value="EI KÄYTÖSSÄ. Asiointitlin kysely WS1 / HaeAsiakasTiloja Asiakkaat-kyselylaji",
//            notes= AsiointitiliResource.TARKISTA_ASIAKASTILIN_TILA_NOTES, response = AsiakasTilaKyselyVastausDto.class)
//    public Response tarkistaAsiointitilinTila(@ApiParam("Kysely") AsiakasTilaTarkastusKyselyDto kyselyWS1) {
//        beanValidator.validate(kyselyWS1);
//        AsiakasTilaKyselyVastausDto vastaus = asiointitiliCommunicationService.tarkistaAsiointitilinTila(kyselyWS1);
//        return Response.status(statusCode(vastaus.getTilaKoodi())).entity(vastaus).build();
//    }
//
//    @POST
//    @Deprecated
//    @PreAuthorize(SecurityConstants.ASIOINTITILI)
//    @Consumes(MediaType.APPLICATION_JSON+";charset=utf-8")
//    @Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
//    @Path("/lisaaKohteitaAsiointitilille")
//    @ApiOperation(value="EI KÄYTÖSSÄ. Asiointitlin kysely WS2 / LisaaKohteita",
//            notes= AsiointitiliResource.KOHDE_LISAYS_NOTES, response = KohdeLisaysVastausDto.class)
//    public Response lisaaKohteitaAsiointitilille(@ApiParam("Kysely") KohdeLisaysDto kyselyWS2) {
//        beanValidator.validate(kyselyWS2);
//        KohdeLisaysVastausDto vastaus = asiointitiliCommunicationService.lisaaKohteitaAsiointitilille(kyselyWS2);
//        return Response.status(statusCode(vastaus.getTilaKoodi())).entity(vastaus).build();
//    }

    private int statusCode(int tilaKoodi) {
        if (Response.Status.fromStatusCode(tilaKoodi) == null) {
            if (tilaKoodi >= 500) {
                return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            }
            if (tilaKoodi >= 400) {
                return Response.Status.BAD_REQUEST.getStatusCode();
            }
            if (tilaKoodi >= 300) {
                return Response.Status.UNAUTHORIZED.getStatusCode();
            }
            return Response.Status.OK.getStatusCode();
        }
        return tilaKoodi;
    }

    public static final String SEND_NOTES = "Lähettää viestin asiointitilipalveluun niille vastaanottajille, joilla " +
            "asiointitili on olemassa.";

    public static final String HAE_ASIAKAS_TILOJA_NOTES = "HUOM: Suora pyyntö asiointitilipalveluun. EI KÄYTÖSSÄ." +
            "" +
            "Omien asiointitiliasiakkaiden tarkistus" +
            "" +
            "Tämän kyselyn avulla viranomaisjärjestelmä voi tarkistaa mitkä sen asiakkaista ovat asiointitilipalvelun" +
            "käyttäjiä ja voivat siis vastaanottaa asiointiasioita asiointitilille. \n" +
            "Kyselyn avulla voi hakea kaikki omat asiakkaat tai tarkistaa annetun tai annettujen hetujen osalta," +
            "ovatko he ottaneet asiointitilin käyttöön.\n" +
            "\n" +
            "Vastauksessa saadaan tieto, onko  asiakas asiointitilin käyttäjä.";
    public static final String TARKISTA_ASIAKASTILIN_TILA_NOTES = "HUOM: Suora pyyntö asiointitilipalveluun. EI KÄYTÖSSÄ." +
            "" +
            "Omien asiointitiliasiakkaiden tarkistus" +
            "" +
            "Vastauksessa saadaan tieto, onko  asiakas asiointitilin käyttäjä.\n" +
            "\n" +
            "Kyselyä rajataan ajan suhteen siten, että haetaan vain tietyllä aikavälillä" +
            "asiointitilin käyttäjäksi liittyneet.";
    public static final String KOHDE_LISAYS_NOTES = "HUOM: Suora pyyntö asiointitilipalveluun. EI KÄYTÖSSÄ." +
            "" +
            "Asiointiasian lähettäminen / viranomaisen tiedoksianto" +
            "" +
            "Tämän rajapinnan avulla viranomaisjärjestelmä voi lähettää asiointitilipalveluun asiointiasioita," +
            "tiedoksiantoja ja vastauksia kyselyihin. Lähetetyt asiat voivat sisältää linkkejä viranomaisen" +
            "välivarastossa oleviin asiakirjoihin tai kokonaisia asiakirjoja, jotka tallennetaan" +
            "asiointitilipalvelun välivarastoon.\n" +
            "\n" +
            "Lähetetty asia voidaan liittää asiointitilillä jo olevaan asiaan, jolloin kyseinen asia näkyy" +
            "asiakkaalle kyseisen asiointiasian yhteydessä (esimerkiksi lisäselvityspyyntö tai vastaus kysymykseen).  \n" +
            "\n" +
            "Lähetetty asia voi olla myös todisteellinen tiedoksianto, jolloin paluukanavan kautta (WSPA4)" +
            "saadaan tieto tiedoksiannon kuittauksesta.\n" +
            " \n" +
            "Viranomaisen tulee toimittaa asialle tunniste, jonka tulee olla viranomaisen" +
            "palvelun sisällä yksilöllinen ajasta riippumatta.\n" +
            "\n" +
            "Kutsun vastauksena voidaan toimittaa lähetettyjen asioiden tallennustiedot asiakohtaisesti" +
            "(synkroninen) tai pelkkä kuittaus asioiden vastaanottamisesta (asynkroninen). Viestintätyyppi" +
            "on valittavissa liittymisen yhteydessä viranomaiskohtaisesti";
}
