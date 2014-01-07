package fi.vm.sade.viestintapalvelu.download;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Collection;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Service;

import fi.vm.sade.viestintapalvelu.Urls;

@Service
@Singleton
@Path(Urls.DOWNLOAD_RESOURCE_PATH)
public class DownloadResource {
    private DownloadCache downloadCache;

    @Inject
    public DownloadResource(DownloadCache downloadCache) {
        this.downloadCache = downloadCache;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Collection<Header> available() {
        // Wrapping to TreeSet automatically sorts the elements as TreeSet is
        // SortedSet. Header is sorted by createdAt value.
        return new TreeSet<Header>(downloadCache.getListOfAvailableDocuments());
    }

    @GET
    @Path("{documentId}")
    public Response download(@PathParam("documentId") String input, @Context HttpServletResponse response) {
        Download download = downloadCache.get(input);
        if (download == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        response.setHeader("Content-Type", download.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + download.getFilename() + "\"");
        response.setHeader("Content-Length", String.valueOf(download.toByteArray().length));
        return Response.ok(download.toByteArray()).type(download.getContentType()).build();
    }
}
