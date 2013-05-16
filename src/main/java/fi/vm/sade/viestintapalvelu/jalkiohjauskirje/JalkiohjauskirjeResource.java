package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

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

import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;

@Singleton
@Path("jalkiohjauskirje")
public class JalkiohjauskirjeResource {
	private DownloadCache downloadCache;
	private JalkiohjauskirjeBuilder jalkiohjauskirjeBuilder;

	@Inject
	public JalkiohjauskirjeResource(JalkiohjauskirjeBuilder jalkiohjauskirjeBuilder, DownloadCache downloadCache) {
		this.jalkiohjauskirjeBuilder = jalkiohjauskirjeBuilder;
		this.downloadCache = downloadCache;
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("pdf")
	public String pdf(JalkiohjauskirjeBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		byte[] pdf = jalkiohjauskirjeBuilder.printPDF(input);
		return downloadCache.addDocument(request.getSession().getId(), 
				new Download("application/pdf;charset=utf-8", "jalkiohjauskirje.pdf", pdf));
	}
}
