package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UrlsTest {
	@Test
	public void test() {
		assertEquals("http://localhost:8080/api/v1/addresslabel/download", Urls
				.localhost().addresslabelDownload());
	}
}
