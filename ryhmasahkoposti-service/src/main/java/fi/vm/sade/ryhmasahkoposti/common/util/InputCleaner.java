package fi.vm.sade.ryhmasahkoposti.common.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class InputCleaner {

    public static String cleanHtmlFragment(String html) {
        Document doc = cleanHtml(html);
        return doc.body().html().toString().replaceAll("\\r\\n|\\n|\\r", "");
    }

    public static String cleanHtmlDocument(String html) {
        Document doc = cleanHtml(html);
        return doc.toString().replaceAll("\\r\\n|\\n|\\r", "");
    }

    /* Removes dangerous elements and all attributes except style */
    public static Document cleanHtml(String html) {

        Cleaner cleaner = new Cleaner(getWhitelist());
        Document dirtyDoc = Jsoup.parseBodyFragment(html);
        Document almostCleanDoc = cleaner.clean(dirtyDoc);

        // check that style attributes don't include any url() or expression()
        // if they do, remove them
        for(Element el: almostCleanDoc.select("[style]")) {
            String attr = el.attr("style");
            if(attr.contains("url") || attr.contains("expression")) {
                el.removeAttr("style");
            }
        }

        return almostCleanDoc;
    }

    /*
    * Allows the following elements:
    * a, b, blockquote, br, caption, cite, code, col, colgroup, dd, dl, dt, em, h1, h2, h3, h4, h5, h6, span,
    * i, img, li, ol, p, pre, q, small, strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul
    * and all style attributes
    */
    public static Whitelist getWhitelist() {
        Whitelist wl = Whitelist.relaxed();
        wl.addTags("span");
        wl.addAttributes(":all", "style");
        return wl;
    }
}
