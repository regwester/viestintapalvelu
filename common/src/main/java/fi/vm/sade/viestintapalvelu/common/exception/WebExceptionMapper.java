/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.viestintapalvelu.common.exception;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.spi.ExtendedExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@Provider
/* Centralized error handling for jersey resources */
public class WebExceptionMapper implements ExtendedExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(WebExceptionMapper.class);

    private static final ResourceBundle resource = ResourceBundle.getBundle("properties.ErrorMessages");

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
