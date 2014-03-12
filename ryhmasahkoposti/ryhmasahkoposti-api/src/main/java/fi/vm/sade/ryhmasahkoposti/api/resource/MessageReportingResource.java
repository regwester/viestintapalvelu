package fi.vm.sade.ryhmasahkoposti.api.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import fi.vm.sade.ryhmasahkoposti.api.constants.RestConstants;
import fi.vm.sade.ryhmasahkoposti.api.constants.SecurityConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessagesDTO;

/**
 * REST-rajapinta ryhmäsähköpostien selailua varten
 * 
 * @author vehei1
 *
 */
@PreAuthorize(SecurityConstants.USER_IS_AUTHENTICATED)
@Path(RestConstants.PATH_REPORT_MESSAGES)
public interface MessageReportingResource {
	/**
	 * Hakee käyttäjän ja hänen käyttäjäryhmänsä lähettämät ryhmäshköpostiviestit
	 * 
	 * @param nbrOfRows Haettavien ryhmäsähköpostiviestien lukumäärä
	 * @param page Sivu, jolle halutaan siirtyä katselemaan viestejä
	 * @param sortedBy Sarake, minkä mukaan lajittelu suoritetaan
	 * @param order Lajittelujärjestys
	 * @return Tiedot raportoitavista ryhmäsähköpostiviesteistä {@link ReportedMessagesDTO}
	 */
	@Secured(SecurityConstants.READ)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(RestConstants.PATH_REPORT_MESSAGES_LIST)
	@GET
	public ReportedMessagesDTO getReportedMessages(@QueryParam(RestConstants.PARAM_NUMBER_OF_ROWS) Integer nbrOfRows, 
        @QueryParam(RestConstants.PARAM_PAGE) Integer page,@QueryParam(RestConstants.PARAM_SORTED_BY) String sortedBy, 
        @QueryParam(RestConstants.PARAM_ORDER) String order);

	/**
	 * Hakee hakuparametrin mukaiset viestit käyttäjän ja hänen käyttäjäryhmänsä lähettämistä ryhmäshköpostiviesteistä
	 * 
	 * @param hakuKentta Käyttäjän antama hakuparametri
     * @param nbrOfRows Haettavien ryhmäsähköpostiviestien lukumäärä
     * @param page Sivu, jolle halutaan siirtyä katselemaan viestejä
     * @param sortedBy Sarake, minkä mukaan lajittelu suoritetaan
     * @param order Lajittelujärjestys
	 * @return Tiedot raportoitavista ryhmäsähköpostiviesteistä {@link ReportedMessagesDTO}
	 */
	@Secured(SecurityConstants.READ)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(RestConstants.PATH_REPORT_MESSAGES_SEARCH)
	@GET
	public ReportedMessagesDTO getReportedMessages(@PathParam(RestConstants.PARAM_SEARCH_ARGUMENT) String searchArgument, 
	    @QueryParam(RestConstants.PARAM_NUMBER_OF_ROWS) Integer nbrOfRows, @QueryParam(RestConstants.PARAM_PAGE) Integer page, 
	    @QueryParam(RestConstants.PARAM_SORTED_BY) String sortedBy, @QueryParam(RestConstants.PARAM_ORDER) String order);
	
	/**
	 * Hakee yksittäisen ryhmäsähköpostiviestin tiedot
	 * 
	 * @param Ryhmäsähköpostiviestin tunnus
	 * @return Raportoitavan ryhmäsähköpostin tiedot {@link ReportedMessageDTO}}
	 */
	@Secured(SecurityConstants.READ)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(RestConstants.PATH_REPORT_MESSAGE_VIEW)
	@GET	
	public ReportedMessageDTO getReportedMessage(@PathParam(RestConstants.PARAM_MESSAGE_ID) Long messageID);
	
	/**
	 * Hakee yksittäisen ryhmäsähköpostiviestin tiedot. Palauttaa tietyn määrän vastaanottajien tietoja
	 * 
	 * @param messageID Ryhmäsähköpostin tunnus
	 * @param nbrOfRows Palautettavien vastaanottajien lukumäärä
	 * @param page Sivu, jolle halutaan siirtyä katselemaan vastaanottajia
	 * @param sortedBy Kenttä, minkä mukaan lajitellaan
	 * @param order Nouseva (=asc) vai laskeva (= desc) lajittelujärjestys
	 * @return
	 */
	@Secured(SecurityConstants.READ)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(RestConstants.PATH_REPORT_MESSAGE_VIEW_WITH_PAGING)
    @GET
    public ReportedMessageDTO getReportedMessage(@PathParam(RestConstants.PARAM_MESSAGE_ID) Long messageID, 
        @QueryParam(RestConstants.PARAM_NUMBER_OF_ROWS) Integer nbrOfRows, 
        @QueryParam(RestConstants.PARAM_PAGE) Integer page, @QueryParam(RestConstants.PARAM_SORTED_BY) String sortedBy, 
        @QueryParam(RestConstants.PARAM_ORDER) String order);
}
