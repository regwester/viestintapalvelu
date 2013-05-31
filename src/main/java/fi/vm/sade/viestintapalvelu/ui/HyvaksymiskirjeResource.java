package fi.vm.sade.viestintapalvelu.ui;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.domain.download.Download;
import fi.vm.sade.viestintapalvelu.domain.download.DownloadCache;
import fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje.HyvaksymiskirjeBatch;
import fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje.HyvaksymiskirjeBuilder;

@Singleton
@Path(Urls.HYVAKSYMISKIRJE_RESOURCE_PATH)
public class HyvaksymiskirjeResource extends AsynchronousResource {
	private DownloadCache downloadCache;
	private HyvaksymiskirjeBuilder hyvaksymiskirjeBuilder;

	@Inject
	public HyvaksymiskirjeResource(
			HyvaksymiskirjeBuilder jalkiohjauskirjeBuilder,
			DownloadCache downloadCache) {
		this.hyvaksymiskirjeBuilder = jalkiohjauskirjeBuilder;
		this.downloadCache = downloadCache;
	}

	@POST
	@Consumes("application/json")
	@Produces("text/plain")
	@Path("pdf")
	public Response pdf(HyvaksymiskirjeBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		byte[] pdf = hyvaksymiskirjeBuilder.printPDF(input);
		String documentId = downloadCache.addDocument(new Download(
				"application/pdf;charset=utf-8", "hyvaksymiskirje.pdf", pdf));
		return createResponse(request, documentId);
	}
}
