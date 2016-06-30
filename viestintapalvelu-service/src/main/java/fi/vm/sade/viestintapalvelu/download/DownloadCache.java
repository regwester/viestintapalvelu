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
package fi.vm.sade.viestintapalvelu.download;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Collections2;

@Service
@Singleton
public class DownloadCache {
    public static final int DURATION = 24;
    private Cache<String, Download> downloads = CacheBuilder.newBuilder().expireAfterWrite(DURATION, TimeUnit.HOURS).build();

    @SuppressWarnings("deprecation")
    public Collection<Header> getListOfAvailableDocuments() {
        return Collections2.transform(downloads.asMap().entrySet(), new Function<Entry<String, Download>, Header>() {
            public Header apply(Entry<String, Download> o) {
                return new Header(o.getValue().getContentType(), o.getValue().getFilename(), o.getKey(), o.getValue().toByteArray().length, o.getValue()
                        .getTimestamp());
            }
        });
    }

    public String addDocument(Download download) {
        String documentId = UUID.randomUUID().toString();
        downloads.put(documentId, download);
        return documentId;
    }

    public String addDocument(Download download, String documentId) {
        downloads.put(documentId, download);
        return documentId;
    }

    public Download get(String documentId) {
        Download download = downloads.getIfPresent(documentId);
        if (download != null) {
            downloads.invalidate(download);
        }
        return download;
    }
}
