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

    public static<By, T> Map<By, T> map(Collection<T> entries, Function<? super T,By> function) {
        Map<By, T> map = new HashMap<By, T>();
        for (T entry : entries) {
            map.put(function.apply(entry), entry);
        }
        return map;
    }

    public static<K,V> boolean addToMappedList(Map<K, List<V>> map, K key, V value) {
        List<V> values = map.get(key);
        boolean newKey = values == null;
        if (newKey) {
            values = new ArrayList<V>();
            map.put(key, values);
        }
        values.add(value);
        return newKey;
    }

    public static<CommonKey,KeyType,ValueType> Map<KeyType,ValueType> combine(Map<CommonKey,KeyType> keys,
                                                                              Map<CommonKey,ValueType> values) {
        Map<KeyType,ValueType> map = new HashMap<KeyType, ValueType>();
        for (Map.Entry<CommonKey,ValueType> value : values.entrySet()) {
            KeyType key = keys.get(value.getKey());
            if (key != null) {
                map.put(key, value.getValue());
            }
        }
        return map;
    }
}
