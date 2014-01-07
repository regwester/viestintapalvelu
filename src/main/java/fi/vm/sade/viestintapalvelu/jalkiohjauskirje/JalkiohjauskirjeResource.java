package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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

import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;

@Service
@Singleton
@Path(Urls.JALKIOHJAUSKIRJE_RESOURCE_PATH)
public class JalkiohjauskirjeResource extends AsynchronousResource {
    private DownloadCache downloadCache;
    private JalkiohjauskirjeBuilder jalkiohjauskirjeBuilder;
    private final Validator validator;
    private final ExecutorService executor;
    private final Logger LOG = LoggerFactory.getLogger(JalkiohjauskirjeResource.class);

    @Inject
    public JalkiohjauskirjeResource(JalkiohjauskirjeBuilder jalkiohjauskirjeBuilder, DownloadCache downloadCache,
            Validator validator, ExecutorService executor) {
        this.jalkiohjauskirjeBuilder = jalkiohjauskirjeBuilder;
        this.downloadCache = downloadCache;
        this.validator = validator;
        this.executor = executor;
    }

    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    @Path("pdf")
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

    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    @Path("zip")
    public Response zip(JalkiohjauskirjeBatch input, @Context HttpServletRequest request) throws IOException,
            DocumentException, NoSuchAlgorithmException {
        byte[] zip = jalkiohjauskirjeBuilder.printZIP(input);
        String documentId = downloadCache.addDocument(new Download("application/zip", "jalkiohjauskirje.zip", zip));
        return createResponse(request, documentId);
    }

    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    @Path("asynczip")
    public Response asynczip(final JalkiohjauskirjeBatch input, @Context HttpServletRequest request)
            throws IOException, DocumentException, NoSuchAlgorithmException {
        executor.execute(new Runnable() {

            @Override
            public void run() {
                byte[] zip;
                try {
                    zip = jalkiohjauskirjeBuilder.printZIP(input);
                    String documentId = downloadCache.addDocument(new Download("application/zip",
                            "jalkiohjauskirje.zip", zip));
                    LOG.info("Jälkiohjauskirje {} luotu", documentId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return createResponse(request, "");
    }
}
