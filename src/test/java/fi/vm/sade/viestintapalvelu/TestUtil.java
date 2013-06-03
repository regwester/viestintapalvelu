package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fi.vm.sade.viestintapalvelu.application.ViestintapalveluObjectMapper;
import fi.vm.sade.viestintapalvelu.domain.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.domain.address.AddressLabelBatch;
import fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje.Hyvaksymiskirje;
import fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje.HyvaksymiskirjeBatch;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.JalkiohjauskirjeBatch;
import fi.vm.sade.viestintapalvelu.test.Localhost;
import fi.vm.sade.viestintapalvelu.test.stub.AddressLabelBatchStub;
import fi.vm.sade.viestintapalvelu.test.stub.HyvaksymiskirjeBatchStub;
import fi.vm.sade.viestintapalvelu.test.stub.JalkiohjauskirjeBatchStub;

public class TestUtil {
	private static final Localhost LOCALHOST = new Localhost();

	public static List<List<String>> generateAddressLabelsPDF(
			final List<AddressLabel> labels) throws Exception {
		AddressLabelBatch batch = new AddressLabelBatchStub(labels);
		return readPDF(get(batch, LOCALHOST.addresslabelPdf()), -1, -1);
	}

	public static List<List<String>> generateAddressLabelsXLS(
			final List<AddressLabel> labels) throws Exception {
		AddressLabelBatch batch = new AddressLabelBatchStub(labels);
		return readXLS(get(batch, LOCALHOST.addresslabelXls()));
	}

	public static List<List<String>> generateJalkiohjauskirje(
			Jalkiohjauskirje kirje) throws Exception {
		JalkiohjauskirjeBatch batch = new JalkiohjauskirjeBatchStub(
				Arrays.asList(kirje));
		return readPDF(get(batch, LOCALHOST.jalkiohjauskirjePdf()), 1, 2);
	}

	public static byte[] generateIPostZIP(List<Jalkiohjauskirje> kirjeet)
			throws Exception {
		JalkiohjauskirjeBatch batch = new JalkiohjauskirjeBatchStub(kirjeet);
		return get(batch, LOCALHOST.jalkiohjauskirjeZip());
	}

	public static List<List<String>> generateHyvaksymiskirje(
			final Hyvaksymiskirje kirje) throws Exception {
		HyvaksymiskirjeBatch batch = new HyvaksymiskirjeBatchStub(kirje);
		return readPDF(get(batch, LOCALHOST.hyvaksymiskirjePdf()), 1, 2);
	}

	public static String generateLiite(Jalkiohjauskirje kirje) throws Exception {
		JalkiohjauskirjeBatch batch = new JalkiohjauskirjeBatchStub(
				Arrays.asList(kirje));
		return readAsHtml(get(batch, LOCALHOST.jalkiohjauskirjePdf()), 2, 2);
	}

	private static byte[] get(Object json, String url)
			throws UnsupportedEncodingException, IOException,
			JsonGenerationException, JsonMappingException,
			ClientProtocolException, DocumentException {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json;charset=utf-8");
		String postEntityJson = new ViestintapalveluObjectMapper()
				.writeValueAsString(json);
		post.setEntity(new StringEntity(postEntityJson,
				ContentType.APPLICATION_JSON));
		HttpResponse response = client.execute(post);
		String resultUrl = IOUtils.toString(response.getEntity().getContent(),
				"UTF-8");
		HttpGet get = new HttpGet(resultUrl);
		response = client.execute(get);
		return IOUtils.toByteArray(response.getEntity().getContent());
	}

	private static List<List<String>> readPDF(byte[] byteDocument,
			int startPage, int endPage) throws IOException, DocumentException {
		return parseHTML(readAsHtml(byteDocument, startPage, endPage)
				.getBytes());
	}

	private static String readAsHtml(byte[] byteDocument, int startPage,
			int endPage) throws IOException {
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

	private static List<List<String>> parseHTML(byte[] document)
			throws DocumentException {
		org.w3c.dom.Document doc = newTidy()
				.parseDOM(new ByteArrayInputStream(document),
						new ByteArrayOutputStream());
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
