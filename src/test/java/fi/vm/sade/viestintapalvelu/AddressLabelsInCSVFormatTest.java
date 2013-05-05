package fi.vm.sade.viestintapalvelu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.odftoolkit.odfdom.converter.core.utils.ByteArrayOutputStream;

import fi.vm.sade.viestintapalvelu.testdata.Generator;

@RunWith(Enclosed.class)
public class AddressLabelsInCSVFormatTest {
		
	@BeforeClass
	public static void setUp() throws Exception {
		Launcher.start();
	}

	public static class WhenCreatingLabelForValidForeignAddress {

		private static AddressLabel label = new AddressLabel("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "65330", "Stockholm", "Sweden");
		private static String[] csv;

		@BeforeClass
		public static void setUp() throws Exception {
			String responseBody = callGenerateLabels(Arrays.asList(label));
			csv = responseBody.split(",");
		}

		@Test
		public void firstNameIsMappedToFirstColumn() throws Exception {
			Assert.assertEquals(label.getFirstName(), csv[0]);
		}

		@Test
		public void lastNameIsMappedToSecondColumn() throws Exception {
			Assert.assertEquals(label.getLastName(), csv[1]);
		}

		@Test
		public void streetAddressIsMappedToThirdColumn() throws Exception {
			Assert.assertEquals(label.getStreetAddress(), csv[2]);
		}

		@Test
		public void postalCodeIsMappedToFouthColumn() throws Exception {
			Assert.assertEquals(label.getPostalCode(), csv[3]);
		}
		
		@Test
		public void postOfficeIsMappedToFifthColumn() throws Exception {
			Assert.assertEquals(label.getPostOffice(), csv[4]);
		}

		@Test
		public void countryIsMappedToSixthColumn() throws Exception {
			Assert.assertEquals(label.getCountry(), csv[5]);
		}

		@Test
		public void rowContainsSixColumns() throws Exception {
			Assert.assertEquals(6, csv.length);
		}
	}

	public static class WhenFirstNameIsEmpty {
		@Test
		public void firstColumnIsEmpty() throws Exception {
			Assert.assertEquals("", callGenerateLabels("", "Öistämö", "Brännkyrksgatan 177 B 149", "65330", "Stockholm", "Sweden")[0]);
		}
	}

	public static class WhenLastNameIsEmpty {
		@Test
		public void secondColumnIsEmpty() throws Exception {
			Assert.assertEquals("", callGenerateLabels("Åle", "", "Brännkyrksgatan 177 B 149", "65330", "Stockholm", "Sweden")[1]);
		}
	}

	public static class WhenStreetAddressIsEmpty {
		@Test
		public void thirdColumnIsEmpty() throws Exception {
			Assert.assertEquals("", callGenerateLabels("Åle", "Öistämö", "", "65330", "Stockholm", "Sweden")[2]);
		}
	}

	public static class WhenPostalCodeIsEmpty {
		@Test
		public void fourthColumnIsEmptyString() throws Exception {
			Assert.assertEquals("", callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "", "Stockholm", "Sweden")[3]);
		}
	}

	public static class WhenPostOfficeIsEmpty {
		@Test
		public void fifthColumnIsEmptyString() throws Exception {
			Assert.assertEquals("", callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "65330", "", "Sweden")[4]);
		}
	}

	public static class WhenCountryIsEmpty {
		@Test
		public void rowHasOnlyFiveColumns() throws Exception {
			Assert.assertEquals(5, callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "65330", "Stockholm", "").length);
		}
	}

	public static class WhenAddressIsLocal {
		@Test
		public void rowHasOnlyFiveColumns() throws Exception {
			Assert.assertEquals(5, callGenerateLabels("Åle", "Öistämö", "Mannerheimintie 177 B 149", "65330", "Helsinki", "Finland").length);
		}
	}

	public static class WhenAddressIsLocalAndCountryIsUppercaseFINLAND {
		@Test
		public void rowHasOnlyFiveColumns() throws Exception {
			Assert.assertEquals(5, callGenerateLabels("Åle", "Öistämö", "Mannerheimintie 177 B 149", "65330", "Helsinki", "FINLAND").length);
		}
	}

	public static class WhenCreatingLabelsForDomesticAndForeignAddresses {

		private static AddressLabel domestic = new AddressLabel("Åle", "Öistämö", "Mannerheimintie 177 B 149", "65330", "Helsinki", "FINLAND");
		private static AddressLabel foreign = new AddressLabel("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "65330", "Stockholm", "Sweden");
		private static String responseBody;

		@BeforeClass
		public static void setUp() throws Exception {
			responseBody = callGenerateLabels(Arrays.asList(domestic, foreign));
		}

		@Test
		public void responseContainsTwoAddressLabels() throws Exception {
			Assert.assertEquals(2, responseBody.split("\n").length);
		}

		@Test
		public void domesticAddressHasFiveColumns() throws Exception {
			Assert.assertEquals(5, responseBody.split("\n")[0].split(",").length);
		}

		@Test
		public void foreignAddressHasSixColumns() throws Exception {
			Assert.assertEquals(6, responseBody.split("\n")[1].split(",").length);
		}
	}

	public static class WhenCreatingLabelsInABigBatch {

		private static List<AddressLabel> batch = createLabels(1000);
		private static String responseBody;

		@BeforeClass
		public static void setUp() throws Exception {
			responseBody = callGenerateLabels(batch);
		}

		@Test
		public void returnedCSVContainsEqualAmountOfRows() throws Exception {
			Assert.assertEquals(batch.size(), responseBody.split("\n").length);
		}
	}

	private static String readResponseBody(HttpResponse response) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(response.getEntity().getContent());
		return out.toString();
	}
	
	private static List<AddressLabel> createLabels(int count) {
		return new Generator<AddressLabel>(){
			protected AddressLabel createObject(TestData testData) {
				String postOffice = testData.random("postOffice");
				return new AddressLabel(
						testData.random("firstname"), 
						testData.random("lastname"), 
						testData.random("street") + " " + testData.random("houseNumber"), 
						postOffice.substring(0, postOffice.indexOf(" ")),
						postOffice.substring(postOffice.indexOf(" ") + 1),
						testData.random("country"));
			}
		}.generateObjects(count);
	}

	private final static String CSV_TEMPLATE = "/osoitetarrat.csv"; 
	private static String[] callGenerateLabels(String firstName, String lastName, String streetAddress, String postalCode, String postOffice, String country) throws UnsupportedEncodingException,
			IOException, JsonGenerationException, JsonMappingException,	ClientProtocolException {
		return callGenerateLabels(Arrays.asList(new AddressLabel(firstName, lastName, streetAddress, postalCode, postOffice, country))).split(",");
	}
	
	private static String callGenerateLabels(List<AddressLabel> labels) throws UnsupportedEncodingException,
			IOException, JsonGenerationException, JsonMappingException,
			ClientProtocolException {
		AddressLabelBatch batch = new AddressLabelBatch(CSV_TEMPLATE, labels);
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
		HttpPost post = new HttpPost("http://localhost:8080/api/v1/addresslabel");
		post.setHeader("Content-Type", "application/json;charset=utf-8");
		post.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(batch), ContentType.APPLICATION_JSON));
		HttpResponse response = client.execute(post);
		return readResponseBody(response);
	}

}
