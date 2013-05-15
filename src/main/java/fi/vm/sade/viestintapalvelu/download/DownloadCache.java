package fi.vm.sade.viestintapalvelu.download;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Singleton;

@Singleton
public class DownloadCache {
	private Cache<String, Download> downloads = CacheBuilder.newBuilder()
			.expireAfterWrite(10, TimeUnit.SECONDS).build();

	public String addDocument(String sessionId, Download download) {
		String documentId = UUID.randomUUID().toString();
		downloads.put(sessionId + documentId, download);
		return documentId;
	}

	public Download get(String sessionId, String documentId) {
		Download download = downloads.getIfPresent(sessionId + documentId);
		downloads.invalidate(download);
		return download;
	}
}
