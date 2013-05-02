package fi.vm.sade.viestintapalvelu;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Path("addresslabel")
public class AddressLabelResource {
	private PDFService pdfService;

	@Inject
	public AddressLabelResource(PDFService pdfService) {
		this.pdfService = pdfService;
	}

	@POST
	@Consumes("application/json")
	@Produces("application/pdf")
	public byte[] post(AddressLabelBatch input,
			@Context HttpServletResponse response) throws JSONException {
		response.setHeader("Content-Disposition",
				"attachment; filename=\"addresslabels.pdf\"");
		return pdfService.printAddressLabels(input);
	}
}
