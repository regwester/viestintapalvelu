package fi.vm.sade.ryhmasahkoposti.api.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fi.vm.sade.ryhmasahkoposti.api.constants.RestConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;

/**
 * REST-rajapinta ryhmäsähköpostien selailua varten
 * 
 * @author vehei1
 *
 */
@Path(RestConstants.PATH_RYHMASAHKOPOSTI)
public interface GroupEmailBrowsingResource {
	/**
	 * Hakee käyttäjän ja hänen käyttäjäryhmänsä lähettämät ryhmäshköpostiviestit
	 * 
	 * @return Lista raportoitavia viestejä {@link EmailMessageDTO} tai tyhjä lista
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@Path(RestConstants.PATH_RYHMASAHKOPOSTI_SELAA)
	@GET
	public List<ReportedMessageDTO> getBrowsingMessages();
	
	/**
	 * Hakee hakuparametrin mulaiset viestit käyttäjän ja hänen käyttäjäryhmänsä lähettämistä ryhmäshköpostiviesteistä
	 * 
	 * @param hakuKentta Käyttäjän antama hakuparametri
	 * @return Lista raportoitavia viestejä {@link EmailMessageDTO} tai tyhjä lista 
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@Path(RestConstants.PATH_RYHMASAHKOPOSTI_HAE)
	@GET
	public List<ReportedMessageDTO> getBrowsingMessages(@QueryParam(RestConstants.PARAM_HAKUKENTTA) String searchArgument);	
}
