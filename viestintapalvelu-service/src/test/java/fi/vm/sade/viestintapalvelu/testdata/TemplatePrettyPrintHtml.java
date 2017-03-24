package fi.vm.sade.viestintapalvelu.testdata;



import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.apache.commons.lang3.StringEscapeUtils.*;


public class TemplatePrettyPrintHtml {


    public static void main(String[] args) {

        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><meta http-equiv=\"Content-Style-Type\" content=\"text/css\"/></head><body><p>Hej $etunimi</p> <p>Gratulerar, du har blivit beviljad en studieplats! Du kan se mer information och ta emot studieplatsen i <a href=\"https://studieinfo.fi/omatsivut/login\"> Min Studieinfo-tj&auml;nsten </a>. Du f&aring;r ytterligare anvisningar om mottagande av studieplatsen i <a href=\"https://studieinfo.fi/wp/stod-for-studievalet/gemensam-ansokan/gemensam-ansokan-till-hogskolor/sa-har-tar-du-emot-en-studieplats-i-gemensam-ansokan-till-hogskolor/\"> Studieinfo.fi-tj&auml;nsten</a> eller fr&aring;n h&ouml;gskolan som du har s&ouml;kt till.</p><p>Ans&ouml;kan: $haunNimi</p> <p>Ans&ouml;kningsm&aring;l:</p> #foreach( $hakukohde in $hakukohteet ) <p>$hakukohde.nimi  $hakukohde.tarjoaja</p> #end <p>Svara inte p&aring; detta meddelande - meddelandet har skickats automatiskt.</p> </body></html>";

        Tidy tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setIndentContent(true);
        tidy.setTidyMark(false);

        Document htmlDOM = tidy.parseDOM(new ByteArrayInputStream(
                unescapeEcmaScript(content).getBytes()), null);
        OutputStream out = new ByteArrayOutputStream();
        tidy.pprint(htmlDOM, out);


        System.out.println(unescapeHtml4(out.toString()));
    }
}
