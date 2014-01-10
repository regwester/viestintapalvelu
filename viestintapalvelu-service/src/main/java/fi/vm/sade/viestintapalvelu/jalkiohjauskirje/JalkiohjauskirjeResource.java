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

import javax.inject.Inject;
import javax.inject.Singleton;
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
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

import fi.vm.sade.valinta.dokumenttipalvelu.resource.DokumenttiResource;
import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;

@Service
@Singleton
@Path(Urls.JALKIOHJAUSKIRJE_RESOURCE_PATH)
public class JalkiohjauskirjeResource extends AsynchronousResource {
    private final Logger LOG = LoggerFactory.getLogger(JalkiohjauskirjeResource.class);
    private DownloadCache downloadCache;
    private JalkiohjauskirjeBuilder jalkiohjauskirjeBuilder;
    private final DokumenttiResource dokumenttiResource;
    private final Validator validator;
    private final ExecutorService executor;

    @Inject
    public JalkiohjauskirjeResource(JalkiohjauskirjeBuilder jalkiohjauskirjeBuilder, DownloadCache downloadCache,
            Validator validator, ExecutorService executor, DokumenttiResource dokumenttiResource) {
        this.jalkiohjauskirjeBuilder = jalkiohjauskirjeBuilder;
        this.downloadCache = downloadCache;
        this.validator = validator;
        this.executor = executor;
        this.dokumenttiResource = dokumenttiResource;
    }

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
    public Response pdf(JalkiohjauskirjeBatch jalkiohjauskirjeBatch, @Context HttpServletRequest request)
            throws IOException, DocumentException {
        Set<ConstraintViolation<JalkiohjauskirjeBatch>> validate = validator.validate(jalkiohjauskirjeBatch);
        if (!validate.isEmpty()) {
            throw new IllegalJalkiohjauskirjeException(validate);
        }
        byte[] pdf = jalkiohjauskirjeBuilder.printPDF(jalkiohjauskirjeBatch);
        String documentId = downloadCache.addDocument(new Download("application/pdf;charset=utf-8",
                "jalkiohjauskirje.pdf", pdf));
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
    public Response zip(JalkiohjauskirjeBatch input, @Context HttpServletRequest request) throws IOException,
            DocumentException, NoSuchAlgorithmException {
        byte[] zip = jalkiohjauskirjeBuilder.printZIP(input);
        String documentId = downloadCache.addDocument(new Download("application/zip", "jalkiohjauskirje.zip", zip));
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
    public Response asynczip(final JalkiohjauskirjeBatch input, @Context HttpServletRequest request)
            throws IOException, DocumentException, NoSuchAlgorithmException {
        final String documentId = globalRandomId();
        executor.execute(new Runnable() {
            public void run() {
                try {
                    byte[] zip = jalkiohjauskirjeBuilder.printZIP(input);
                    dokumenttiResource.tallenna(filenamePrefixWithUsernameAndTimestamp("jalkiohjauskirje.zip"), now()
                            .plusDays(1).toDate().getTime(),
                            Arrays.asList("viestintapalvelu", "jalkiohjauskirje", "zip"), "application/zip",
                            new ByteArrayInputStream(zip));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("Jalkiohjauskirje ZIP async failed: {}", e.getMessage());
                }
            }
        });
        return createResponse(request, documentId);
    }
}
