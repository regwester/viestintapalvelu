package fi.vm.sade.viestintapalvelu.hyvaksymiskirje;

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

import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;

@Singleton
@Path(Urls.HYVAKSYMISKIRJE_RESOURCE_PATH)
public class HyvaksymiskirjeResource {
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
	@Produces("application/json")
	@Path("pdf")
	public String pdf(HyvaksymiskirjeBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		byte[] pdf = hyvaksymiskirjeBuilder.printPDF(input);
		return downloadCache.addDocument(new Download(
				"application/pdf;charset=utf-8", "hyvaksymiskirje.pdf", pdf));
	}
}
