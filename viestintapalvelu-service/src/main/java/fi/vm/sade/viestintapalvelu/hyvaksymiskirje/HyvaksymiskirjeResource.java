package fi.vm.sade.viestintapalvelu.hyvaksymiskirje;

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
@Path(Urls.HYVAKSYMISKIRJE_RESOURCE_PATH)
public class HyvaksymiskirjeResource extends AsynchronousResource {
    private final Logger LOG = LoggerFactory.getLogger(HyvaksymiskirjeResource.class);
    private final DownloadCache downloadCache;
    private final HyvaksymiskirjeBuilder hyvaksymiskirjeBuilder;
    private final DokumenttiResource dokumenttiResource;
    private final ExecutorService executor;

    @Inject
    public HyvaksymiskirjeResource(HyvaksymiskirjeBuilder jalkiohjauskirjeBuilder, DownloadCache downloadCache,
            DokumenttiResource dokumenttiResource, ExecutorService executor) {
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
    public Response pdf(HyvaksymiskirjeBatch input, @Context HttpServletRequest request) throws IOException,
            DocumentException {
        byte[] pdf = hyvaksymiskirjeBuilder.printPDF(input);
        String documentId = downloadCache.addDocument(new Download("application/pdf;charset=utf-8",
                "hyvaksymiskirje.pdf", pdf));
        return createResponse(request, documentId);
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
    public Response asyncPdf(final HyvaksymiskirjeBatch input, @Context HttpServletRequest request) throws IOException,
            DocumentException {
        final String documentId = globalRandomId();
        executor.execute(new Runnable() {
            public void run() {
                try {
                    byte[] pdf = hyvaksymiskirjeBuilder.printPDF(input);
                    dokumenttiResource.tallenna(filenamePrefixWithUsernameAndTimestamp("hyvaksymiskirje.pdf"), now()
                            .plusDays(1).toDate().getTime(),
                            Arrays.asList("viestintapalvelu", "hyvaksymiskirje", "pdf"),
                            "application/pdf;charset=utf-8", new ByteArrayInputStream(pdf));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("Hyvaksymiskirje PDF async failed: {}", e.getMessage());
                }
            }
        });
        return createResponse(request, documentId);
    }

}
