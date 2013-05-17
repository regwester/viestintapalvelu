package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.JalkiohjauskirjeBatch;

@RunWith(Enclosed.class)
public class HakutulostaulukkoPDFTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
			"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm", "SL", "Sweden");

	public static class WhenCreatingLiiteWithOneHakutoive {

		private static Map<String,String> tulos = createHaku("20", "30");
		private static List<List<String>> hakutoivetaulukko;

		@SuppressWarnings("unchecked")
		@BeforeClass
		public static void setUp() throws Exception {
			Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, Arrays.asList(tulos));
			hakutoivetaulukko = callGenerateJalkiohjauskirje(Arrays.asList(kirje));
		}

		@Test
		public void aloituspaikatHeaderIsPrinted() throws Exception {
			Assert.assertEquals("Aloituspaikat", hakutoivetaulukko.get(0).get(0));
		}

		@Test
		public void varasijaHeaderIsPrinted() throws Exception {
			Assert.assertEquals("Varasija", hakutoivetaulukko.get(0).get(1));
		}

		@Test
		public void aloituspaikatIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("aloituspaikat"), hakutoivetaulukko.get(1).get(0));
		}

		@Test
		public void varasijaIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("varasija"), hakutoivetaulukko.get(1).get(1));
		}
	}

	@SuppressWarnings("unchecked")
	public static class WhenCreatingLiiteWithNullInput {

		private static List<List<String>> hakutoivetaulukko;
		private static Map<String,String> tulos = createHaku(null, "30");
		private static List<Jalkiohjauskirje> request = Arrays.asList(new Jalkiohjauskirje(label, Arrays.asList(tulos)));

		@BeforeClass
		public static void setUp() throws Exception {
			hakutoivetaulukko = callGenerateJalkiohjauskirje(request);
		}

		@Test
		public void aloituspaikatHeaderIsPrinted() throws Exception {
			Assert.assertEquals("Aloituspaikat", hakutoivetaulukko.get(0).get(0));
		}

		@Test
		public void varasijaHeaderIsPrinted() throws Exception {
			Assert.assertEquals("Varasija", hakutoivetaulukko.get(0).get(1));
		}

		@Test
		public void aloituspaikatIsEmptyStringAndVarasijaIsPrinted() throws Exception {
			Assert.assertEquals(1, hakutoivetaulukko.get(1).size());
			Assert.assertEquals(tulos.get("varasija"), hakutoivetaulukko.get(1).get(0));
		}
	}

	@SuppressWarnings("unchecked")
	public static class WhenCreatingLiiteWithWithoutOnePredefinedColumn {

		private static List<List<String>> hakutoivetaulukko;
		private static Map<String,String> tulos = createHaku("20");
		private static List<Jalkiohjauskirje> request = Arrays.asList(new Jalkiohjauskirje(label, Arrays.asList(tulos)));

		@BeforeClass
		public static void setUp() throws Exception {
			hakutoivetaulukko = callGenerateJalkiohjauskirje(request);
		}

		@Test
		public void sentHeadersArePrinted() throws Exception {
			Assert.assertEquals("Aloituspaikat", hakutoivetaulukko.get(0).get(0));
			Assert.assertEquals(1, hakutoivetaulukko.get(0).size());
		}

		@Test
		public void sentColumnsArePrinted() throws Exception {
			Assert.assertEquals(tulos.get("aloituspaikat"), hakutoivetaulukko.get(1).get(0));
			Assert.assertEquals(1, hakutoivetaulukko.get(1).size());
		}
	}

	private static List<List<String>> readDownloadResponseBody(HttpResponse response)
			throws IOException, DocumentException {
		PDDocument document = PDDocument
				.load(response.getEntity().getContent());
		PDFText2HTML stripper = new PDFText2HTML("UTF-8");
		StringWriter writer = new StringWriter();
		stripper.setLineSeparator("<br/>");
		stripper.setAddMoreFormatting(true);
		stripper.setStartPage(2);
		stripper.setEndPage(2);
		stripper.writeText(document, writer);
		document.close();
		return parseHTML(new String(toXhtml(writer.toString().getBytes())));
	}

	public static List<String> stripHeaders(List<String> hakutoivetaulukko, String firstHeader) {
		return hakutoivetaulukko.subList(0, hakutoivetaulukko.indexOf(firstHeader));
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

	private static List<List<String>> parseHTML(String xml)
			throws DocumentException {
		SAXReader reader = new SAXReader();
		List<List<String>> labels = new ArrayList<List<String>>();
		Document document = reader.read(new StringReader(xml));
		for (String row : document.selectSingleNode("//div/p").getText().split("\n")) {
			labels.add(Arrays.asList(row.split(" ")));
		}
		return labels;
	}

	private final static String KIRJE_TEMPLATE = "/jalkiohjauskirje.html";
	private final static String LIITE_TEMPLATE = "/hakutulostaulukko_test.html";

	private static List<List<String>> callGenerateJalkiohjauskirje(List<Jalkiohjauskirje> letters)
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

	private static Map<String, String> createHaku(String aloituspaikat, String varasija) {
		Map<String, String> toive = new HashMap<String, String>();
		toive.put("aloituspaikat", aloituspaikat);
		toive.put("varasija", varasija);
		return toive;
	}

	private static Map<String, String> createHaku(String aloituspaikat) {
		Map<String, String> toive = new HashMap<String, String>();
		toive.put("aloituspaikat", aloituspaikat);
		return toive;
	}
}
