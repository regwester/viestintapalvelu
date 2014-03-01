package fi.vm.sade.ryhmasahkoposti.api.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import fi.vm.sade.ryhmasahkoposti.api.constants.RestConstants;
import fi.vm.sade.ryhmasahkoposti.api.constants.SecurityConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;

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
	 * @return Lista raportoitavia viestejä {@link ReportedMessageDTO} tai tyhjä lista
	 */
	@Secured(SecurityConstants.READ)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(RestConstants.PATH_REPORT_MESSAGES_LIST)
	@GET
	public List<ReportedMessageDTO> getReportedMessages();
	
	/**
	 * Hakee hakuparametrin mukaiset viestit käyttäjän ja hänen käyttäjäryhmänsä lähettämistä ryhmäshköpostiviesteistä
	 * 
	 * @param hakuKentta Käyttäjän antama hakuparametri
	 * @return Lista raportoitavia viestejä {@link ReportedMessageDTO} tai tyhjä lista 
	 */
	@Secured(SecurityConstants.READ)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(RestConstants.PATH_REPORT_MESSAGES_SEARCH)
	@GET
	public List<ReportedMessageDTO> getReportedMessages(@PathParam(RestConstants.PARAM_SEARCH_ARGUMENT) String searchArgument);
	
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
}
