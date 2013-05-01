package fi.vm.sade.viestintapalvelu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * curl -H "Content-Type: application/json" -X POST -d '["Ville Peurala", "Iina
 * Sipilä"]' http://localhost:8080/spike
 */
@Singleton
@Path("spike")
public class SpikeResource {
	private IPDFService pdfService;

	@Inject
	public SpikeResource(IPDFService pdfService) {
		this.pdfService = pdfService;
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public JSONObject post(JSONArray input) throws JSONException {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < input.length(); i++) {
			list.add(input.getString(i));
		}
		pdfService.createDocuments(list);
		Map<String, String> output = new HashMap<String, String>();
		output.put("status", "ok");
		return new JSONObject(output);
	}
}
