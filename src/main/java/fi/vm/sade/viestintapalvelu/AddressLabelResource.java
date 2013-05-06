package fi.vm.sade.viestintapalvelu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.lowagie.text.DocumentException;

import fr.opensagres.xdocreport.core.XDocReportException;

@Singleton
@Path("addresslabel")
public class AddressLabelResource {
	//TODO isipila 6.5.2012: Refactor this
	private Map<String, Download> downloads = new HashMap<String, Download>();
	
	private AddressLabelBuilder labelBuilder;

	@Inject
	public AddressLabelResource(AddressLabelBuilder labelBuilder) {
		this.labelBuilder = labelBuilder;
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("createDocument")
	public String createDocument(AddressLabelBatch input, @Context HttpServletRequest request) throws IOException,
			DocumentException {
		byte[] binaryDocument = labelBuilder.printAddressLabels(input);
		String contentType = resolveContentType(input.getTemplateName());
		String filename = resolveFilename(input.getTemplateName());
		String documentId = UUID.randomUUID().toString();
		downloads.put(request.getSession().getId() + documentId, new Download(contentType, filename, binaryDocument));
		return documentId;
	}

	@GET
	@Produces("application/pdf")
	@Path("download/{documentId}")
	public Response download(@PathParam("documentId") String input,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws JSONException,
			IOException, XDocReportException {
		Download download = downloads.remove(request.getSession().getId() + input);
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ download.getFilename() + "\"");
		return Response.ok(download.toByteArray())
				.type(download.getContentType()).build();
	}

	private String resolveFilename(String templateName) {
		if (labelBuilder.isPDFTemplate(templateName)) {
			return "addresslabels.pdf";
		}
		// TODO vpeurala 6.5.2013: Refactor this branching
		return "addresslabels.csv";
	}

	private String resolveContentType(String templateName) {
		if (labelBuilder.isPDFTemplate(templateName)) {
			return "application/pdf;charset=utf-8";
		}
		return "application/csv;charset=utf-8";
	}
}
