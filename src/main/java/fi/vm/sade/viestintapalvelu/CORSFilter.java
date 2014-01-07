package fi.vm.sade.viestintapalvelu;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

/**
 * Filter to handle cross-origin resource sharing.
 */
public class CORSFilter implements javax.ws.rs.container.ContainerResponseFilter {
    private static final String ACAOHEADER = "Access-Control-Allow-Origin";
    private static final String ACRHHEADER = "Access-Control-Request-Headers";
    private static final String ACAHHEADER = "Access-Control-Allow-Headers";
    private static final String ACAMHEADER = "Access-Control-Allow-Methods";
    private static final String ACACHEADER = "Access-Control-Allow-Credentials";

    private final transient CORSConfiguration configuration = new CORSConfiguration();

    public CORSFilter() {
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        // TODO Tighten up security and configuration options
        // final String requestOrigin = request.getHeaderValue(ORIGINHEADER);
        response.getHeaders().add(ACAOHEADER, this.configuration.getOrigin());

        final String requestHeaders = request.getHeaderString(ACRHHEADER);
        response.getHeaders().add(ACAHHEADER, requestHeaders);

        response.getHeaders().add(ACAMHEADER, this.configuration.getAllowedMethods());

        response.getHeaders().add(ACACHEADER, this.configuration.allowCredentials());

        // return response;
    }
}