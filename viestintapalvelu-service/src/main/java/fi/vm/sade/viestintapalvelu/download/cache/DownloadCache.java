package fi.vm.sade.viestintapalvelu.download.cache;

import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.Header;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public interface DownloadCache {
    Duration DURATION = Duration.ofHours(24L);

    @SuppressWarnings("deprecation")
    Collection<Header> getListOfAvailableDocuments();

    DocumentId addDocument(Download download);

    DocumentId addDocument(Download download, DocumentId documentId);

    Download get(DocumentId documentId);
}
