package fi.vm.sade.viestintapalvelu;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import fi.vm.sade.viestintapalvelu.download.DownloadResource;

public class AsynchronousResource {
	protected Response createResponse(HttpServletRequest request,
			String documentId) {
		String resultUrl = urlTo(request, DownloadResource.class);
		URI contentLocation = URI.create(resultUrl + "/" + documentId);
		return Response.status(Status.ACCEPTED)
				.contentLocation(contentLocation)
				.entity(contentLocation.toString()).build();
	}

	private String urlTo(HttpServletRequest request,
			Class<DownloadResource> resourceClass) {
		return UriBuilder
				.fromUri(request.getRequestURL().toString())
				.replacePath(
						request.getServletPath()
								+ "/"
								+ UriBuilder.fromResource(resourceClass)
										.build()).build().toString();
	}
}
