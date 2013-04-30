package fi.vm.sade.viestintapalvelu;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 * curl -H "Content-Type: application/json" -X POST -d '["Ville Peurala", "Iina
 * Sipilä"]' http://localhost:8080/spike
 */
@Path("spike")
public class SpikeResource {
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public JSONObject post(JSONArray input) {
		System.out.println(input);
		Map<String, String> output = new HashMap<String, String>();
		output.put("status", "ok");
		return new JSONObject(output);
	}
}
