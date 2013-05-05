package fi.vm.sade.viestintapalvelu;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

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
	public Response post(AddressLabelBatch input,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws
			IOException, DocumentException {
		response.setHeader("Content-Disposition",
				"attachment; filename=\""+resolveFilename(input.getTemplateName())+"\"");
		return Response.ok(labelBuilder.printAddressLabels(input)).type(resolveContentType(input.getTemplateName())).build();
	}
	
	private String resolveFilename(String templateName) {
		if (labelBuilder.isPDFTemplate(templateName)) {
			return "addresslabels.pdf";
		}
		return "addresslabels.csv";
	}

	private String resolveContentType(String templateName) {
		if (labelBuilder.isPDFTemplate(templateName)) {
			return "application/pdf;charset=utf-8";
		}
		return "application/csv;charset=utf-8";
	}
}
