package fi.vm.sade.ryhmasahkoposti.resource;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GenericResourceImpl {

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
