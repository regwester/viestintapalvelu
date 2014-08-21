package fi.vm.sade.viestintapalvelu.letter;

import static fi.vm.sade.viestintapalvelu.Utils.filenamePrefixWithUsernameAndTimestamp;
import static fi.vm.sade.viestintapalvelu.Utils.globalRandomId;
import static org.joda.time.DateTime.now;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.DocumentException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fi.vm.sade.valinta.dokumenttipalvelu.resource.DokumenttiResource;
import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;
import fi.vm.sade.viestintapalvelu.externalinterface.component.EmailComponent;
import fi.vm.sade.viestintapalvelu.validator.LetterBatchValidator;
import fi.vm.sade.viestintapalvelu.validator.UserRightsValidator;

@Component
@Path(Urls.LETTER_PATH)
@PreAuthorize("isAuthenticated()")
// Use HTML-entities instead of scandinavian letters in @Api-description, since
// swagger-ui.js treats model's description as HTML and does not escape it
// properly
@Api(value = "/" + Urls.API_PATH + "/" + Urls.LETTER_PATH, description = "Kirjeiden muodostusrajapinnat")
public class LetterResource extends AsynchronousResource {

    private final Logger LOG = LoggerFactory.getLogger(LetterResource.class);

    @Autowired
    private DownloadCache downloadCache;

    @Autowired
    private LetterBuilder letterBuilder;

    @Qualifier
    private DokumenttiResource dokumenttiResource;

    @Autowired
    private ExecutorService executor;

    @Autowired
    private LetterService letterService;

    @Autowired
    private UserRightsValidator userRightsValidator;
    
    private final static String ApiPDFSync = "Palauttaa URLin, josta voi ladata kirjeen/kirjeet PDF-muodossa; synkroninen";
    private final static String ApiPDFAsync = "Palauttaa URLin, josta voi ladata kirjeen/kirjeet PDF-muodossa; asynkroninen";
    private final static String PDFResponse400 = "BAD_REQUEST; PDF-tiedoston luonti epäonnistui eikä tiedostoa voi noutaa download-linkin avulla.";

    private final static String ApiZIPSync = "Palauttaa URLin, josta voi ladata kirjeen/kirjeet Itellan ZIP-muodossa; synkroninen.";
    private final static String ApiZIPAsync = "Palauttaa URLin, josta voi ladata kirjeen/kirjeet Itellan ZIP-muodossa; asynkroninen";
    private final static String ZIPResponse400 = "BAD_REQUEST; ZIP-tiedoston luonti epäonnistui eikä tiedostoa voi noutaa download-linkin avulla.";

    private final static String TemplateByID = "Palauttaa letter pohjan id:n perusteella.";
    private final static String TemplateByID400 = "Pohjan palautus id:n perusteella epäonnistui.";

    private final static String TemplateByNameOrgTag = "Palauttaa letter pohjan nimen, organisaation ja tunnisteen perusteella.";
    private final static String TemplateByNameOrgTag400 = "Pohjan palautus pohjan nimen, organisaation ja tunnisteen perusteella epäonnistui.";

    private final static String GetLetter = "Palauttaa generoidun/lähetetyn/tallennetun kirjeen Id:n perusteella. Id taulusta 'vastaanottajakirje'";

    private final static String ApiEMAILSync = "Palauttaa URLin, josta voi ladata kirjeen/kirjeet Email-muodossa; synkroninen.";

