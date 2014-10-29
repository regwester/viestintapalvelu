package fi.vm.sade.ryhmasahkoposti.util;

import fi.vm.sade.ryhmasahkoposti.common.util.InputCleaner;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class InputCleanerTest {

    /*
    @Test
    public void parseHtmlDocumentTest() {
        String h = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Testi</title></head><body><h1>Otsikko</h1><p>Leipätekstiä</p></body></html>";
        String cleanHtml = InputCleaner.cleanHtmlDocument(h);
        assertThat(cleanHtml).isEqualTo("<html><head></head><body><h1>Otsikko</h1><p>Lei&auml;teksti&auml;</p></body></html>");
    }
    */

    @Test
    public void parseHtmlFragment() {
        String h = "<h1>Otsikko</h1><p style=\"color:red\">Leipätekstiä</p>";
        String cleanHtml = InputCleaner.cleanHtmlFragment(h);
        assertThat(cleanHtml).isEqualTo("<h1>Otsikko</h1><p style=\"color:red\">Leip&auml;teksti&auml;</p>");
    }

}
