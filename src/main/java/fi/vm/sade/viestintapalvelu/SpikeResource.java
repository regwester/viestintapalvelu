package fi.vm.sade.viestintapalvelu;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("spike")
public class SpikeResource {
	
	@GET
	@Produces("application/json")
	public String post() {
		return "{name: 'Ville'}";
	}

}
