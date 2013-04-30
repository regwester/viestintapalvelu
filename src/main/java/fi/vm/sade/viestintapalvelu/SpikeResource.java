package fi.vm.sade.viestintapalvelu;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * curl -H "Content-Type: application/json" -X POST -d '["Ville Peurala", "Iina
 * Sipilä"]' http://localhost:8080/spike
 */
@Path("spike")
public class SpikeResource {
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public String post(Nimilista input) {
		System.out.println(input);
		return "{name: 'Ville'}";
	}
}
