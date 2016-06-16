/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.viestintapalvelu.options;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuDetailsDto;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuRDTO;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakukohdeDTO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.TarjontaComponent;
import fi.vm.sade.viestintapalvelu.recovery.Recoverer;
import fi.vm.sade.viestintapalvelu.recovery.RecovererPriority;
import static org.joda.time.DateTime.now;

/**
 * User: ratamaa
 * Date: 7.10.2014
 * Time: 14:25
 */
@Component("OptionsResource")
@Path(Urls.OPTIONS_PATH)
@Api(value=Urls.OPTIONS_PATH, description = "Käyttöliittymässä käytettävät valinnat")
@RecovererPriority(10)
public class OptionsResource implements Recoverer {
    private static final Logger logger = LoggerFactory.getLogger(OptionsResource.class);

    protected enum CacheType {
        hakus
    }

    @Autowired
    private TarjontaComponent tarjontaComponent;

    @Value("#{optionsCacheConfig['timeoutMillis'] != null ? optionsCacheConfig['timeoutMillis'] : 86400000}")
    private long optionsCacheValidMillis = 24*3600* 1000L;

    @Override
    public Runnable getTask() {
        return new Runnable() {
            @Override
            public void run() {
                listHakus(false);
            }
        };
    }

    protected Map<CacheType, CacheEntry<?>> cache = new HashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/hakus")
    @ApiOperation(value="Palauttaa julkaistut haut",
            responseContainer = "List", response = HakuDetailsDto.class)
    public List<HakuDetailsDto> listHakus(
            @ApiParam("Pakota päivitys (vapaaehtoinen, ilman tätä vastaus voidaan ladata kätköstä)")
            @QueryParam("forceRefresh") Boolean forceRefresh) {
        return cached(CacheType.hakus, new Cacheable<List<HakuDetailsDto>>() {
            public List<HakuDetailsDto> resolve() {
                return tarjontaComponent.findPublished(null);
            }
        }, Optional.fromNullable(forceRefresh).or(false));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/hakukohde/{oid}")
    public Response getHakuKohdeByOid(@PathParam("oid") String oid) {
        HakuRDTO<HakukohdeDTO> hakuKohde = tarjontaComponent.getHakuKohdeByOid(oid);
        return Response.status(Status.OK).entity(hakuKohde).build();
    }

    protected<T> T cached(CacheType type, Cacheable<T> resolver, boolean forceRefresh) {
        CacheEntry<T> entry = (CacheEntry<T>) cache.get(type);
        if (entry != null && entry.getCreatedAt().plus(optionsCacheValidMillis).isAfter(now())
                && !forceRefresh) {
            return entry.getData();
        } else {
            logger.info("Refreshing OptionsCache={}", type);
            entry = new CacheEntry<>(resolver.resolve());
            cache.put(type,entry);
            logger.info("Refreshed OptionsCache={}", type);
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
