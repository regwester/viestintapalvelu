package fi.vm.sade.viestintapalvelu.ui;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fi.vm.sade.viestintapalvelu.domain.download.Download;
import fi.vm.sade.viestintapalvelu.domain.download.DownloadCache;

@Singleton
@Path(Urls.DOWNLOAD_RESOURCE_PATH)
public class DownloadResource {
	private DownloadCache downloadCache;

	@Inject
	public DownloadResource(DownloadCache downloadCache) {
		this.downloadCache = downloadCache;
	}

	@GET
	@Path("{documentId}")
	public Response download(@PathParam("documentId") String input,
			@Context HttpServletResponse response) {
		Download download = downloadCache.get(input);
		if (download == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		response.setHeader("Content-Type", download.getContentType());
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ download.getFilename() + "\"");
		response.setHeader("Content-Length",
				String.valueOf(download.toByteArray().length));
		return Response.ok(download.toByteArray())
				.type(download.getContentType()).build();
	}
}
