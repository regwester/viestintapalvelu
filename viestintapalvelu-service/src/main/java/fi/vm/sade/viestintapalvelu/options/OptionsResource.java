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

package fi.vm.sade.viestintapalvelu.options;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuDetailsDto;
import fi.vm.sade.viestintapalvelu.externalinterface.component.TarjontaComponent;

import static org.joda.time.DateTime.now;

/**
 * User: ratamaa
 * Date: 7.10.2014
 * Time: 14:25
 */
@Component
@PreAuthorize("isAuthenticated()")
@Path(Urls.OPTIONS_PATH)
public class OptionsResource {
    protected enum CacheType {
        hakus
    }

    @Autowired
    private TarjontaComponent tarjontaComponent;

    @Value("${viestintapalvelu.options.cache.valid.millis}")
    private long optionsCacheValidMillis = 3600*1000l;

    protected Map<CacheType, CacheEntry<?>> cache = new HashMap<CacheType, CacheEntry<?>>();

    @GET
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_TEMPLATE)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/hakus")
    public List<HakuDetailsDto> listHakus() {
        return cached(CacheType.hakus, new Cacheable<List<HakuDetailsDto>>() {
            public List<HakuDetailsDto> resolve() {
                return tarjontaComponent.findPublisehedHakus();
            }
        });
    }

    protected<T> T cached(CacheType type, Cacheable<T> resolver) {
        CacheEntry<T> entry = (CacheEntry<T>) cache.get(type);
        if (entry != null && entry.getCreatedAt().plus(optionsCacheValidMillis).isAfter(now())) {
            return entry.getData();
        } else {
            entry = new CacheEntry<T>(resolver.resolve());
            cache.put(type,entry);
            return entry.getData();
        }
    }

    protected interface Cacheable<T> {
        T resolve();
    }

    protected class CacheEntry<T> {
        private DateTime createdAt = now();
        private T data;

        public CacheEntry(T data) {
            this.data = data;
        }

        public DateTime getCreatedAt() {
            return createdAt;
        }

        public T getData() {
            return data;
        }
    }

}
