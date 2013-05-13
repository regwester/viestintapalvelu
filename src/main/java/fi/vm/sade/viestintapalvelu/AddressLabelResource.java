package fi.vm.sade.viestintapalvelu;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
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
	private DownloadCache downloadCache;
	private AddressLabelBuilder labelBuilder;

	@Inject
	public AddressLabelResource(AddressLabelBuilder labelBuilder, DownloadCache downloadCache) {
		this.labelBuilder = labelBuilder;
		this.downloadCache = downloadCache;
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("createDocument")
	public String createDocument(AddressLabelBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		byte[] binaryDocument = labelBuilder.printAddressLabels(input);
		String contentType = resolveContentType(input.getTemplateName());
		String filename = resolveFilename(input.getTemplateName());
		return downloadCache.addDocument(request.getSession().getId(), new Download(
				contentType, filename, binaryDocument));
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
		return "text/csv;charset=utf-8";
	}
}
