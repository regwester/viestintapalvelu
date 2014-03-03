package fi.vm.sade.viestintapalvelu.koekutsukirje;

import static fi.vm.sade.viestintapalvelu.Utils.filenamePrefixWithUsernameAndTimestamp;
import static fi.vm.sade.viestintapalvelu.Utils.globalRandomId;
import static org.joda.time.DateTime.now;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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

@Component
@Path(Urls.KOEKUTSUKIRJE_RESOURCE_PATH)
// Use HTML-entities instead of scandinavian letters in @Api-description, since
// swagger-ui.js treats model's description as HTML and does not escape it
// properly
@Api(value = "/" + Urls.API_PATH + "/" + Urls.KOEKUTSUKIRJE_RESOURCE_PATH, description = "Koekutsukirjeen k&auml;sittelyn rajapinnat")
public class KoekutsukirjeResource extends AsynchronousResource {
	private final Logger LOG = LoggerFactory
			.getLogger(KoekutsukirjeResource.class);

	@Autowired
	private DownloadCache downloadCache;
	@Autowired
	private KoekutsukirjeBuilder koekutsukirjeBuilder;
	@Autowired
	private DokumenttiResource dokumenttiResource;
	@Autowired
	private ExecutorService executor;

	private final static String ApiPDFSync = "Palauttaa URLin, josta voi ladata koekutsukirjeen/kirjeet PDF-muodossa; synkroninen";
	private final static String ApiPDFAsync = "Palauttaa URLin, josta voi ladata koekutsukirjeen/kirjeet PDF-muodossa; asynkroninen";
	private final static String PDFResponse400 = "BAD_REQUEST; PDF-tiedoston luonti epäonnistui eikä tiedostoa voi noutaa download-linkin avulla.";

	/**
	 * Koekutsukirje PDF sync
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
			@ApiParam(value = "Muodostettavien koekutsukirjeiden tiedot (1-n)", required = true) final KoekutsukirjeBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		String documentId;
		try {
			byte[] pdf = koekutsukirjeBuilder.printPDF(input);
			documentId = downloadCache.addDocument(new Download(
					"application/pdf;charset=utf-8", "koekutsukirje.pdf", pdf));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Koekutsukirje PDF failed: {}", e.getMessage());
			return createFailureResponse(request);
		}
		return createResponse(request, documentId);
	}

	/**
	 * Varaudutaan viestintapalvelun klusterointiin. Ei tarvitse tietoturvaa
	 * https:n lisäksi. Kutsuja antaa datat ja kutsuja saa kirjeen.
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/octet-stream")
	@Path("/sync/pdf")
	@ApiOperation(value = ApiPDFSync, notes = ApiPDFSync)
	@ApiResponses(@ApiResponse(code = 400, message = PDFResponse400))
	public InputStream syncPdf(
			@ApiParam(value = "Muodostettavien koekutsukirjeiden tiedot (1-n)", required = true) final KoekutsukirjeBatch input)
			throws IOException, DocumentException {
		return new ByteArrayInputStream(koekutsukirjeBuilder.printPDF(input));
	}

	/**
	 * Koekutsukirje PDF async
	 * 
	 * @param input
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	@POST
	@Consumes("application/json")
	@PreAuthorize("isAuthenticated()")
	@Produces("text/plain")
	@Path("/async/pdf")
	@ApiOperation(value = ApiPDFAsync, notes = ApiPDFAsync
			+ AsyncResponseLogicDocumentation)
	public Response asyncPdf(
			@ApiParam(value = "Muodostettavien koekutsukirjeiden tiedot (1-n)", required = true) final KoekutsukirjeBatch input,
			@Context final HttpServletRequest request) throws IOException,
			DocumentException {
		if (input == null || input.getLetters().isEmpty()) {
			LOG.error("Batch was empty! {}", input);
			return Response.serverError().entity("Batch was empty!").build();
		}
		LOG.info("Creating koekutsukirjeet for {} people", input.getLetters()
				.size());
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		try {
			LOG.info("Authentication {\r\n\tName: {}\r\n\tPrincipal: {}\r\n}",
					new Object[] { auth.getName(), auth.getPrincipal() });

		} catch (Exception e) {
			LOG.error("No authentication!!!");
		}
		final String documentId = globalRandomId();
		executor.execute(new Runnable() {
			public void run() {
				SecurityContextHolder.getContext().setAuthentication(auth);
				try {
					byte[] pdf = koekutsukirjeBuilder.printPDF(input);
					dokumenttiResource
							.tallenna(
									null,
									filenamePrefixWithUsernameAndTimestamp("koekutsukirje.pdf"),
									now().plusDays(1).toDate().getTime(),
									Arrays.asList("viestintapalvelu",
											"koekutsukirje", "pdf"),
									"application/pdf;charset=utf-8",
									new ByteArrayInputStream(pdf));
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("Koekutsukirje PDF async failed: {}",
							e.getMessage());
				}
			}
		});
		return createResponse(request, documentId);
	}

}
