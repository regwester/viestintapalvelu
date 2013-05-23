package fi.vm.sade.viestintapalvelu;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import fi.vm.sade.viestintapalvelu.download.DownloadResource;

public class AsynchronousResource {
	// TODO vpeurala 21.5.2013: Move to singleton
	private ExecutorService executorService = Executors.newCachedThreadPool();

	public void executeAsynchronously(Runnable task) {
		executorService.execute(task);
	}

	protected Response createResponse(HttpServletRequest request,
			String documentId) {
		String resultUrl = urlTo(request, DownloadResource.class);
		URI contentLocation = URI.create(resultUrl + "/" + documentId);
		return Response.status(Status.ACCEPTED)
				.contentLocation(contentLocation)
				.entity(contentLocation.toString()).build();
	}

	// FIXME vpeurala 23.5.2013: Hack
	String urlTo(HttpServletRequest request,
			Class<DownloadResource> resourceClass) {
		return UriBuilder
				.fromUri(request.getRequestURL().toString())
				.replacePath(
						chompSlashes(request.getContextPath().trim().equals("") ? ""
								: "/" + request.getContextPath())
								+ "/"
								+ chompSlashes((request.getServletPath().trim()
										.equals("") ? "" : "/"
										+ request.getServletPath()))
								+ "/"
								+ chompSlashes((UriBuilder.fromResource(
										resourceClass).build().toString())))
				.build().toString();
	}

	private static String chompSlashes(final String input) {
		String processed = input.trim();
		while (processed.startsWith("/")) {
			processed = processed.substring(1);
		}
		while (processed.endsWith("/")) {
			processed = processed.substring(0, processed.length() - 1);
		}
		return processed;
	}
}
