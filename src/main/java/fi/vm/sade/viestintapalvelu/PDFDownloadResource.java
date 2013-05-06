package fi.vm.sade.viestintapalvelu;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fr.opensagres.xdocreport.core.XDocReportException;

@Singleton
@Path("download")
public class PDFDownloadResource {
	private PDFService pdfService;

	@Inject
	public PDFDownloadResource(PDFService pdfService) {
		this.pdfService = pdfService;
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/pdf")
	public byte[] post(@FormParam("hakija") String input,
			@Context HttpServletResponse response) throws JSONException,
			IOException, XDocReportException {
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ input + ".pdf\"");
		return pdfService.createDocument(input);
	}
}
