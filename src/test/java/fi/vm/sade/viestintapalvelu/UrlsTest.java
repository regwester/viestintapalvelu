package fi.vm.sade.viestintapalvelu;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UrlsTest {
    @Test
    public void addresslabelDownload() {
        assertEquals("http://localhost:8080/api/v1/addresslabel/download", Urls
                .localhost().addresslabelDownload());
    }
}
