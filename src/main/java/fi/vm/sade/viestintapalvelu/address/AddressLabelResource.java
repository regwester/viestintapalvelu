package fi.vm.sade.viestintapalvelu.address;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;

@Singleton
@Path(Urls.ADDRESS_LABEL_RESOURCE_PATH)
public class AddressLabelResource {
	private DownloadCache downloadCache;
	private AddressLabelBuilder labelBuilder;

	@Inject
	public AddressLabelResource(AddressLabelBuilder labelBuilder,
			DownloadCache downloadCache) {
		this.labelBuilder = labelBuilder;
		this.downloadCache = downloadCache;
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("pdf")
	public Response pdf(AddressLabelBatch input) throws IOException,
			DocumentException {
		byte[] pdf = labelBuilder.printPDF(input);
		String documentId = downloadCache.addDocument(new Download(
				"application/pdf;charset=utf-8", "addresslabels.pdf", pdf));
		return Response.status(Status.ACCEPTED)
				.contentLocation(URI.create(documentId)).entity(documentId)
				.build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("xls")
	public String csv(AddressLabelBatch input) throws IOException,
			DocumentException {
		byte[] csv = labelBuilder.printCSV(input);
		return downloadCache.addDocument(new Download(
				"application/vnd.ms-excel", "addresslabels.xls", csv));
	}
}
