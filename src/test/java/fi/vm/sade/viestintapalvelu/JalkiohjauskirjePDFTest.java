package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import org.codehaus.jackson.JsonParseException;
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
import org.odftoolkit.odfdom.converter.core.utils.ByteArrayOutputStream;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.JalkiohjauskirjeBatch;
import fi.vm.sade.viestintapalvelu.testdata.Generator;

@RunWith(Enclosed.class)
public class JalkiohjauskirjePDFTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	public static class WhenCreatingJalkiohjauskirjeForAForeignStudent {

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
	}

	public static class WhenFirstNameIsEmpty {
		@Test
		public void firstRowContainsLastName() throws Exception {
			Assert.assertEquals(
					"Öistämö",
					callGenerateJalkiohjauskirje("", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm",
							"SL", "Sweden")[0]);
		}
	}

	public static class WhenLastNameIsEmpty {
		@Test
		public void firstRowContainsFirstName() throws Exception {
			Assert.assertEquals(
					"Åle",
					callGenerateJalkiohjauskirje("Åle", "", "Brännkyrksgatan 177 B 149",
							"Södermalm", "13", "65330", "Stockholm", "SL", "Sweden")[0]);
		}
	}

	public static class WhenAddresslineIsEmpty {
		@Test
		public void labelContainsNameAddressline2Addressline3PostOfficeRegionAndCountry() throws Exception {
			String[] label = callGenerateJalkiohjauskirje("Åle", "Öistämö", "", "Södermalm", "13", "65330",
					"Stockholm", "SL", "Sweden");
			Assert.assertEquals("Åle Öistämö", label[0]);
			Assert.assertEquals("Södermalm", label[1]);
			Assert.assertEquals("13", label[2]);
			Assert.assertEquals("65330 Stockholm", label[3]);
			Assert.assertEquals("SL", label[4]);
			Assert.assertEquals("Sweden", label[5]);
			Assert.assertEquals(6, label.length);
		}
	}

	public static class WhenAddressline2IsEmpty {
		@Test
		public void labelContainsNameAddresslineAddressline3PostOfficeAndCountry() throws Exception {
			String[] label = callGenerateJalkiohjauskirje("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "", "13", "65330",
					"Stockholm", "SL", "Sweden");
			Assert.assertEquals("Åle Öistämö", label[0]);
			Assert.assertEquals("Brännkyrksgatan 177 B 149", label[1]);
			Assert.assertEquals("13", label[2]);
			Assert.assertEquals("65330 Stockholm", label[3]);
			Assert.assertEquals("SL", label[4]);
			Assert.assertEquals("Sweden", label[5]);
			Assert.assertEquals(6, label.length);
		}
	}

	public static class WhenAddressline3IsEmpty {
		@Test
		public void labelContainsNameAddresslineAddressline3PostOfficeAndCountry() throws Exception {
			String[] label = callGenerateJalkiohjauskirje("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", "", "65330",
					"Stockholm", "SL", "Sweden");
			Assert.assertEquals("Åle Öistämö", label[0]);
			Assert.assertEquals("Brännkyrksgatan 177 B 149", label[1]);
			Assert.assertEquals("Södermalm", label[2]);
			Assert.assertEquals("65330 Stockholm", label[3]);
			Assert.assertEquals("SL", label[4]);
			Assert.assertEquals("Sweden", label[5]);
			Assert.assertEquals(6, label.length);
		}
	}

	public static class WhenPostalCodeIsEmpty {
		@Test
		public void fifthRowContainsPostOffice() throws Exception {
			Assert.assertEquals(
					"Stockholm",
					callGenerateJalkiohjauskirje("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "", "Stockholm",
							"SL", "Sweden")[4]);
		}
	}

	public static class WhenCityIsEmpty {
		@Test
		public void fifthRowContainsPostalCode() throws Exception {
			Assert.assertEquals(
					"65330",
					callGenerateJalkiohjauskirje("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "", "SL", "Sweden")[4]);
		}
	}

	public static class WhenCountryIsEmpty {
		@Test
		public void labelContainsNameAddresslineAddressline2Addressline3CityAndRegion() throws Exception {
			String[] label = callGenerateJalkiohjauskirje("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330",
					"Stockholm", "SL", "");
			Assert.assertEquals("Åle Öistämö", label[0]);
			Assert.assertEquals("Brännkyrksgatan 177 B 149", label[1]);
			Assert.assertEquals("Södermalm", label[2]);
			Assert.assertEquals("13", label[3]);
			Assert.assertEquals("65330 Stockholm", label[4]);
			Assert.assertEquals("SL", label[5]);
			Assert.assertEquals(6, label.length);
		}
	}

	public static class WhenAddressIsLocal {
		@Test
		public void labelHasOnlyThreeRows() throws Exception {
			String[] label = callGenerateJalkiohjauskirje("Åle", "Öistämö", "Mannerheimintie 177 B 149", "", "", "65330", "Helsinki",
					"", "Finland");
			Assert.assertEquals("Åle Öistämö", label[0]);
			Assert.assertEquals("Mannerheimintie 177 B 149", label[1]);
			Assert.assertEquals("65330 Helsinki", label[2]);
			Assert.assertEquals(3, label.length);
		}
	}

	public static class WhenAddressIsLocalAndCountryIsUppercaseFINLAND {
		@Test
		public void labelHasOnlyThreeRows() throws Exception {
			String[] label = callGenerateJalkiohjauskirje("Åle", "Öistämö", "Mannerheimintie 177 B 149", "", "", "65330", "Helsinki",
					"", "FINLAND");
			Assert.assertEquals("Åle Öistämö", label[0]);
			Assert.assertEquals("Mannerheimintie 177 B 149", label[1]);
			Assert.assertEquals("65330 Helsinki", label[2]);
			Assert.assertEquals(3, label.length);
		}
	}

	public static class WhenCreatingJalkiohjauskirjeInABigBatch {

		private static List<Jalkiohjauskirje> batch;
		private static List<String[]> response;

		@BeforeClass
		public static void setUp() throws Exception {
			batch = createJalkiohjauskirjeet(50);
			response = findAddressLabels(callGenerateJalkiohjauskirje(batch));
		}

		@Test
		public void returnedPDFContainsEqualAmountOfLetters() throws Exception {
			Assert.assertEquals(batch.size(), response.size());
		}
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
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(response.getEntity().getContent());
		return out.toString("UTF-8");
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

	private static List<Jalkiohjauskirje> createJalkiohjauskirjeet(int count)
			throws JsonParseException, JsonMappingException, IOException {
		return new Generator<Jalkiohjauskirje>() {
			protected Jalkiohjauskirje createObject(TestData testData) {
				String postOffice = testData.random("postOffice");
				AddressLabel label = new AddressLabel(testData.random("firstname"),
						testData.random("lastname"), testData.random("street")
								+ " " + testData.random("houseNumber"), "", "",
						postOffice.substring(0, postOffice.indexOf(" ")),
						postOffice.substring(postOffice.indexOf(" ") + 1), "",
						testData.random("country"));
				return new Jalkiohjauskirje(label, new ArrayList<Map<String,String>>());
			}
		}.generateObjects(count);
	}

	private static String[] callGenerateJalkiohjauskirje(String firstName,
			String lastName, String addressline, String addressline2, 
			String addressline3, String postalCode,
			String postOffice, String region, String country)
			throws UnsupportedEncodingException, IOException,
			JsonGenerationException, JsonMappingException,
			ClientProtocolException, DocumentException {
		AddressLabel label = new AddressLabel(firstName, lastName,
				addressline, addressline2, addressline3, postalCode, 
				postOffice, region, country);
		return findAddressLabel(callGenerateJalkiohjauskirje(
				Arrays.asList(new Jalkiohjauskirje(label, new ArrayList<Map<String,String>>()))));
	}

	private final static String KIRJE_TEMPLATE = "/jalkiohjauskirje.docx";
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
	
	private static List<String[]> findAddressLabels(List<String[]> pdf) {
		List<String[]> addresses = Lists.newArrayList(Iterables.filter(pdf, new Predicate<String[]>() {
			public boolean apply(String[] element) {
				return element.length > 0 && element[0].startsWith("Priority PP Finlande");
			}
		}));
		return addresses;
	}
}
