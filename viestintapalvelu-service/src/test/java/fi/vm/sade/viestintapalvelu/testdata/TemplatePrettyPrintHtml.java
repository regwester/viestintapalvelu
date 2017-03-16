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

        String content = "<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><!DOCTYPE html PUBLIC \\\"-//W3C//DTD XHTML 1.0 Strict//EN\\\" \\\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\\\"><html xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><head><meta http-equiv=\\\"Content-Type\\\" content=\\\"text/html; charset=utf-8\\\"/><meta http-equiv=\\\"Content-Style-Type\\\" content=\\\"text/css\\\"/></head><body><p>Hei $etunimi</p> <p>Olet hakenut opiskelupaikkaa Opintopolku-verkkopalvelussa. Sinulle on nyt my&ouml;nnetty opiskelupaikka! K&auml;y hyv&auml;ksym&auml;ss&auml; opiskelupaikka Opintopolussa viimeist&auml;&auml;n $deadline</p> <p><a href=\\\"https://opintopolku.fi/omatsivut/login\\\">Kirjaudu opintopolkuun</a></p> </body></html>";

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
