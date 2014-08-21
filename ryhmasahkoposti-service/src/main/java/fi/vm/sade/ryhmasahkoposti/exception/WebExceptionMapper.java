package fi.vm.sade.ryhmasahkoposti.exception;

import org.glassfish.jersey.spi.ExtendedExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Locale;
import java.util.ResourceBundle;

@Service
@Provider
/* Centralized error handling for jersey resources */
public class WebExceptionMapper implements ExtendedExceptionMapper<Throwable> {

    private static final Logger log = LoggerFactory.getLogger(WebExceptionMapper.class);

    private static final ResourceBundle resource = ResourceBundle.getBundle("properties.messages");

    @Override
    public boolean isMappable(Throwable throwable) {
        //Let Jersey handle these
        return !(throwable instanceof WebApplicationException);
    }

    @Override
    public Response toResponse(Throwable throwable) {
        if(throwable instanceof AccessDeniedException) {
            log.error("Encountered an exception: ", throwable);
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN)
                .entity(resource.getString("error.unauthorized")).language(getUserLocale()).build();
        }
        else if(throwable instanceof ExternalInterfaceException) {
            log.error("Problems in getting data from external interfaces:\n ", throwable);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(resource.getString("error.internal")).language(getUserLocale()).build();
        }
        //rest of the handled exceptions, throw them in the service layer and catch them here
        else {
            log.error("Encountered an exception: ", throwable);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN)
                    .entity(resource.getString("error.internal")).language(getUserLocale()).build();
        }
    }

    //TODO: get the actual user language from somewhere
    private Locale getUserLocale() {
        return new Locale("fi", "FI");
    }
}
