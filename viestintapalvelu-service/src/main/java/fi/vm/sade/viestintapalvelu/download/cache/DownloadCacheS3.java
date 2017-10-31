package fi.vm.sade.viestintapalvelu.download.cache;

import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Profile("aws")
class DownloadCacheS3 implements DownloadCache {

    private final OphS3Client ophS3Client;

    private static final Logger log = LoggerFactory.getLogger(DownloadCacheS3.class);

    @Autowired
    public DownloadCacheS3(OphS3Client ophS3Client) {
        this.ophS3Client = ophS3Client;
    }

    @Override
    public Collection<Header> getListOfAvailableDocuments() {
        CompletableFuture<List<Header>> listCompletableFuture = ophS3Client.listObjects();
        try {
            return listCompletableFuture.get();
        } catch (InterruptedException e) {
            log.error("Interrupted while getting list of documents", e);
        } catch (ExecutionException e) {
            log.error("Error getting list of documents", e);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public DocumentId addDocument(Download download) {
        OphS3Client.AddObjectResponse<PutObjectResponse> res = ophS3Client.addFileObject(download);
        return res.getId();
    }

    @Override
    public DocumentId addDocument(Download download, DocumentId documentId) {
        OphS3Client.AddObjectResponse<PutObjectResponse> res = ophS3Client.addFileObject(download, documentId);
        return res.getId();
    }

    @Override
    public Download get(DocumentId documentId) {
        return ophS3Client.getObject(documentId, true);
    }
}
