package fi.vm.sade.viestintapalvelu.download;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.FastDateFormat;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Collections2;
import com.google.inject.Singleton;

@Singleton
public class DownloadCache {
    private Cache<String, Download> downloads = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES)
            .build();
    private FastDateFormat dateFormat = FastDateFormat.getInstance("dd.MM.yyyy HH.mm");

    public Collection<Header> getListOfAvailableDocuments() {
        return Collections2.transform(downloads.asMap().entrySet(), new Function<Entry<String, Download>, Header>() {
            public Header apply(Entry<String, Download> o) {
                return new Header(o.getValue().getContentType(), o.getValue().getFilename(), o.getKey(), o.getValue()
                        .toByteArray().length, o.getValue().getTimestamp());
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
