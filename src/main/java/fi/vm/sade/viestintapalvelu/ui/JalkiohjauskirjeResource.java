package fi.vm.sade.viestintapalvelu.ui;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

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
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.JalkiohjauskirjeBatch;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.JalkiohjauskirjeBuilder;

@Singleton
@Path(Urls.JALKIOHJAUSKIRJE_RESOURCE_PATH)
public class JalkiohjauskirjeResource extends AsynchronousResource {
	private DownloadCache downloadCache;
	private JalkiohjauskirjeBuilder jalkiohjauskirjeBuilder;

	@Inject
	public JalkiohjauskirjeResource(
			JalkiohjauskirjeBuilder jalkiohjauskirjeBuilder,
			DownloadCache downloadCache) {
		this.jalkiohjauskirjeBuilder = jalkiohjauskirjeBuilder;
		this.downloadCache = downloadCache;
	}

	@POST
	@Consumes("application/json")
	@Produces("text/plain")
	@Path("pdf")
	public Response pdf(JalkiohjauskirjeBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		byte[] pdf = jalkiohjauskirjeBuilder.printPDF(input);
		String documentId = downloadCache.addDocument(new Download(
				"application/pdf;charset=utf-8", "jalkiohjauskirje.pdf", pdf));
		return createResponse(request, documentId);
	}

	@POST
	@Consumes("application/json")
	@Produces("text/plain")
	@Path("zip")
	public Response zip(JalkiohjauskirjeBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException, NoSuchAlgorithmException {
		input.split(3, "foo");
		byte[] zip = jalkiohjauskirjeBuilder.printZIP(input);
		String documentId = downloadCache.addDocument(new Download(
				"application/zip", "jalkiohjauskirje.zip", zip));
		return createResponse(request, documentId);
	}
}
