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

package fi.vm.sade.viestintapalvelu.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import com.google.common.base.Supplier;

/**
 * User: ratamaa
 * Date: 2.10.2014
 * Time: 15:32
 */
public class OptionalHelpper {

    public static<T> Supplier<T> notFound(final String message) throws NotFoundException {
        return new Supplier<T>() {
            public T get() {
                Map<String,Object> result = new HashMap<String, Object>();
                result.put("status", Response.Status.NOT_FOUND.getStatusCode());
                result.put("description", message);
                result.put("errors", Arrays.asList(message));
                Response response = Response.status(Response.Status.NOT_FOUND)
                        .entity(result).build();
                throw new NotFoundException(message, response);
            }
        };
    }

    public static<T, E extends RuntimeException> Supplier<T> doThrow(final E e) throws E {
        return new Supplier<T>() {
            @Override
            public T get() {
                throw e;
            }
        };
    }

}
