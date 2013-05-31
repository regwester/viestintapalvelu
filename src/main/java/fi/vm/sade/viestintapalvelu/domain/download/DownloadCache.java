package fi.vm.sade.viestintapalvelu.domain.download;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Singleton;

@Singleton
public class DownloadCache {
	private Cache<String, Download> downloads = CacheBuilder.newBuilder()
			.expireAfterWrite(10, TimeUnit.SECONDS).build();

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
