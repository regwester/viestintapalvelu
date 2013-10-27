package fi.vm.sade.viestintapalvelu;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.AddressLabelBatch;
import fi.vm.sade.viestintapalvelu.hyvaksymiskirje.Hyvaksymiskirje;
import fi.vm.sade.viestintapalvelu.hyvaksymiskirje.HyvaksymiskirjeBatch;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.JalkiohjauskirjeBatch;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFText2HTML;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import java.io.*;
import java.util.*;

public class TestUtil {

    private final static String ADDRESS_LABEL_PDF_URL = "http://localhost:8080/api/v1/addresslabel/pdf";
    private final static String ADDRESS_LABEL_XLS_URL = "http://localhost:8080/api/v1/addresslabel/xls";
    private final static String JALKIOHJAUSKIRJE_URL = "http://localhost:8080/api/v1/jalkiohjauskirje/pdf";
    private final static String IPOST_URL = "http://localhost:8080/api/v1/jalkiohjauskirje/zip";
    private final static String HYVAKSYMISKIRJE_URL = "http://localhost:8080/api/v1/hyvaksymiskirje/pdf";

    public static List<List<String>> generateAddressLabelsPDF(
            List<AddressLabel> labels) throws Exception {
        AddressLabelBatch batch = new AddressLabelBatch(labels);
        return readPDF(get(batch, ADDRESS_LABEL_PDF_URL), -1, -1);
    }

    public static List<List<String>> generateAddressLabelsXLS(
            List<AddressLabel> labels) throws Exception {
        AddressLabelBatch batch = new AddressLabelBatch(labels);
        return readXLS(get(batch, ADDRESS_LABEL_XLS_URL));
    }

    public static List<List<String>> generateJalkiohjauskirje(
            Jalkiohjauskirje kirje) throws DocumentException, IOException {
        JalkiohjauskirjeBatch batch = new JalkiohjauskirjeBatch(
                Arrays.asList(kirje));
        return readPDF(get(batch, JALKIOHJAUSKIRJE_URL), 1, 2);
    }

    public static byte[] generateIPostZIP(List<Jalkiohjauskirje> kirjeet)
            throws Exception {
        JalkiohjauskirjeBatch batch = new JalkiohjauskirjeBatch(kirjeet);
        return get(batch, IPOST_URL);
    }

    public static List<List<String>> generateHyvaksymiskirje(
            Hyvaksymiskirje kirje) throws Exception {
        HyvaksymiskirjeBatch batch = new HyvaksymiskirjeBatch(
                Arrays.asList(kirje));
        return readPDF(get(batch, HYVAKSYMISKIRJE_URL), 1, 2);
    }

    public static String generateLiite(Jalkiohjauskirje kirje)
            throws Exception {
        JalkiohjauskirjeBatch batch = new JalkiohjauskirjeBatch(
                Arrays.asList(kirje));
        return readAsHtml(get(batch, JALKIOHJAUSKIRJE_URL), 2, 2);
    }


    private static byte[] get(Object json, String url)
            throws IOException, DocumentException {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.content-charset",
                "UTF-8");
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json;charset=utf-8");
        post.setEntity(new StringEntity(new ObjectMapper()
                .writeValueAsString(json), ContentType.APPLICATION_JSON));
        HttpResponse response = client.execute(post);
        String resultUrl = IOUtils.toString(response.getEntity().getContent(),
                "UTF-8");
        HttpGet get = new HttpGet(resultUrl);
        response = client.execute(get);
        return IOUtils.toByteArray(response.getEntity().getContent());
    }

    private static List<List<String>> readPDF(byte[] byteDocument,
                                              int startPage, int endPage) throws IOException {
        return parseHTML(readAsHtml(byteDocument, startPage, endPage).getBytes());
    }

    private static String readAsHtml(byte[] byteDocument,
                                     int startPage, int endPage) throws IOException {
        PDDocument document = PDDocument.load(new ByteArrayInputStream(
                byteDocument));
        PDFText2HTML stripper = new PDFText2HTML("UTF-8");
        StringWriter writer = new StringWriter();
        if (startPage > 0) {
            stripper.setStartPage(startPage);
        }
        if (endPage > 0) {
            stripper.setEndPage(endPage);
        }
        stripper.setLineSeparator("<br/>");
        stripper.writeText(document, writer);
        document.close();
        return writer.toString();
    }

    @SuppressWarnings("unchecked")
    private static List<List<String>> readXLS(byte[] byteDocument)
            throws Exception {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new ByteArrayInputStream(byteDocument));
        List<List<String>> labels = new ArrayList<List<String>>();
        List<Node> rows = xpath("//html40:tr").selectNodes(doc);
        for (Node row : rows) {
            List<String> rowContent = new ArrayList<String>();
            labels.add(rowContent);
            List<Node> columns = xpath("./html40:*").selectNodes(row);
            for (Node column : columns) {
                rowContent.add(column.getText());
            }
        }
        return labels;
    }

    private static XPath xpath(String selector) {
        Map<String, String> namespaceUris = new HashMap<String, String>();
        namespaceUris.put("html40", "http://www.w3.org/TR/REC-html40");
        XPath xPath = DocumentHelper.createXPath(selector);
        xPath.setNamespaceURIs(namespaceUris);
        return xPath;
    }

    private static Tidy newTidy() {
        Tidy tidy = new Tidy();
        tidy.setTidyMark(false);
        tidy.setDocType("omit");
        tidy.setXHTML(true);
        tidy.setCharEncoding(Configuration.UTF8);
        tidy.setQuiet(true);
        return tidy;
    }

    private static List<List<String>> parseHTML(byte[] document) {
        org.w3c.dom.Document doc = newTidy().parseDOM(new ByteArrayInputStream(document), new ByteArrayOutputStream());
        List<List<String>> nodes = new ArrayList<List<String>>();
        NodeList p = doc.getElementsByTagName("p");
        int i = 0;
        while (i < p.getLength()) {
            NodeList textNodes = p.item(i).getChildNodes();
            int j = 0;
            List<String> texts = new ArrayList<String>();
            while (j < textNodes.getLength()) {
                if (textNodes.item(j).getNodeType() == Node.TEXT_NODE) {
                    texts.add(textNodes.item(j).getNodeValue());
                }
                j++;
            }
            nodes.add(texts);
            i++;
        }
        return nodes;
    }
}
