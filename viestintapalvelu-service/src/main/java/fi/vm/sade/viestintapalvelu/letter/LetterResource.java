package fi.vm.sade.viestintapalvelu.letter;

import static fi.vm.sade.viestintapalvelu.Utils.filenamePrefixWithUsernameAndTimestamp;
import static fi.vm.sade.viestintapalvelu.Utils.globalRandomId;
import static org.joda.time.DateTime.now;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
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

import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.valinta.dokumenttipalvelu.resource.DokumenttiResource;
import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;

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
    private CurrentUserComponent currentUserComponent;
    @Autowired
    private DownloadCache downloadCache;
    @Autowired
    private LetterBuilder letterBuilder;
    @Autowired
    private DokumenttiResource dokumenttiResource;
    @Autowired
    private ExecutorService executor;

    @Autowired 
    private LetterService letterService;
    
    private final static String ApiPDFSync = "Palauttaa URLin, josta voi ladata kirjeen/kirjeet PDF-muodossa; synkroninen";
    private final static String ApiPDFAsync = "Palauttaa URLin, josta voi ladata kirjeen/kirjeet PDF-muodossa; asynkroninen";
    private final static String PDFResponse400 = "BAD_REQUEST; PDF-tiedoston luonti epäonnistui eikä tiedostoa voi noutaa download-linkin avulla.";

    private final static String ApiZIPSync = "Palauttaa URLin, josta voi ladata kirjeen/kirjeet Itellan ZIP-muodossa; synkroninen.";
    private final static String ApiZIPAsync = "Palauttaa URLin, josta voi ladata kirjeen/kirjeet Itellan ZIP-muodossa; asynkroninen";
    private final static String ZIPResponse400 = "BAD_REQUEST; ZIP-tiedoston luonti epäonnistui eikä tiedostoa voi noutaa download-linkin avulla.";

    
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
//    @Secured("ROLE_APP_LETTER_CREATE")
    @ApiOperation(value = ApiPDFSync, notes = ApiPDFSync)
    @ApiResponses(@ApiResponse(code = 400, message = PDFResponse400))
    public Response pdf(
            @ApiParam(value = "Muodostettavien koekutsukirjeiden tiedot (1-n)", required = true) final LetterBatch input,
            @Context HttpServletRequest request) throws IOException,
            DocumentException {
        String documentId;
        try {
            byte[] pdf = letterBuilder.printPDF(input);
            documentId = downloadCache.addDocument(new Download(
                    "application/pdf;charset=utf-8", "letter.pdf", pdf));
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
//    @Secured("ROLE_APP_LETTER_CREATE")
    @ApiOperation(value = ApiPDFSync, notes = ApiPDFSync)
    @ApiResponses(@ApiResponse(code = 400, message = PDFResponse400))
    public InputStream syncPdf(
            @ApiParam(value = "Muodostettavien koekutsukirjeiden tiedot (1-n)", required = true) final LetterBatch input)
            throws IOException, DocumentException {
        return new ByteArrayInputStream(letterBuilder.printPDF(input));
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
    @Produces("text/plain")
    @Path("/async/pdf")
//    @Secured("ROLE_APP_LETTER_CREATE")
    @ApiOperation(value = ApiPDFAsync, notes = ApiPDFAsync
            + AsyncResponseLogicDocumentation)
    public Response asyncPdf(
            @ApiParam(value = "Muodostettavien koekutsukirjeiden tiedot (1-n)", required = true) final LetterBatch input,
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
                    byte[] pdf = letterBuilder.printPDF(input);
                    dokumenttiResource
                            .tallenna(
                                    null,
                                    filenamePrefixWithUsernameAndTimestamp("letter.pdf"),
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

    @GET
    @Transactional
    @Produces("application/json")
    @Path("/getById")
//    @Secured("ROLE_APP_TEMPLATE_READ")
    public LetterBatch templateByID(@Context HttpServletRequest request) throws IOException, DocumentException {       
       String letterBatchId = request.getParameter("letterBatchId");
       Long id = Long.parseLong(letterBatchId);
       
       return letterService.findById(id);
    }
    
    @GET
    @Transactional
    @Produces("application/json")
    @Path("/getByNameOrgTag")
//    @Secured("ROLE_APP_TEMPLATE_READ")
    public Response templateByNameOidTag(@Context HttpServletRequest request) throws IOException, DocumentException {
        // Pick up the organization oid from request and check urer's rights to organization
        String oid = request.getParameter("oid");
        Response response = checkUserRights(oid); 
        
        // User isn't authorized to the organization
        if (response.getStatus() != 200) {
            return response;
        }
       
       String name = request.getParameter("name");
       String tag = request.getParameter("tag");
       if ((tag==null) || ("".equals(tag))) {
    	   tag="%%";
       }
       
       return Response.ok(letterService.findLetterBatchByNameOrgTag(name, oid, tag)).build();
    }

    
    // FOR TESTING
    @POST
    @Path("/createLetter")
    @Consumes("application/json")
    @Produces("application/json")
    public fi.vm.sade.viestintapalvelu.model.LetterBatch createLetter(LetterBatch letterBatch) 
        throws IOException, DocumentException {
        return letterService.createLetter(letterBatch);
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
//    @Secured("ROLE_APP_LETTER_CREATE")
    @ApiOperation(value = ApiZIPSync, notes = ApiZIPSync)
    @ApiResponses(@ApiResponse(code = 404, message = ZIPResponse400))
    public Response zip(
            @ApiParam(value = "Muodostettavien kirjeiden tiedot (1-n)", required = true) LetterBatch input,
            @Context HttpServletRequest request) throws IOException,
            DocumentException, NoSuchAlgorithmException {
        String documentId;
        try {
            byte[] zip = letterBuilder.printZIP(input);
            documentId = downloadCache.addDocument(new Download(
                    "application/zip", input.getTemplateName()+".zip", zip));
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
//    @Secured("ROLE_APP_LETTER_CREATE")
    @ApiOperation(value = ApiZIPSync, notes = ApiZIPSync)
    @ApiResponses(@ApiResponse(code = 404, message = ZIPResponse400))
    public InputStream syncZip(
            @ApiParam(value = "Muodostettavien kirjeiden tiedot (1-n)", required = true) LetterBatch input,
            @Context HttpServletRequest request) throws IOException,
            DocumentException, NoSuchAlgorithmException {
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
//    @Secured("ROLE_APP_LETTER_CREATE")
    @ApiOperation(value = ApiZIPAsync, notes = ApiZIPAsync
            + ". Toistaiseksi kirjeen malli on kiinteästi tiedostona jakelupaketissa. "
            + AsyncResponseLogicDocumentation)
    public Response asynczip(
            @ApiParam(value = "Muodostettavien kirjeiden tiedot (1-n)", required = true) final LetterBatch input,
            @Context final HttpServletRequest request) throws IOException,
            DocumentException, NoSuchAlgorithmException {
        final Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        final String documentId = globalRandomId();
        executor.execute(new Runnable() {
            public void run() {
                SecurityContextHolder.getContext().setAuthentication(auth);
                try {
                    byte[] zip = letterBuilder.printZIP(input);
                    dokumenttiResource
                            .tallenna(
                                    null,
                                    filenamePrefixWithUsernameAndTimestamp(input.getTemplateName()+".zip"),
                                    now().plusDays(1).toDate().getTime(),
                                    Arrays.asList("viestintapalvelu",
                                            input.getTemplateName(), "zip"),
                                    "application/zip",
                                    new ByteArrayInputStream(zip));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("letter ZIP async failed: {}",
                            e.getMessage());
                }
            }
        });
        return createResponse(request, documentId);
    }

    private Response checkUserRights(String oid) {
        if (oid == null) {
            return Response.status(Status.OK).build();
        }
        
        List<OrganisaatioHenkilo> organisaatioHenkiloList = currentUserComponent.getCurrentUserOrganizations();
        
        for (OrganisaatioHenkilo organisaatioHenkilo : organisaatioHenkiloList) {
            if (oid.equals(organisaatioHenkilo.getOrganisaatioOid())) {
                return Response.status(Status.OK).build();
            }
        }
        
        return Response.status(Status.FORBIDDEN).entity("User is not authorized to the organization " + oid).build();
    }
}
