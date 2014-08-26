package fi.vm.sade.ryhmasahkoposti.exception;

import org.glassfish.jersey.spi.ExtendedExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
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

    private static final Logger logger = LoggerFactory.getLogger(WebExceptionMapper.class);

    private static final ResourceBundle resource = ResourceBundle.getBundle("properties.messages");

    @Override
    public boolean isMappable(Throwable throwable) {
        //Let Jersey handle these
        return !(throwable instanceof WebApplicationException);
    }

    @Override
    public Response toResponse(Throwable throwable) {
        /* Spring security related exceptions */
        if(throwable instanceof AccessDeniedException) {
            logger.error("AccessDeniedException: {}", throwable);
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN)
                .entity(resource.getString("error.msg.forbidden")).language(getUserLocale()).build();
        }
        else if (throwable instanceof AuthenticationException) {
            logger.error("AuthenticationException: {}", throwable);
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN)
                .entity(resource.getString("error.msg.unauthorized")).language(getUserLocale()).build();
        }
        /* Custom exceptions*/
        else if(throwable instanceof ExternalInterfaceException) {
            logger.error("Problems in getting data from external interfaces: {}", throwable);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(resource.getString(throwable.getMessage())).language(getUserLocale()).build();
        }
        else if(throwable instanceof PersistenceException){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(resource.getString(throwable.getMessage())).language(getUserLocale()).build();
        }
        /* Unhandled exceptions */
        else {
            logger.error("Encountered an exception: {}", throwable);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN)
                    .entity(resource.getString("error.msg.internal")).language(getUserLocale()).build();
        }
    }

    //TODO: get the actual user language from accept-language header
    private Locale getUserLocale() {
        return new Locale("fi", "FI");
    }
}
