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

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.Version;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.base.Optional;

import fi.ratamaa.dtoconverter.reflection.Property;
import fi.vm.sade.generic.dao.JpaDAO;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 17:02
 */
public class DaoVault<T> {
    private Long maxId = 0l;
    private CatchSingleParameterAnswer<T,T> catcher
            = new CatchSingleParameterAnswer<T,T>(new Answer<T>() {
        @Override
        public T answer(InvocationOnMock invocation) throws Throwable {
            return firstArgument(invocation);
        }
    }) {
        @Override
        protected T handleArgument(T argument) {
            Property id = idProp(argument),
                    version = versionProp(argument);
            if (id != null) {
                Long idValue = ++maxId;
                id.set(argument, idValue);
                store.put(idValue, argument);
            }
            if (version != null) {
                version.set(argument, 0l);
            }
            return argument;
        }
    };

    @SuppressWarnings("unchecked")
    private T firstArgument(InvocationOnMock invocation) {
        return (T) invocation.getArguments()[0];
    }
    private Property versionProp(T argument) {
        return longProp(argument, "version", Version.class);
    }
    private Property idProp(T argument) {
        return longProp(argument, "id", Id.class);
    }
    private Property longProp(T argument, String name, Class<? extends Annotation> requiredAnnotation) {
        if (argument == null) {
            return null;
        }
        Property p = Property.findForName(argument.getClass(), name);
        if (p != null && p.isAnnotationPresent(requiredAnnotation)
                && Long.class.isAssignableFrom(p.getType())) {
            return p;
        }
        return null;
    }

    private Map<Long,T> store = new HashMap<Long, T>();

    public DaoVault() {
    }

    public DaoVault(JpaDAO<T,Long> dao, Class<T> tClzz) {
        when(dao.insert(any(tClzz))).then(store());
        when(dao.read(any(Long.class))).then(readById());
        doAnswer(store()).when(dao).update(any(tClzz));
    }

    public Answer<T> store() {
        return catcher;
    }

    public Answer<T> update() {
        return new Answer<T>() {
            @Override
            public T answer(InvocationOnMock invocation) throws Throwable {
                T model = firstArgument(invocation);
                Property id = idProp(model),
                        version = versionProp(model);
                if (id != null) {
                    store.put((Long)id.get(model), model);
                }
                if (version != null) {
                    Long current = (Long) version.get(model);
                    if (current == null) {
                        current = -1l;
                    }
                    version.set(model, current+1);
                }
                return null;
            }
        };
    }

    public T get(Long id) {
        return this.store.get(id);
    }

    public Answer<T> readById() {
        return new Answer<T>() {
            @Override
            public T answer(InvocationOnMock invocation) throws Throwable {
                return store.get((Long) invocation.getArguments()[0]);
            }
        };
    }

    public Answer<Optional<T>> readFirstOptional() {
        return new Answer<Optional<T>>() {
            @Override
            public Optional<T> answer(InvocationOnMock invocation) throws Throwable {
                List<T> values = catcher.getArguments();
                if (values.isEmpty()) {
                    return Optional.absent();
                }
                return Optional.of(values.get(0));
            }
        };
    }

    public Answer<T> readFirst() {
        return new Answer<T>() {
            @Override
            public T answer(InvocationOnMock invocation) throws Throwable {
                List<T> values = catcher.getArguments();
                if (values.isEmpty()) {
                    return null;
                }
                return values.get(0);
            }
        };
    }
}
