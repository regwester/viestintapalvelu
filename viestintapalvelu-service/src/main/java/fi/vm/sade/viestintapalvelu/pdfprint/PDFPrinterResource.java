package fi.vm.sade.viestintapalvelu.pdfprint;

import static fi.vm.sade.viestintapalvelu.Utils.filenamePrefixWithUsernameAndTimestamp;
import static fi.vm.sade.viestintapalvelu.Utils.globalRandomId;
import static org.joda.time.DateTime.now;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities.EscapeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.lowagie.text.DocumentException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;

import fi.vm.sade.valinta.dokumenttipalvelu.resource.DokumenttiResource;
import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.MergedPdfDocument;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;
import fi.vm.sade.viestintapalvelu.letter.LetterResource;

@Component
@Path(Urls.PRINTER_PATH)
@PreAuthorize("isAuthenticated()")
@Api(value = "/" + Urls.API_PATH + "/" + Urls.PRINTER_PATH, description = "Pdf tulosteiden muodostus rajapinta")
public class PDFPrinterResource extends AsynchronousResource {
    private final Logger LOG = LoggerFactory.getLogger(LetterResource.class);

    @Autowired
    private DokumenttiResource dokumenttiResource;
    @Autowired
    private DownloadCache downloadCache;
    @Autowired
    private ExecutorService executor;
    @Autowired
    private DocumentBuilder documentBuilder;

    @GET
    @Produces("text/plain")
    @Path("/isAlive")
    public String isAlive() {
        return "alive";
    }

    /**
     * PDF sync
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
    // @ApiOperation(value = ApiPDFSync, notes = ApiPDFSync)
    // @ApiResponses(@ApiResponse(code = 400, message = PDFResponse400))
    public Response pdf(
            @ApiParam(value = "Muodostettavien koekutsukirjeiden tiedot (1-n)", required = true) DocumentSource input,
            @Context HttpServletRequest request) throws IOException,
            DocumentException {
        String documentId = null;
        try {
            byte[] pdf = buildDocument(input);
            String documentName = input.getDocumentName();
            documentName = input.getDocumentName() == null ? "document.pdf"
                    : input.getDocumentName() + ".pdf";
            documentId = downloadCache.addDocument(new Download(
                    "application/pdf;charset=utf-8", documentName, pdf));
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Sync PDF failed: {}", e.getMessage());
            return createFailureResponse(request);
        }
        return createResponse(request, documentId);
    }

    /**
     * Get PDF content
     * 
     * @param input
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    @POST
    @Consumes("application/json")
    @Produces("application/pdf")
    @Path("/pdf/content")
    // @ApiOperation(value = "Muodostaa HTML-pohjaisesta asiakirja PDF-dokumentin ja palauttaa sen", notes = "")
    // @ApiResponses(@ApiResponse(code = 400, message = "Document creation failed"))
    public Response pdfContent(
        @ApiParam(value = "Muodostettavien asiakirjojen tiedot (1-n)", required = true) DocumentSource input,
        @Context HttpServletRequest request) throws IOException, DocumentException {
        try {
            byte[] pdf = buildDocument(input);
            return Response.ok(pdf).build();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Getting PDF content failed: {}", e.getMessage());
            return createFailureResponse(request);
        }
    }
    
    /**
     * PDF async
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
    // @ApiOperation(value = ApiPDFAsync, notes = ApiPDFAsync
    // + AsyncResponseLogicDocumentation)
    public Response asyncPdf(
            @ApiParam(value = "PDF source", required = true) final DocumentSource input,
            @Context final HttpServletRequest request) throws IOException,
            DocumentException {
        if (input == null || input.getSources().isEmpty()) {
            LOG.error("Nothing to do ", input);
            return Response.serverError().entity("Batch was empty!").build();
        }
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
                    byte[] pdf = buildDocument(input);
                    String documentName = input.getDocumentName();
                    documentName = input.getDocumentName() == null ? "document.pdf"
                            : input.getDocumentName() + ".pdf";
                    dokumenttiResource
                            .tallenna(
                                    null,
                                    filenamePrefixWithUsernameAndTimestamp("documentName"),
                                    now().plusDays(1).toDate().getTime(),
                                    Arrays.asList("viestintapalvelu",
                                            documentName, "pdf"),
                                    "application/pdf;charset=utf-8",
                                    new ByteArrayInputStream(pdf));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("PDF async failed: {}", e.getMessage());
                }
            }
        });
        return createResponse(request, documentId);
    }
    
    @GET
    @Path("/getDocumentSource")
    @Produces("application/json")
    public Response getDocumentSource() {
        DocumentSource ds = new DocumentSource();
        
        List<String> sources = new ArrayList<String>();
        sources.add("documentsource text");
        ds.setDocumentName("documentName");
        ds.setSources(sources);
        
        return Response.ok(ds).build();
    }

    private byte[] buildDocument(DocumentSource input)
            throws DocumentException, IOException {
        List<PdfDocument> pdfs = new ArrayList<PdfDocument>();

        for (String source : input.getSources()) {
            Document jsoupDoc = Jsoup.parse(source);
            jsoupDoc.outputSettings(new OutputSettings().escapeMode(EscapeMode.xhtml));
            String parsedSource = jsoupDoc.toString();
            byte[] pdf = documentBuilder.xhtmlToPDF(parsedSource.getBytes());
            PdfDocument doc = new PdfDocument(null, pdf, null);
            pdfs.add(doc);
        }
        MergedPdfDocument mPdf = documentBuilder.merge(pdfs);
        return mPdf.toByteArray();
    }
}
