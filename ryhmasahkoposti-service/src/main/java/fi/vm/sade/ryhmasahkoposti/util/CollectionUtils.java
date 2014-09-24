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

import java.util.*;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import fi.vm.sade.generic.model.BaseEntity;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 10:12
 */
public final class CollectionUtils {
    private CollectionUtils() {
    }

    public static<T extends BaseEntity> List<Long> extractIds(Collection<T> entities) {
        return new ArrayList<Long>(Collections2.transform(entities, ENTITY_ID));
    }

    public static<By, T> Map<By, T> map(Collection<T> entries, Function<? super T,By> function) {
        Map<By, T> map = new HashMap<By, T>();
        for (T entry : entries) {
            map.put(function.apply(entry), entry);
        }
        return map;
    }

    public static final Function<BaseEntity, Long> ENTITY_ID = new Function<BaseEntity, Long>() {
        @Nullable
        @Override
        public Long apply(@Nullable BaseEntity baseEntity) {
            if (baseEntity == null) {
                return null;
            }
            return baseEntity.getId();
        }
    };

}
