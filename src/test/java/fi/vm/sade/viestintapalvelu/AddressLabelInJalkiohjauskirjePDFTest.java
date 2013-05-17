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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.JalkiohjauskirjeBatch;

@RunWith(Enclosed.class)
public class AddressLabelInJalkiohjauskirjePDFTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
			"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm", "SL", "Sweden");
	private static List<String[]> pdf;
	private static String[] pdfLabel;

	@BeforeClass
	public static void setUp() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, new ArrayList<Map<String,String>>());
		pdf = callGenerateJalkiohjauskirje(Arrays.asList(kirje));
		pdfLabel = findAddressLabel(pdf);
	}

	@Test
	public void firstNameAndLastNameAreMappedToFirstRow() throws Exception {
		Assert.assertEquals(
				label.getFirstName() + " " + label.getLastName(), pdfLabel[0]);
	}

	@Test
	public void streetAddresslineIsMappedToSecondRow() throws Exception {
		Assert.assertEquals(label.getAddressline(), pdfLabel[1]);
	}

	@Test
	public void streetAddressline2IsMappedToThirdRow() throws Exception {
		Assert.assertEquals(label.getAddressline2(), pdfLabel[2]);
	}

	@Test
	public void streetAddressline3IsMappedToFourthRow() throws Exception {
		Assert.assertEquals(label.getAddressline3(), pdfLabel[3]);
	}

	@Test
	public void postalCodeAndPostOfficeAreMappedToFifthRow()
			throws Exception {
		Assert.assertEquals(label.getPostalCode() + " " + label.getCity(), pdfLabel[4]);
	}

	@Test
	public void regionIsMappedToSixthRow() throws Exception {
		Assert.assertEquals(label.getRegion(), pdfLabel[5]);
	}

	@Test
	public void countryIsMappedToSeventhRow() throws Exception {
		Assert.assertEquals(label.getCountry(), pdfLabel[6]);
	}

	@Test
	public void labelContainsSevenRows() throws Exception {
		Assert.assertEquals(7, pdfLabel.length);
	}

	private static List<String[]> readDownloadResponseBody(HttpResponse response)
			throws IOException, DocumentException {
		PDDocument document = PDDocument
				.load(response.getEntity().getContent());
		PDFText2HTML stripper = new PDFText2HTML("UTF-8");
		StringWriter writer = new StringWriter();
		stripper.setLineSeparator("<br/>");
		stripper.writeText(document, writer);
		document.close();
		return parseHTML(new String(toXhtml(writer.toString().getBytes())));
	}

	private static String readCreateDocumentResponseBody(HttpResponse response)
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

	private static List<String[]> parseHTML(String xml)
			throws DocumentException {
		SAXReader reader = new SAXReader();
		List<String[]> labels = new ArrayList<String[]>();
		Document document = reader.read(new StringReader(xml));
		for (Object object : document.selectNodes("//div/p")) {
			Node p = (Node) object;
			labels.add(p.getText().split("\n"));
		}
		return labels;
	}

	private final static String KIRJE_TEMPLATE = "/jalkiohjauskirje.html";
	private final static String LIITE_TEMPLATE = "/liite.html";

	private static List<String[]> callGenerateJalkiohjauskirje(List<Jalkiohjauskirje> letters)
			throws UnsupportedEncodingException, IOException,
			JsonGenerationException, JsonMappingException,
			ClientProtocolException, DocumentException {
		JalkiohjauskirjeBatch batch = new JalkiohjauskirjeBatch(KIRJE_TEMPLATE, LIITE_TEMPLATE, letters);
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");
		HttpPost post = new HttpPost(
				"http://localhost:8080/api/v1/jalkiohjauskirje/pdf");
		post.setHeader("Content-Type", "application/json;charset=utf-8");
		post.setEntity(new StringEntity(new ObjectMapper()
				.writeValueAsString(batch), ContentType.APPLICATION_JSON));
		HttpResponse response = client.execute(post);
		String documentId = readCreateDocumentResponseBody(response);
		HttpGet get = new HttpGet(
				"http://localhost:8080/api/v1/download/document/"
						+ documentId);
		response = client.execute(get);
		return readDownloadResponseBody(response);
	}

	private static String[] findAddressLabel(List<String[]> pdf) {
		String[] address = Iterables.find(pdf, new Predicate<String[]>() {
			public boolean apply(String[] element) {
				return element.length > 0 && element[0].startsWith("Priority PP Finlande");
			}
		});
		return Arrays.copyOfRange(address, 1, address.length);
	}
}
