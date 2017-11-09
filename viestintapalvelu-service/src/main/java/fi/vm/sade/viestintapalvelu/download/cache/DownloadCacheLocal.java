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
package fi.vm.sade.viestintapalvelu.download.cache;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import cucumber.api.java.eo.Se;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.Header;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Collections2;


class DownloadCacheLocal implements DownloadCache {
    private Cache<DocumentId, Download> downloads = CacheBuilder.newBuilder().expireAfterWrite(DURATION.getSeconds(), TimeUnit.SECONDS).build();

    @Override
    @SuppressWarnings("deprecation")
    public Collection<Header> getListOfAvailableDocuments() {
        return Collections2.transform(downloads.asMap().entrySet(), o ->
                new Header(o.getValue().getContentType(), o.getValue().getFilename(), o.getKey().getDocumentId(), o.getValue().toByteArray().length, o.getValue()
                .getTimestamp()));
    }

    @Override
    public DocumentId addDocument(Download download) {
        String documentIdStr = UUID.randomUUID().toString();
        DocumentId documentId = new DocumentId(documentIdStr);
        downloads.put(documentId, download);
        return documentId;
    }

    @Override
    public DocumentId addDocument(Download download, DocumentId documentId) {
        downloads.put(documentId, download);
        return documentId;
    }

    @Override
    public Download get(DocumentId documentId) {
        Download download = downloads.getIfPresent(documentId);
        if (download != null) {
            downloads.invalidate(download);
        }
        return download;
    }
}
