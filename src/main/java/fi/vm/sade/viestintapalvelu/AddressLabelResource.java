package fi.vm.sade.viestintapalvelu;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.odftoolkit.odfdom.converter.core.utils.ByteArrayOutputStream;

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
	@Path("pdf")
	public String pdf(AddressLabelBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		byte[] pdf = labelBuilder.printPDF(input);
		return downloadCache.addDocument(request.getSession().getId(), 
				new Download("application/pdf;charset=utf-8", "addresslabels.pdf", pdf));
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("csv")
	public String csv(AddressLabelBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		byte[] csv = writeBOM(labelBuilder.printCSV(input));
		return downloadCache.addDocument(request.getSession().getId(), 
				new Download("text/csv;charset=utf-8", "addresslabels.csv", csv));
	}
	
	private byte[] writeBOM(byte[] document) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(output);
		stream.print('\ufeff');
		stream.write(document);
		return output.toByteArray();
	}
}
