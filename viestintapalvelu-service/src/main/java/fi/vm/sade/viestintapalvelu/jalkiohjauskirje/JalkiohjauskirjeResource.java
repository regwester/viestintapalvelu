package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import static fi.vm.sade.viestintapalvelu.Utils.filenamePrefixWithUsernameAndTimestamp;
import static fi.vm.sade.viestintapalvelu.Utils.globalRandomId;
import static org.joda.time.DateTime.now;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@Path(Urls.JALKIOHJAUSKIRJE_RESOURCE_PATH)
// Use HTML-entities instead of scandinavian letters in @Api-description, since
// swagger-ui.js treats model's description as HTML and does not escape it
// properly
@Api(value = "/" + Urls.API_PATH + "/" + Urls.JALKIOHJAUSKIRJE_RESOURCE_PATH, description = "J&auml;lkiohjauskirjeen k&auml;sittelyn rajapinnat")
public class JalkiohjauskirjeResource extends AsynchronousResource {
	private final Logger LOG = LoggerFactory
			.getLogger(JalkiohjauskirjeResource.class);

	@Autowired
	private DownloadCache downloadCache;
	@Autowired
	private JalkiohjauskirjeBuilder jalkiohjauskirjeBuilder;
	@Autowired
	private DokumenttiResource dokumenttiResource;
	@Autowired
	private Validator validator;
	@Autowired
	private ExecutorService executor;

	private final static String FixedTemplateNote = "Toistaiseksi kirjeen malli on kiinteästi tiedostona jakelupaketissa.";
	private final static String ApiPDFSync = "Palauttaa URLin, josta voi ladata jälkiohjauskirjeen/kirjeet PDF-muodossa; synkroninen. "
			+ FixedTemplateNote;
	private final static String ApiZIPSync = "Palauttaa URLin, josta voi ladata jälkiohjauskirjeen/kirjeet Itellan ZIP-muodossa; synkroninen. "
			+ FixedTemplateNote;
	private final static String ApiZIPAsync = "Palauttaa URLin, josta voi ladata jälkiohjauskirjeen/kirjeet Itellan ZIP-muodossa; asynkroninen";
	private final static String PDFResponse400 = "BAD_REQUEST; PDF-tiedoston luonti epäonnistui eikä tiedostoa voi noutaa download-linkin avulla.";
	private final static String ZIPResponse400 = "BAD_REQUEST; ZIP-tiedoston luonti epäonnistui eikä tiedostoa voi noutaa download-linkin avulla.";

	/**
	 * Jalkiohjauskirje PDF sync
	 * 
	 * @param jalkiohjauskirjeBatch
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
			@ApiParam(value = "Muodostettavien jälkiohjauskirjeiden tiedot (1-n)", required = true) JalkiohjauskirjeBatch jalkiohjauskirjeBatch,
			@Context HttpServletRequest request) throws IOException,
			DocumentException {
		String documentId;
		try {
			Set<ConstraintViolation<JalkiohjauskirjeBatch>> validate = validator
					.validate(jalkiohjauskirjeBatch);
			if (!validate.isEmpty()) {
				throw new IllegalJalkiohjauskirjeException(validate);
			}
			byte[] pdf = jalkiohjauskirjeBuilder
					.printPDF(jalkiohjauskirjeBatch);
			documentId = downloadCache.addDocument(new Download(
					"application/pdf;charset=utf-8", "jalkiohjauskirje.pdf",
					pdf));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Jälkiohjauskirje PDF failed: {}", e.getMessage());
			return createFailureResponse(request);
		}
		return createResponse(request, documentId);
	}

	/**
	 * Jalkihohjauskirje ZIP sync
	 * 
	 * @param input
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws NoSuchAlgorithmException
	 */
	@POST
	@Consumes("application/json")
	@Produces("text/plain")
	@Path("/zip")
	@ApiOperation(value = ApiZIPSync, notes = ApiZIPSync)
	@ApiResponses(@ApiResponse(code = 404, message = ZIPResponse400))
	public Response zip(
			@ApiParam(value = "Muodostettavien jälkiohjauskirjeiden tiedot (1-n)", required = true) JalkiohjauskirjeBatch input,
			@Context HttpServletRequest request) throws IOException,
			DocumentException, NoSuchAlgorithmException {
		String documentId;
		try {
			byte[] zip = jalkiohjauskirjeBuilder.printZIP(input);
			documentId = downloadCache.addDocument(new Download(
					"application/zip", "jalkiohjauskirje.zip", zip));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Jälkiohjauskirje ZIP failed: {}", e.getMessage());
			return createFailureResponse(request);
		}
		return createResponse(request, documentId);
	}

	/**
	 * Jalkihohjauskirje ZIP async
	 * 
	 * @param input
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws NoSuchAlgorithmException
	 */
	@POST
	@Consumes("application/json")
	@Produces("text/plain")
	@Path("/async/zip")
	@ApiOperation(value = ApiZIPAsync, notes = ApiZIPAsync
			+ ". Toistaiseksi kirjeen malli on kiinteästi tiedostona jakelupaketissa. "
			+ AsyncResponseLogicDocumentation)
	public Response asynczip(
			@ApiParam(value = "Muodostettavien jälkiohjauskirjeiden tiedot (1-n)", required = true) final JalkiohjauskirjeBatch input,
			@Context final HttpServletRequest request) throws IOException,
			DocumentException, NoSuchAlgorithmException {
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		final String documentId = globalRandomId();
		executor.execute(new Runnable() {
			public void run() {
				SecurityContextHolder.getContext().setAuthentication(auth);
				try {
					byte[] zip = jalkiohjauskirjeBuilder.printZIP(input);
					dokumenttiResource
							.tallenna(
									filenamePrefixWithUsernameAndTimestamp("jalkiohjauskirje.zip"),
									now().plusDays(1).toDate().getTime(),
									Arrays.asList("viestintapalvelu",
											"jalkiohjauskirje", "zip"),
									"application/zip",
									new ByteArrayInputStream(zip));
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("Jalkiohjauskirje ZIP async failed: {}",
							e.getMessage());
				}
			}
		});
		return createResponse(request, documentId);
	}
}
