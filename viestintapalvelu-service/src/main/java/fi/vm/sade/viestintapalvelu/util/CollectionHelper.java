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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import fi.vm.sade.generic.model.BaseEntity;

/**
 * User: ratamaa
 * Date: 3.10.2014
 * Time: 15:13
 */
public class CollectionHelper {
    private CollectionHelper() {
    }

    /**
     * @param c collection to split
     * @param chunkSize the maximum number of elements in each chunk
     * @param <T> contained in the collection
     * @param <C> the collection type
     * @return a list of chunks
     */
    public static<T,C extends Collection<? extends T>> List<List<T>> split(C c, int chunkSize) {
        List<List<T>> ready = new ArrayList<List<T>>();
        List<T> current = new ArrayList<T>();
        if (c != null) {
            for (T e : c) {
                if (current.size() >= chunkSize) {
                    ready.add(current);
                    current = new ArrayList<T>();
                }
                current.add(e);
            }
        }
        ready.add(current);
        return ready;
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

    public static<T extends BaseEntity> List<Long> extractIds(Collection<T> entities) {
        return new ArrayList<Long>(Collections2.transform(entities, ENTITY_ID));
    }
}
