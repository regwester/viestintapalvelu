package fi.vm.sade.viestintapalvelu.address;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;

@Singleton
@Path(Urls.ADDRESS_LABEL_RESOURCE_PATH)
public class AddressLabelResource extends AsynchronousResource {
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
	@Produces("text/plain")
	@Path("pdf")
	public Response pdf(final AddressLabelBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException, JSONException {
		System.out.println("START PDF");
		try {
			final String documentId = UUID.randomUUID().toString();
			System.out.println("BEFORE EXECUTEASYNCH");
			executeAsynchronously(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println("BEGIN call()");
						byte[] pdf = labelBuilder.printPDF(input);
						System.out.println("AFTER printPDF");
						downloadCache.addDocument(new Download(
								"application/pdf;charset=utf-8",
								"addresslabels.pdf", pdf), documentId);
						System.out.println("RETURNING FROM EXECUTEASYNCH");
					} catch (Throwable t) {
						System.out.println(t);
						throw new RuntimeException(t);
					}
				}
			});
			return createResponse(request, documentId);
		} catch (Throwable t) {
			System.out.println(t);
			throw new RuntimeException(t);
		}
	}

	@POST
	@Consumes("application/json")
	@Produces("text/plain")
	@Path("xls")
	public Response csv(AddressLabelBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		byte[] csv = labelBuilder.printCSV(input);
		String documentId = downloadCache.addDocument(new Download(
				"application/vnd.ms-excel", "addresslabels.xls", csv));
		return createResponse(request, documentId);
	}
}
