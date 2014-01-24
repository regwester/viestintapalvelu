package fi.vm.sade.viestintapalvelu.koekutsukirje;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

import fi.vm.sade.valinta.dokumenttipalvelu.resource.DokumenttiResource;
import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;

@Service
@Singleton
@Path(Urls.KOEKUTSUKIRJE_RESOURCE_PATH)
public class KoekutsukirjeResource extends AsynchronousResource {
    private final Logger LOG = LoggerFactory.getLogger(KoekutsukirjeResource.class);
    private final DownloadCache downloadCache;
    private final KoekutsukirjeBuilder koekutsukirjeBuilder;
    private final DokumenttiResource dokumenttiResource;
    private final ExecutorService executor;

    @Inject
    public KoekutsukirjeResource(KoekutsukirjeBuilder koekutsukirjeBuilder, DownloadCache downloadCache,
            DokumenttiResource dokumenttiResource, ExecutorService executor) {
        this.koekutsukirjeBuilder = koekutsukirjeBuilder;
        this.downloadCache = downloadCache;
        this.dokumenttiResource = dokumenttiResource;
        this.executor = executor;
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
    public Response pdf(KoekutsukirjeBatch input, @Context HttpServletRequest request) throws IOException,
            DocumentException {
        byte[] pdf = koekutsukirjeBuilder.printPDF(input);
        String documentId = downloadCache.addDocument(new Download("application/pdf;charset=utf-8",
                "koekutsukirje.pdf", pdf));
        return createResponse(request, documentId);
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
    public Response asyncPdf(final KoekutsukirjeBatch input, @Context HttpServletRequest request) throws IOException,
            DocumentException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String documentId = globalRandomId();
        executor.execute(new Runnable() {
            public void run() {
                SecurityContextHolder.getContext().setAuthentication(auth);
                try {
                    byte[] pdf = koekutsukirjeBuilder.printPDF(input);
                    dokumenttiResource.tallenna(filenamePrefixWithUsernameAndTimestamp("koekutsukirje.pdf"), now()
                            .plusDays(1).toDate().getTime(),
                            Arrays.asList("viestintapalvelu", "koekutsukirje", "pdf"),
                            "application/pdf;charset=utf-8", new ByteArrayInputStream(pdf));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("Koekutsukirje PDF async failed: {}", e.getMessage());
                }
            }
        });
        return createResponse(request, documentId);
    }

}
