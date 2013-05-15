package fi.vm.sade.viestintapalvelu.download;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Path("download")
public class DownloadResource {
	private DownloadCache downloadCache;

	@Inject
	public DownloadResource(DownloadCache downloadCache) {
		this.downloadCache = downloadCache;
	}

	@GET
	@Path("document/{documentId}")
	public Response download(@PathParam("documentId") String input,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		Download download = downloadCache.get(request.getSession().getId(), input);
		if (download == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ download.getFilename() + "\"");
		return Response
				.ok(download.toByteArray())
				.type(download.getContentType()).build();
	}
}
