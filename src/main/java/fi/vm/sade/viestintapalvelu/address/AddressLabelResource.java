package fi.vm.sade.viestintapalvelu.address;

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
	public String pdf(AddressLabelBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		byte[] pdf = labelBuilder.printPDF(input);
		return downloadCache.addDocument(request.getSession().getId(),
				new Download("application/pdf;charset=utf-8",
						"addresslabels.pdf", pdf));
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("xls")
	public String csv(AddressLabelBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		byte[] csv = labelBuilder.printCSV(input);
		return downloadCache.addDocument(request.getSession().getId(),
				new Download("application/vnd.ms-excel", "addresslabels.xls",
						csv));
	}
}
