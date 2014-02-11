package fi.vm.sade.ryhmasahkoposti.api.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fi.vm.sade.ryhmasahkoposti.api.constants.RestConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;

/**
 * REST-rajapinta ryhmäsähköpostien selailua varten
 * 
 * @author vehei1
 *
 */
@Path(RestConstants.PATH_REPORT_MESSAGES)
public interface MessageReportingResource {
	/**
	 * Hakee käyttäjän ja hänen käyttäjäryhmänsä lähettämät ryhmäshköpostiviestit
	 * 
	 * @return Lista raportoitavia viestejä {@link ReportedMessageDTO} tai tyhjä lista
	 */
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
	@Produces(MediaType.APPLICATION_JSON)
	@Path(RestConstants.PATH_REPORT_MESSAGES_SEARCH)
	@GET
	public List<ReportedMessageDTO> getReportedMessages(@QueryParam(RestConstants.PARAM_SEARCH_ARGUMENT) String searchArgument);
	
	@Produces(MediaType.APPLICATION_JSON)
	@Path("dto")
	@GET	
	public ReportedMessageDTO getReportedMessage();
}
