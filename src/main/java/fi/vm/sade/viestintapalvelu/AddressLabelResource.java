package fi.vm.sade.viestintapalvelu;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.lowagie.text.DocumentException;

@Singleton
@Path("addresslabel")
public class AddressLabelResource {
	private AddressLabelBuilder labelBuilder;

	@Inject
	public AddressLabelResource(AddressLabelBuilder labelBuilder) {
		this.labelBuilder = labelBuilder;
	}

	@POST
	@Consumes("application/json")
	@Produces("application/pdf")
	public byte[] post(AddressLabelBatch input,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws
			IOException, DocumentException {
		response.setHeader("Content-Disposition",
				"attachment; filename=\"addresslabels.pdf\"");
		return labelBuilder.printAddressLabels(input);
	}
}
