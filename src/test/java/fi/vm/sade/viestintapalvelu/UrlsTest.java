package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fi.vm.sade.viestintapalvelu.application.Urls;

public class UrlsTest {
	@Test
	public void addresslabelDownload() {
		assertEquals("http://localhost:8080/api/v1/addresslabel/download", Urls
				.localhost().addresslabelDownload());
	}
}
