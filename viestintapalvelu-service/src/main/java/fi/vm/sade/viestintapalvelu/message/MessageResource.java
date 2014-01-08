package fi.vm.sade.viestintapalvelu.message;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;

/**
 * 
 * @author Jussi Jartamo
 * 
 *         Temporary resource for messaging
 */
@Service
@Singleton
@Path(Urls.MESSAGE_RESOURCE_PATH)
public class MessageResource {
    private DownloadCache downloadCache;

    @Inject
    public MessageResource(DownloadCache downloadCache) {
        this.downloadCache = downloadCache;
    }

    @POST
    @Produces(APPLICATION_JSON)
    public Response message(String message) {
        downloadCache.addDocument(new Download("text/plain", message, new byte[] {}));
        return Response.ok().build();
    }
}
