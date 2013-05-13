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

import fr.opensagres.xdocreport.core.XDocReportException;

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
	@Path("createDocument")
	public String createDocument(JalkiohjauskirjeBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException, XDocReportException {
		byte[] binaryDocument = jalkiohjauskirjeBuilder.printJalkiohjauskirje(input);
		String contentType = "application/pdf;charset=utf-8";
		String filename = "jalkiohjauskirje.pdf";
		return downloadCache.addDocument(request.getSession().getId(), new Download(
				contentType, filename, binaryDocument));
	}
}
