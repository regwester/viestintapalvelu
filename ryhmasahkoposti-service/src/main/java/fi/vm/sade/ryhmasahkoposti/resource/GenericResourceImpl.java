package fi.vm.sade.ryhmasahkoposti.resource;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GenericResourceImpl {

    protected void throwError404(String msg) {
        throwError(Response.Status.NOT_FOUND, msg);
    }

    protected void throwError400(String msg) {
        throwError(Response.Status.BAD_REQUEST, msg);
    }

    protected void throwError500(String msg) {
        throwError(Response.Status.INTERNAL_SERVER_ERROR, msg);
    }

    protected void throwError(Response.Status status, String msg) {
        throw new WebApplicationException(Response.status(status)
                .type(MediaType.TEXT_PLAIN).entity(msg).build());
    }

    /**
     * Fetches user oid from SecurityContext
     *
     * @return oid of currently authenticated user
     * @throws NullPointerException
     *             if user oid is not available from security context
     */
    protected String getCurrentUserOid() throws NullPointerException {
        String oid = SecurityContextHolder.getContext().getAuthentication().getName();
        if (oid == null) {
            throw new NullPointerException("No user name available from SecurityContext!");
        }
        return oid;
    }
}
