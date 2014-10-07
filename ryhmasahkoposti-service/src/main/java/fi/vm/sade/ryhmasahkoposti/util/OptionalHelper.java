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

package fi.vm.sade.ryhmasahkoposti.util;

import com.google.common.base.Optional;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 16:59
 */
public final class OptionalHelper {
    private OptionalHelper() {
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
