package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.AddressLabelBatch;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.JalkiohjauskirjeBatch;

public class TestUtil {

	private final static String ADDRESS_LABEL_PDF_URL = "http://localhost:8080/api/v1/addresslabel/pdf";
	private final static String JALKIOHJAUSKIRJE_URL = "http://localhost:8080/api/v1/jalkiohjauskirje/pdf";
	private final static String ADDRESS_LABEL_PDF_TEMPLATE = "/osoitetarrat.html";
	private final static String JALKIOHJAUSKIRJE_TEMPLATE = "/jalkiohjauskirje.html";
	private final static String LIITE_TEMPLATE = "/liite.html";
	private final static String HAKUTULOSTAULUKKO_TEMPLATE = "/hakutulostaulukko_test.html";
	
	public static List<List<String>> generateAddressLabels(List<AddressLabel> labels) throws Exception {
		AddressLabelBatch batch = new AddressLabelBatch(ADDRESS_LABEL_PDF_TEMPLATE, labels);
		return generatePDF(batch, ADDRESS_LABEL_PDF_URL, -1, -1);
	}
	
	public static List<List<String>> generateJalkiohjauskirje(Jalkiohjauskirje kirje) throws Exception {
		JalkiohjauskirjeBatch batch = new JalkiohjauskirjeBatch(JALKIOHJAUSKIRJE_TEMPLATE, LIITE_TEMPLATE, Arrays.asList(kirje));
		return generatePDF(batch, JALKIOHJAUSKIRJE_URL, 1, 2);
	}
	
	public static List<List<String>> generateHakutulostaulukko(Jalkiohjauskirje kirje) throws Exception {
		JalkiohjauskirjeBatch batch = new JalkiohjauskirjeBatch(JALKIOHJAUSKIRJE_TEMPLATE, HAKUTULOSTAULUKKO_TEMPLATE, Arrays.asList(kirje));
		return generatePDF(batch, JALKIOHJAUSKIRJE_URL, 2, 2);
	}
	
	private static List<List<String>> generatePDF(Object json, String url, int startPage, int endPage)
			throws UnsupportedEncodingException, IOException,
			JsonGenerationException, JsonMappingException,
			ClientProtocolException, DocumentException {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json;charset=utf-8");
		post.setEntity(new StringEntity(new ObjectMapper()
				.writeValueAsString(json), ContentType.APPLICATION_JSON));
		HttpResponse response = client.execute(post);
		String documentId = readDocumentId(response);
		HttpGet get = new HttpGet(
				"http://localhost:8080/api/v1/download/document/"
						+ documentId);
		response = client.execute(get);
		return readPDF(response, startPage, endPage);
	}

	private static List<List<String>> readPDF(HttpResponse response, int startPage, int endPage)
			throws IOException, DocumentException {
		PDDocument document = PDDocument
				.load(response.getEntity().getContent());
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
		return parseHTML(new String(toXhtml(writer.toString().getBytes())));
	}

	private static String readDocumentId(HttpResponse response)
			throws IOException, DocumentException {
		return IOUtils.toString(response.getEntity().getContent(), "UTF-8");
	}

	private static byte[] toXhtml(byte[] document) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		newTidy().parseDOM(new ByteArrayInputStream(document), out);
		return out.toByteArray();
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

	private static List<List<String>> parseHTML(String xml)
			throws DocumentException {
		SAXReader reader = new SAXReader();
		List<List<String>> nodes = new ArrayList<List<String>>();
		Document document = reader.read(new StringReader(xml));
		for (Object object : document.selectNodes("//div/p")) {
			Node node = (Node) object;
			nodes.add(Arrays.asList(node.getText().split("\n")));
		}
		return nodes;
	}
}
