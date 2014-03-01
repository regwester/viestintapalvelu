package fi.vm.sade.viestintapalvelu.hyvaksymiskirje;

import static fi.vm.sade.viestintapalvelu.Utils.filenamePrefixWithUsernameAndTimestamp;
import static fi.vm.sade.viestintapalvelu.Utils.globalRandomId;
import static org.joda.time.DateTime.now;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fi.vm.sade.valinta.dokumenttipalvelu.resource.DokumenttiResource;
import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;

@Service
@Singleton
@Path(Urls.HYVAKSYMISKIRJE_RESOURCE_PATH)
// Use HTML-entities instead of scandinavian letters in @Api-description, since
// swagger-ui.js treats model's description as HTML and does not escape it
// properly
@Api(value = "/" + Urls.API_PATH + "/" + Urls.HYVAKSYMISKIRJE_RESOURCE_PATH, description = "Hyv&auml;ksymiskirjeen k&auml;sittelyn rajapinnat")
public class HyvaksymiskirjeResource extends AsynchronousResource {
	private final Logger LOG = LoggerFactory
			.getLogger(HyvaksymiskirjeResource.class);
	private final DownloadCache downloadCache;
	private final HyvaksymiskirjeBuilder hyvaksymiskirjeBuilder;
	private final DokumenttiResource dokumenttiResource;
	private final ExecutorService executor;

	private final static String FixedTemplateNote = "Toistaiseksi kirjeen malli on kiinteästi tiedostona jakelupaketissa. ";
	private final static String ApiPDFSync = "Palauttaa URLin, josta voi ladata hyväksymiskirjeen/kirjeet PDF-muodossa; synkroninen. "
			+ FixedTemplateNote;
	private final static String ApiPDFAsync = "Palauttaa URLin, josta voi ladata hyväksymiskirjeen/kirjeet PDF-muodossa; asynkroninen. "
			+ FixedTemplateNote;
	private final static String PDFResponse400 = "BAD_REQUEST; PDF-tiedoston luonti epäonnistui eikä tiedostoa voi noutaa download-linkin avulla.";

	@Inject
	public HyvaksymiskirjeResource(
			HyvaksymiskirjeBuilder jalkiohjauskirjeBuilder,
			DownloadCache downloadCache, DokumenttiResource dokumenttiResource,
			ExecutorService executor) {
		this.hyvaksymiskirjeBuilder = jalkiohjauskirjeBuilder;
		this.downloadCache = downloadCache;
		this.dokumenttiResource = dokumenttiResource;
		this.executor = executor;
	}

	/**
	 * Hyvaksymiskirje PDF sync
	 * 
	 * @param input
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	@POST
	@Consumes("application/json")
	@Produces("text/plain")
	@Path("/pdf")
	@ApiOperation(value = ApiPDFSync, notes = ApiPDFSync)
	@ApiResponses(@ApiResponse(code = 400, message = PDFResponse400))
	public Response pdf(
			@ApiParam(value = "Muodostettavien hyväksymiskirjeiden tiedot (1-n)", required = true) HyvaksymiskirjeBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		String documentId;
		try {
			byte[] pdf = hyvaksymiskirjeBuilder.printPDF(input);
			documentId = downloadCache
					.addDocument(new Download("application/pdf;charset=utf-8",
							"hyvaksymiskirje.pdf", pdf));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Hyväksymiskirje PDF failed: {}", e.getMessage());
			return createFailureResponse(request);
		}
		return createResponse(request, documentId);
	}

	@POST
	@Consumes("application/json")
	@Produces("application/octet-stream")
	@Path("/sync/pdf")
	@ApiOperation(value = ApiPDFSync, notes = ApiPDFSync)
	@ApiResponses(@ApiResponse(code = 400, message = PDFResponse400))
	public InputStream syncPdf(
			@ApiParam(value = "Muodostettavien hyväksymiskirjeiden tiedot (1-n)", required = true) HyvaksymiskirjeBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		return new ByteArrayInputStream(hyvaksymiskirjeBuilder.printPDF(input));
	}

	/**
	 * Hyvaksymiskirje PDF async
	 * 
	 * @param input
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	@POST
	@Consumes("application/json")
	@Produces("text/plain")
	@Path("/async/pdf")
	@ApiOperation(value = ApiPDFAsync, notes = ApiPDFAsync
			+ AsyncResponseLogicDocumentation)
	public Response asyncPdf(
			@ApiParam(value = "Muodostettavien hyväksymiskirjeiden tiedot (1-n)", required = true) final HyvaksymiskirjeBatch input,
			@Context final HttpServletRequest request) throws IOException,
			DocumentException {
		final String documentId = globalRandomId();
		executor.execute(new Runnable() {
			public void run() {
				final Authentication auth = SecurityContextHolder.getContext()
						.getAuthentication();
				SecurityContextHolder.getContext().setAuthentication(auth);
				try {
					byte[] pdf = hyvaksymiskirjeBuilder.printPDF(input);
					dokumenttiResource
							.tallenna(
									null,
									filenamePrefixWithUsernameAndTimestamp("hyvaksymiskirje.pdf"),
									now().plusDays(1).toDate().getTime(),
									Arrays.asList("viestintapalvelu",
											"hyvaksymiskirje", "pdf"),
									"application/pdf;charset=utf-8",
									new ByteArrayInputStream(pdf));
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("Hyvaksymiskirje PDF async failed: {}",
							e.getMessage());
					createFailureResponse(request);
				}
			}
		});
		return createResponse(request, documentId);
	}

}
