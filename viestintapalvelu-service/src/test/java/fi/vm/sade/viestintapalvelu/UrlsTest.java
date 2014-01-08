package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UrlsTest {
    @Test
    public void addresslabelDownload() {
        assertEquals("http://localhost:" + Launcher.DEFAULT_PORT + "/api/v1/addresslabel/download",
                Urls.localhost(Launcher.DEFAULT_PORT).addresslabelDownload());
    }
}
