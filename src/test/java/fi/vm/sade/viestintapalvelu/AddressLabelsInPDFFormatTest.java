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
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.AddressLabelBatch;
import fi.vm.sade.viestintapalvelu.testdata.Generator;

@RunWith(Enclosed.class)
public class AddressLabelsInPDFFormatTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	public static class WhenCreatingLabelForValidForeignAddress {

		private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
				"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm", "SL", "Sweden");
		private static List<String> pdf;

		@BeforeClass
		public static void setUp() throws Exception {
			pdf = TestUtil.generateAddressLabels(Arrays.asList(label)).get(0);
		}

		@Test
		public void firstNameAndLastNameAreMappedToFirstRow() throws Exception {
			Assert.assertEquals(
					label.getFirstName() + " " + label.getLastName(), pdf.get(0));
		}

		@Test
		public void streetAddresslineIsMappedToSecondRow() throws Exception {
			Assert.assertEquals(label.getAddressline(), pdf.get(1));
		}

		@Test
		public void streetAddressline2IsMappedToThirdRow() throws Exception {
			Assert.assertEquals(label.getAddressline2(), pdf.get(2));
		}

		@Test
		public void streetAddressline3IsMappedToFourthRow() throws Exception {
			Assert.assertEquals(label.getAddressline3(), pdf.get(3));
		}

		@Test
		public void postalCodeAndPostOfficeAreMappedToFifthRow()
				throws Exception {
			Assert.assertEquals(label.getPostalCode() + " " + label.getCity(), pdf.get(4));
		}

		@Test
		public void regionIsMappedToSixthRow() throws Exception {
			Assert.assertEquals(label.getRegion(), pdf.get(5));
		}

		@Test
		public void countryIsMappedToSeventhRow() throws Exception {
			Assert.assertEquals(label.getCountry(), pdf.get(6));
		}

		@Test
		public void labelContainsSevenRows() throws Exception {
			Assert.assertEquals(7, pdf.size());
		}
	}

	public static class WhenFirstNameIsEmpty {
		@Test
		public void firstRowContainsLastName() throws Exception {
			Assert.assertEquals(
					"Öistämö",
					callGenerateLabels("", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm",
							"SL", "Sweden")[0]);
		}
	}

	public static class WhenLastNameIsEmpty {
		@Test
		public void firstRowContainsFirstName() throws Exception {
			Assert.assertEquals(
					"Åle",
					callGenerateLabels("Åle", "", "Brännkyrksgatan 177 B 149",
							"Södermalm", "13", "65330", "Stockholm", "SL", "Sweden")[0]);
		}
	}

	public static class WhenAddresslineIsEmpty {
		@Test
		public void labelContainsNameAddressline2Addressline3PostOfficeRegionAndCountry() throws Exception {
			String[] label = callGenerateLabels("Åle", "Öistämö", "", "Södermalm", "13", "65330",
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
			String[] label = callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "", "13", "65330",
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
			String[] label = callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", "", "65330",
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
					callGenerateLabels("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "", "Stockholm",
							"SL", "Sweden")[4]);
		}
	}

	public static class WhenCityIsEmpty {
		@Test
		public void fifthRowContainsPostalCode() throws Exception {
			Assert.assertEquals(
					"65330",
					callGenerateLabels("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "", "SL", "Sweden")[4]);
		}
	}

	public static class WhenCountryIsEmpty {
		@Test
		public void labelContainsNameAddresslineAddressline2Addressline3CityAndRegion() throws Exception {
			String[] label = callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330",
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
			String[] label = callGenerateLabels("Åle", "Öistämö", "Mannerheimintie 177 B 149", "", "", "65330", "Helsinki",
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
			String[] label = callGenerateLabels("Åle", "Öistämö", "Mannerheimintie 177 B 149", "", "", "65330", "Helsinki",
					"", "FINLAND");
			Assert.assertEquals("Åle Öistämö", label[0]);
			Assert.assertEquals("Mannerheimintie 177 B 149", label[1]);
			Assert.assertEquals("65330 Helsinki", label[2]);
			Assert.assertEquals(3, label.length);
		}
	}

	public static class WhenCreatingLabelsForDomesticAndForeignAddresses {

		private static AddressLabel domestic = new AddressLabel("Åle",
				"Öistämö", "Mannerheimintie 177 B 149", "", "", "65330", "Helsinki",
				"", "FINLAND");
		private static AddressLabel foreign = new AddressLabel("Åle",
				"Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm",
				"SL", "Sweden");
		private static List<String[]> response;

		@BeforeClass
		public static void setUp() throws Exception {
			response = callGenerateLabels(Arrays.asList(domestic, foreign));
		}

		@Test
		public void responseContainsTwoAddressLabels() throws Exception {
			Assert.assertEquals(2, response.size());
		}

		@Test
		public void domesticAddressHasThreeRows() throws Exception {
			Assert.assertEquals(3, response.get(1).length); // Order reversed
															// when parsing pdf
															// to html
		}

		@Test
		public void foreignAddressHasSevenRows() throws Exception {
			Assert.assertEquals(7, response.get(0).length); // Order reversed
															// when parsing pdf
															// to html
		}
	}

	public static class WhenCreatingLabelsInABigBatch {

		private static List<AddressLabel> batch;
		private static List<String[]> response;

		@BeforeClass
		public static void setUp() throws Exception {
			batch = createLabels(1000);
			response = callGenerateLabels(batch);
		}

		@Test
		public void returnedCSVContainsEqualAmountOfRows() throws Exception {
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

	private static List<AddressLabel> createLabels(int count)
			throws JsonParseException, JsonMappingException, IOException {
		return new Generator<AddressLabel>() {
			protected AddressLabel createObject(TestData testData) {
				String postOffice = testData.random("postOffice");
				return new AddressLabel(testData.random("firstname"),
						testData.random("lastname"), testData.random("street")
								+ " " + testData.random("houseNumber"), "", "",
						postOffice.substring(0, postOffice.indexOf(" ")),
						postOffice.substring(postOffice.indexOf(" ") + 1), "",
						testData.random("country"));
			}
		}.generateObjects(count);
	}

	private static String[] callGenerateLabels(String firstName,
			String lastName, String addressline, String addressline2, 
			String addressline3, String postalCode,
			String postOffice, String region, String country)
			throws UnsupportedEncodingException, IOException,
			JsonGenerationException, JsonMappingException,
			ClientProtocolException, DocumentException {
		return callGenerateLabels(
				Arrays.asList(new AddressLabel(firstName, lastName,
						addressline, addressline2, addressline3, postalCode, 
						postOffice, region, country)))
				.get(0);
	}

	private final static String PDF_TEMPLATE = "/osoitetarrat.html";

	private static List<String[]> callGenerateLabels(List<AddressLabel> labels)
			throws UnsupportedEncodingException, IOException,
			JsonGenerationException, JsonMappingException,
			ClientProtocolException, DocumentException {
		AddressLabelBatch batch = new AddressLabelBatch(PDF_TEMPLATE, labels);
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");
		HttpPost post = new HttpPost(
				"http://localhost:8080/api/v1/addresslabel/pdf");
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

}
