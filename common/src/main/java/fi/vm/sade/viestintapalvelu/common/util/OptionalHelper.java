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

package fi.vm.sade.viestintapalvelu.common.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import com.google.common.base.Supplier;
import com.google.common.base.Optional;

/**
 * User: ratamaa
 * Date: 2.10.2014
 * Time: 15:32
 */
public class OptionalHelper {
    private OptionalHelper() {
    }

    public static<T> Supplier<T> notFound(final String message) throws NotFoundException {
        return new Supplier<T>() {
            public T get() {
                Map<String,Object> result = new HashMap<>();
                result.put("status", Response.Status.NOT_FOUND.getStatusCode());
                result.put("description", message);
                result.put("errors", Collections.singletonList(message));
                Response response = Response.status(Response.Status.NOT_FOUND)
                        .entity(result).build();
                throw new NotFoundException(message, response);
            }
        };
    }

    public static<T> Supplier<T> illegalState(final String message) throws NotFoundException {
        return new Supplier<T>() {
            public T get() {
                throw new IllegalStateException(message);
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

    public static<T> Optional<T> as(Optional<? super T> from, Class<T> type) {
        if (!from.isPresent()) {
            return Optional.absent();
        }
        Object any = from.get();
        if (type.isAssignableFrom(any.getClass())) {
            return Optional.of((T) any);
        }
        return Optional.absent();
    }

}
