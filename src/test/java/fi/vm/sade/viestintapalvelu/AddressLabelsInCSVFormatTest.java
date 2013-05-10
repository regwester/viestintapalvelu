package fi.vm.sade.viestintapalvelu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.odftoolkit.odfdom.converter.core.utils.ByteArrayOutputStream;

import fi.vm.sade.viestintapalvelu.testdata.Generator;

@RunWith(Enclosed.class)
public class AddressLabelsInCSVFormatTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	public static class WhenCreatingLabelForValidForeignAddress {

		private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
				"Brännkyrksgatan 177 B 149", "", "65330", "Stockholm", "", "Sweden");
		private static String[] otsikko;
		private static String[] osoite;

		@BeforeClass
		public static void setUp() throws Exception {
			String responseBody[] = callGenerateLabels(Arrays.asList(label)).split("\n");
			otsikko = responseBody[0].split(",");
			osoite = responseBody[1].split(",");
		}

		@Test
		public void firstNameIsMappedToFirstColumn() throws Exception {
			Assert.assertEquals("Firstname", otsikko[0]);
			Assert.assertEquals(label.getFirstName(), osoite[0]);
		}

		@Test
		public void lastNameIsMappedToSecondColumn() throws Exception {
			Assert.assertEquals("Lastname", otsikko[1]);
			Assert.assertEquals(label.getLastName(), osoite[1]);
		}

		@Test
		public void addressline1IsMappedToThirdColumn() throws Exception {
			Assert.assertEquals("Addressline1", otsikko[2]);
			Assert.assertEquals(label.getAddressline(), osoite[2]);
		}

		@Test
		public void postalCodeIsMappedToFouthColumn() throws Exception {
			Assert.assertEquals("Postal Code", otsikko[3]);
			Assert.assertEquals(label.getPostalCode(), osoite[3]);
		}

		@Test
		public void cityIsMappedToFifthColumn() throws Exception {
			Assert.assertEquals("City", otsikko[4]);
			Assert.assertEquals(label.getCity(), osoite[4]);
		}

		@Test
		public void countryIsMappedToSixthColumn() throws Exception {
			Assert.assertEquals("Country", otsikko[5]);
			Assert.assertEquals(label.getCountry(), osoite[5]);
		}

		@Test
		public void rowContainsSixColumns() throws Exception {
			Assert.assertEquals(6, osoite.length);
		}
	}

	public static class WhenFirstNameIsEmpty {
		@Test
		public void firstColumnIsEmpty() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("", "Öistämö",
							"Brännkyrksgatan 177 B 149", "65330", "Stockholm",
							"Sweden")[0]);
		}
	}

	public static class WhenLastNameIsEmpty {
		@Test
		public void secondColumnIsEmpty() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "", "Brännkyrksgatan 177 B 149",
							"65330", "Stockholm", "Sweden")[1]);
		}
	}

	public static class WhenStreetAddressIsEmpty {
		@Test
		public void thirdColumnIsEmpty() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö", "", "65330",
							"Stockholm", "Sweden")[2]);
		}
	}

	public static class WhenPostalCodeIsEmpty {
		@Test
		public void fourthColumnIsEmptyString() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "", "Stockholm",
							"Sweden")[3]);
		}
	}

	public static class WhenPostOfficeIsEmpty {
		@Test
		public void fifthColumnIsEmptyString() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "65330", "", "Sweden")[4]);
		}
	}

	public static class WhenCountryIsEmpty {
		@Test
		public void sixthColumnIsEmptyString() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "65330", "Stockholm",
							"")[5]);
		}
	}

	public static class WhenAddressIsLocal {
		@Test
		public void sixthColumnIsEmptyString() throws Exception {
			String label[] = callGenerateLabels("Åle", "Öistämö",
					"Mannerheimintie 177 B 149", "65330", "Helsinki",
					"Finland");
			Assert.assertEquals("", label[5]);
		}
	}

	public static class WhenAddressIsLocalAndCountryIsUppercaseFINLAND {
		@Test
		public void sixthColumnIsEmptyString() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö",
							"Mannerheimintie 177 B 149", "65330", "Helsinki",
							"FINLAND")[5]);
		}
	}

	public static class WhenCreatingLabelsForDomesticAndForeignAddresses {

		private static AddressLabel domestic = new AddressLabel("Åle",
				"Öistämö", "Mannerheimintie 177 B 149", "", "65330", "Helsinki",
				"", "FINLAND");
		private static AddressLabel foreign = new AddressLabel("Åle",
				"Öistämö", "Brännkyrksgatan 177 B 149", "", "65330", "Stockholm",
				"", "Sweden");
		private static String responseBody;

		@BeforeClass
		public static void setUp() throws Exception {
			responseBody = callGenerateLabels(Arrays.asList(domestic, foreign));
		}

		@Test
		public void responseContainsHeaderAndTwoAddressLabels() throws Exception {
			Assert.assertEquals(3, responseBody.split("\n").length);
		}

		@Test
		public void domesticAddressHasFiveColumns() throws Exception {
			Assert.assertEquals(5,
					responseBody.split("\n")[1].split(",").length);
		}

		@Test
		public void foreignAddressHasSixColumns() throws Exception {
			Assert.assertEquals(6,
					responseBody.split("\n")[2].split(",").length);
		}
	}

	public static class WhenCreatingLabelsInABigBatch {

		private static List<AddressLabel> batch;
		private static String responseBody;

		@BeforeClass
		public static void setUp() throws Exception {
			batch = createLabels(1000);
			responseBody = callGenerateLabels(batch);
		}

		@Test
		public void returnedCSVContainsHeaderEqualAmountOfLabels() throws Exception {
			Assert.assertEquals(batch.size() + 1, responseBody.split("\n").length);
		}
	}

	private static String readResponseBody(HttpResponse response, boolean removeBOM)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(response.getEntity().getContent());
		return removeBOM ? new String(removeBOM(out.toByteArray())) : out.toString();
	}

	private static byte[] removeBOM(byte[] byteArray) {
		return Arrays.copyOfRange(byteArray, 3, byteArray.length);
	}

	private static List<AddressLabel> createLabels(int count)
			throws JsonParseException, JsonMappingException, IOException {
		return new Generator<AddressLabel>() {
			protected AddressLabel createObject(TestData testData) {
				String postOffice = testData.random("postOffice");
				return new AddressLabel(testData.random("firstname"),
						testData.random("lastname"), testData.random("street")
								+ " " + testData.random("houseNumber"), "",
						postOffice.substring(0, postOffice.indexOf(" ")),
						postOffice.substring(postOffice.indexOf(" ") + 1), "",
						testData.random("country"));
			}
		}.generateObjects(count);
	}

	private final static String CSV_TEMPLATE = "/osoitetarrat.csv";

	private static String[] callGenerateLabels(String firstName,
			String lastName, String streetAddress, String postalCode,
			String postOffice, String country)
			throws UnsupportedEncodingException, IOException,
			JsonGenerationException, JsonMappingException,
			ClientProtocolException {
		return callGenerateLabels(
				Arrays.asList(new AddressLabel(firstName, lastName,
						streetAddress, "", postalCode, postOffice, "", country))).split("\n")[1]
				.split(",", 20);
	}

	private static String callGenerateLabels(List<AddressLabel> labels)
			throws UnsupportedEncodingException, IOException,
			JsonGenerationException, JsonMappingException,
			ClientProtocolException {
		AddressLabelBatch batch = new AddressLabelBatch(CSV_TEMPLATE, labels);
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");
		HttpPost post = new HttpPost(
				"http://localhost:8080/api/v1/addresslabel/createDocument");
		post.setHeader("Content-Type", "application/json;charset=utf-8");
		post.setEntity(new StringEntity(new ObjectMapper()
				.writeValueAsString(batch), ContentType.APPLICATION_JSON));
		HttpResponse response = client.execute(post);
		String documentId = readResponseBody(response, false);
		HttpGet get = new HttpGet(
				"http://localhost:8080/api/v1/addresslabel/download/"
						+ documentId);
		response = client.execute(get);
		return readResponseBody(response, true);
	}

}