    @GET
    @Produces("text/plain")
    @Path("/isAlive")
    public String isAlive() {
        return "alive";
    }

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
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_LETTER)
    @ApiOperation(value = ApiPDFSync, notes = ApiPDFSync)
    @ApiResponses(@ApiResponse(code = 400, message = PDFResponse400))
    public Response pdf(
        @ApiParam(value = "Muodostettavien koekutsukirjeiden tiedot (1-n)", required = true) final LetterBatch input,
        @Context HttpServletRequest request) throws IOException, DocumentException {
        String documentId;
        try {
            
            boolean valid = LetterBatchValidator.validate(input);
            LOG.debug("Validated input got " + valid);
            
            byte[] pdf = letterBuilder.printPDF(input);
            
            documentId = downloadCache.addDocument(new Download("application/pdf;charset=utf-8", "letter.pdf", pdf));
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
     * @throws Exception 
     */
    @POST
    @Consumes("application/json")
    @Produces("application/octet-stream")
    @Path("/sync/pdf")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_LETTER)
    @ApiOperation(value = ApiPDFSync, notes = ApiPDFSync)
    @ApiResponses(@ApiResponse(code = 400, message = PDFResponse400))
    public InputStream syncPdf(
        @ApiParam(value = "Muodostettavien koekutsukirjeiden tiedot (1-n)", required = true) final LetterBatch input)
        throws Exception {
        return new ByteArrayInputStream(letterBuilder.printPDF(input));
    }

    /**
     * Kirje PDF async
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
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_LETTER)
    @ApiOperation(value = ApiPDFAsync, notes = ApiPDFAsync + AsyncResponseLogicDocumentation)
    public Response asyncPdf(
        @ApiParam(value = "Muodostettavien kirjeiden tiedot (1-n)", required = true) final LetterBatch input,
        @Context final HttpServletRequest request) throws IOException, DocumentException {
        if (input == null || input.getLetters().isEmpty()) {
            LOG.error("Batch was empty! {}", input);
            return Response.serverError().entity("Batch was empty!").build();
        }
        LOG.info("Creating koekutsukirjeet for {} people", input.getLetters().size());
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
                    byte[] pdf = letterBuilder.printPDF(input);
                    dokumenttiResource.tallenna(null, filenamePrefixWithUsernameAndTimestamp("letter.pdf"), now()
                        .plusDays(1).toDate().getTime(), Arrays.asList("viestintapalvelu", "koekutsukirje", "pdf"),
                        "application/pdf;charset=utf-8", new ByteArrayInputStream(pdf));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("Koekutsukirje PDF async failed: {}", e.getMessage());
                }
            }
        });
        return createResponse(request, documentId);
    }

    @GET
    @Transactional
    @Produces("application/json")
    @Path("/getById")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @ApiOperation(value = TemplateByID, notes = TemplateByID, response = LetterBatch.class)
    // SWAGGER
    @ApiResponses(@ApiResponse(code = 400, message = TemplateByID400))
    // SWAGGER
    public Response templateByID(@Context HttpServletRequest request) throws IOException, DocumentException {
        String letterBatchId = request.getParameter("letterBatchId");
        Long id = Long.parseLong(letterBatchId);

        LetterBatch lb = letterService.findById(id);
        if (lb == null || lb.getTemplateId() == null || lb.getTemplateId() == 0) {
            return Response.status(Status.NOT_FOUND).entity("Template by id " + id + " not found.").build();
        } else {
            return Response.ok(lb).build();
        }
    }

    @GET
    @Transactional
    @Produces("application/json")
    @Path("/getByNameOrgTag")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @ApiOperation(value = TemplateByNameOrgTag, notes = TemplateByNameOrgTag, response = LetterBatch.class)
    // SWAGGER
    @ApiResponses(@ApiResponse(code = 400, message = TemplateByNameOrgTag400))
    // SWAGGER
    public Response templateByNameOidTag(@Context HttpServletRequest request) throws IOException, DocumentException {

        // Pick up the organization oid from request and check user's rights to
        // organization
        String oid = request.getParameter("oid");
        Response response = userRightsValidator.checkUserRightsToOrganization(oid);

        // User isn't authorized to the organization
        if (response.getStatus() != 200) {
            return response;
        }

        String name = request.getParameter("name");
        String language = request.getParameter("language");

        String tag = request.getParameter("tag");

        if ((tag == null) || ("".equals(tag))) {
            tag = "%%";
        }

        return Response.ok(letterService.findLetterBatchByNameOrgTag(name, language, oid, tag)).build();
    }

    @GET
    // @PreAuthorize("isAuthenticated()")
    @Transactional
    @Produces("application/json")
    @Path("/getLetter")
    @ApiOperation(value = GetLetter, notes = GetLetter, response = LetterContent.class)
    // SWAGGER
    public fi.vm.sade.viestintapalvelu.letter.LetterContent getLetter(@Context HttpServletRequest request)
        throws IOException, DocumentException {
        String letterId = request.getParameter("id");
        Long id = Long.parseLong(letterId);

        return letterService.getLetter(id);
    }

    /**
     * Kirjeiden itella ZIP sync
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
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_LETTER)
    @ApiOperation(value = ApiZIPSync, notes = ApiZIPSync)
    @ApiResponses(@ApiResponse(code = 404, message = ZIPResponse400))
    public Response zip(@ApiParam(value = "Muodostettavien kirjeiden tiedot (1-n)", required = true) LetterBatch input,
        @Context HttpServletRequest request) throws IOException, DocumentException, NoSuchAlgorithmException {
        String documentId;
        try {
            byte[] zip = letterBuilder.printZIP(input);
            documentId = downloadCache.addDocument(new Download("application/zip", input.getTemplateName() + ".zip",
                zip));
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Letter ZIP failed: {}", e.getMessage());
            return createFailureResponse(request);
        }
        return createResponse(request, documentId);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/octet-stream")
    @Path("/sync/zip")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_LETTER)
    @ApiOperation(value = ApiZIPSync, notes = ApiZIPSync)
    @ApiResponses(@ApiResponse(code = 404, message = ZIPResponse400))
    public InputStream syncZip(
        @ApiParam(value = "Muodostettavien kirjeiden tiedot (1-n)", required = true) LetterBatch input,
        @Context HttpServletRequest request) throws Exception {
        return new ByteArrayInputStream(letterBuilder.printZIP(input));
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
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_LETTER)
    @ApiOperation(value = ApiZIPAsync, notes = ApiZIPAsync
        + ". Toistaiseksi kirjeen malli on kiinteästi tiedostona jakelupaketissa. " + AsyncResponseLogicDocumentation)
    public Response asynczip(
        @ApiParam(value = "Muodostettavien kirjeiden tiedot (1-n)", required = true) final LetterBatch input,
        @Context final HttpServletRequest request) throws IOException, DocumentException, NoSuchAlgorithmException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String documentId = globalRandomId();
        executor.execute(new Runnable() {
            public void run() {
                SecurityContextHolder.getContext().setAuthentication(auth);
                try {
                    byte[] zip = letterBuilder.printZIP(input);
                    dokumenttiResource.tallenna(null, filenamePrefixWithUsernameAndTimestamp(input.getTemplateName()
                        + ".zip"), now().plusDays(1).toDate().getTime(),
                        Arrays.asList("viestintapalvelu", input.getTemplateName(), "zip"), "application/zip",
                        new ByteArrayInputStream(zip));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("letter ZIP async failed: {}", e.getMessage());
                }
            }
        });
        return createResponse(request, documentId);
    }
}
