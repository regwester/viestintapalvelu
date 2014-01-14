package fi.vm.sade.ryhmasahkoposti.api.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fi.vm.sade.ryhmasahkoposti.api.constants.RestConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;

/**
 * REST-rajapinta ryhmäsähköpostien selailua varten
 * 
 * @author vehei1
 *
 */
@Path(RestConstants.PATH_RYHMASAHKOPOSTISELAILU)
public interface RyhmasahkopostiSelailuResource {
	/**
	 * Hakee käyttäjän ja hänen käyttäjäryhmänsä lähettämät ryhmäshköpostiviestit
	 * 
	 * @return Lista raportoitavia viestejä {@link RaportoitavaViestiDTO} tai tyhjä lista
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(RestConstants.PATH_HAE_RAPORTOITAVAT_VIESTIT)
	public List<RaportoitavaViestiDTO> getRaportoitavatViestit();
	
	/**
	 * Hakee hakuparametrin mulaiset viestit käyttäjän ja hänen käyttäjäryhmänsä lähettämistä ryhmäshköpostiviesteistä
	 * 
	 * @param query Käyttäjän antama hakuparametri
	 * @return Lista raportoitavia viestejä {@link RaportoitavaViestiDTO} tai tyhjä lista 
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(RestConstants.PATH_HAE_RAPORTOITAVAT_VIESTIT_QUERY)
	public List<RaportoitavaViestiDTO> getRaportoitavatViestit(
		@PathParam(RestConstants.PATH_QUERY) String query);	
}
