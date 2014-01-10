package fi.vm.sade.viestintapalvelu.address;

import static fi.vm.sade.viestintapalvelu.Utils.filenamePrefixWithUsernameAndTimestamp;
import static fi.vm.sade.viestintapalvelu.Utils.globalRandomId;
import static org.joda.time.DateTime.now;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

import fi.vm.sade.valinta.dokumenttipalvelu.resource.DokumenttiResource;
import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;

@Service
@Singleton
@Path(Urls.ADDRESS_LABEL_RESOURCE_PATH)
public class AddressLabelResource extends AsynchronousResource {
    private final Logger LOG = LoggerFactory.getLogger(AddressLabelResource.class);
    private final DownloadCache downloadCache;
    private final AddressLabelBuilder labelBuilder;
    private final DokumenttiResource dokumenttiResource;
    private final ExecutorService executor;

    @Inject
    public AddressLabelResource(AddressLabelBuilder labelBuilder, DownloadCache downloadCache,
            DokumenttiResource dokumenttiResource, ExecutorService executor) {
        this.labelBuilder = labelBuilder;
        this.downloadCache = downloadCache;
        this.dokumenttiResource = dokumenttiResource;
        this.executor = executor;
    }

    // Sync routes

    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    @Path("/pdf")
    public Response pdf(final AddressLabelBatch input, @Context HttpServletRequest request) throws IOException,
            DocumentException {
        byte[] pdf = labelBuilder.printPDF(input);
        final String documentId = downloadCache.addDocument(new Download("application/pdf;charset=utf-8",
                "addresslabels.pdf", pdf));
        return createResponse(request, documentId);
    }

    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    @Path("/xls")
    public Response xls(AddressLabelBatch input, @Context HttpServletRequest request) throws IOException,
            DocumentException {
        byte[] csv = labelBuilder.printCSV(input);
        String documentId = downloadCache
                .addDocument(new Download("application/vnd.ms-excel", "addresslabels.xls", csv));
        return createResponse(request, documentId);
    }

    // Async routes
    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    @Path("/async/pdf")
    public Response asyncPdf(final AddressLabelBatch input, @Context HttpServletRequest request) throws IOException,
            DocumentException {
        final String documentId = globalRandomId();
        executor.execute(new Runnable() {
            public void run() {
                try {
                    byte[] pdf = labelBuilder.printPDF(input);
                    dokumenttiResource.tallenna(filenamePrefixWithUsernameAndTimestamp("addresslabels.pdf"), now()
                            .plusDays(1).toDate().getTime(), Arrays.asList("viestintapalvelu", "addresslabels", "pdf"),
                            "application/pdf;charset=utf-8", new ByteArrayInputStream(pdf));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("AddressLabel PDF failed: {}", e.getMessage());
                }
            }
        });
        return createResponse(request, documentId);
    }

    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    @Path("/async/xls")
    public Response asyncXls(final AddressLabelBatch input, @Context HttpServletRequest request) throws IOException,
            DocumentException {
        final String documentId = globalRandomId();
        executor.execute(new Runnable() {
            public void run() {
                try {
                    byte[] csv = labelBuilder.printCSV(input);
                    dokumenttiResource.tallenna(filenamePrefixWithUsernameAndTimestamp("addresslabels.xls"), now()
                            .plusDays(1).toDate().getTime(), Arrays.asList("viestintapalvelu", "addresslabels", "xls"),
                            "application/vnd.ms-excel", new ByteArrayInputStream(csv));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("AddressLabel PDF failed: {}", e.getMessage());
                }
            }
        });
        return createResponse(request, documentId);
    }
}
