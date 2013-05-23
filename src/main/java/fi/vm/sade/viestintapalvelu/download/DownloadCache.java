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

	public String addDocument(Download download) {
		String documentId = UUID.randomUUID().toString();
		downloads.put(documentId, download);
		return documentId;
	}

	public String addDocument(Download download, String documentId) {
		System.out.println("DownloadCache.addDocument: '" + documentId + "'");
		downloads.put(documentId, download);
		return documentId;
	}

	public Download get(String documentId) {
		System.out.println("DownloadCache.get: '" + documentId + "'");
		Download download = downloads.getIfPresent(documentId);
		if (download != null) {
			downloads.invalidate(download);
		}
		return download;
	}

	public Download getAndWait(String documentId) {
		System.out.println("getAndWait: '" + documentId + "'");
		int ticks = 0;
		Download download = get(documentId);
		while (download == null && ticks < 20) {
			try {
				Thread.sleep(1000);
				ticks++;
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			download = get(documentId);
		}
		return download;
	}
}
