package fi.vm.sade.viestintapalvelu;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.testdata.Generator;

@RunWith(Enclosed.class)
public class AddressLabelsInXLSFormatTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	public static class WhenCreatingLabelForValidForeignAddress {

		private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
				"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm", "SL", "Sweden");
		private static List<String> otsikko;
		private static List<String> osoite;

		@BeforeClass
		public static void setUp() throws Exception {
			List<List<String>> responseBody = TestUtil.generateAddressLabelsXLS(Arrays.asList(label));
			otsikko = responseBody.get(0);
			osoite = responseBody.get(1);
		}

		@Test
		public void firstNameIsMappedToFirstColumn() throws Exception {
			Assert.assertEquals("Firstname", otsikko.get(0));
			Assert.assertEquals(label.getFirstName(), osoite.get(0));
		}

		@Test
		public void lastNameIsMappedToSecondColumn() throws Exception {
			Assert.assertEquals("Lastname", otsikko.get(1));
			Assert.assertEquals(label.getLastName(), osoite.get(1));
		}

		@Test
		public void addresslineIsMappedToThirdColumn() throws Exception {
			Assert.assertEquals("Addressline", otsikko.get(2));
			Assert.assertEquals(label.getAddressline(), osoite.get(2));
		}

		@Test
		public void addressline2IsMappedToFouthColumn() throws Exception {
			Assert.assertEquals("Addressline2", otsikko.get(3));
			Assert.assertEquals(label.getAddressline2(), osoite.get(3));
		}

		@Test
		public void addressline3IsMappedToFifthColumn() throws Exception {
			Assert.assertEquals("Addressline3", otsikko.get(4));
			Assert.assertEquals(label.getAddressline3(), osoite.get(4));
		}

		@Test
		public void postalCodeIsMappedToSixthColumn() throws Exception {
			Assert.assertEquals("Postal Code", otsikko.get(5));
			Assert.assertEquals(label.getPostalCode(), osoite.get(5));
		}

		@Test
		public void cityIsMappedToSeventhColumn() throws Exception {
			Assert.assertEquals("City", otsikko.get(6));
			Assert.assertEquals(label.getCity(), osoite.get(6));
		}

		@Test
		public void regionIsMappedToEightColumn() throws Exception {
			Assert.assertEquals("Region", otsikko.get(7));
			Assert.assertEquals(label.getRegion(), osoite.get(7));
		}

		@Test
		public void countryIsMappedToNinethColumn() throws Exception {
			Assert.assertEquals("Country", otsikko.get(8));
			Assert.assertEquals(label.getCountry(), osoite.get(8));
		}

		@Test
		public void rowContainsNineColumns() throws Exception {
			Assert.assertEquals(9, osoite.size());
		}
	}

	public static class WhenFirstNameIsEmpty {
		@Test
		public void firstColumnIsEmpty() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm",
							"SL", "Sweden").get(0));
		}
	}

	public static class WhenLastNameIsEmpty {
		@Test
		public void secondColumnIsEmpty() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "", "Brännkyrksgatan 177 B 149",
							"Södermalm", "13", "65330", "Stockholm", "SL", "Sweden").get(1));
		}
	}

	public static class WhenAddresslineIsEmpty {
		@Test
		public void thirdColumnIsEmpty() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö", "", "Södermalm", "13", "65330",
							"Stockholm", "SL", "Sweden").get(2));
		}
	}

	public static class WhenAddressline2IsEmpty {
		@Test
		public void fourthColumnIsEmpty() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "", 
							"13", "65330", "Stockholm", "SL", "Sweden").get(3));
		}
	}

	public static class WhenAddressline3IsEmpty {
		@Test
		public void fifthColumnIsEmpty() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", 
							"", "65330", "Stockholm", "SL", "Sweden").get(4));
		}
	}

	public static class WhenPostalCodeIsEmpty {
		@Test
		public void sixthColumnIsEmptyString() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "", "Stockholm",
							"SL", "Sweden").get(5));
		}
	}

	public static class WhenPostOfficeIsEmpty {
		@Test
		public void seventhColumnIsEmptyString() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "", "SL","Sweden").get(6));
		}
	}

	public static class WhenRegionIsEmpty {
		@Test
		public void eightColumnIsEmptyString() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm",
							"", "Sweden").get(7));
		}
	}

	public static class WhenCountryIsEmpty {
		@Test
		public void ninethColumnIsEmptyString() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm",
							"SL", "").get(8));
		}
	}

	public static class WhenAddressIsLocal {
		@Test
		public void seventhColumnIsEmptyString() throws Exception {
			List<String> label = callGenerateLabels("Åle", "Öistämö",
					"Mannerheimintie 177 B 149", "", "", "65330", "Helsinki",
					"", "Finland");
			Assert.assertEquals("", label.get(8));
		}
	}

	public static class WhenAddressIsLocalAndCountryIsUppercaseFINLAND {
		@Test
		public void seventhColumnIsEmptyString() throws Exception {
			Assert.assertEquals(
					"",
					callGenerateLabels("Åle", "Öistämö",
							"Mannerheimintie 177 B 149", "", "", "65330", "Helsinki",
							"", "FINLAND").get(8));
		}
	}

	public static class WhenCreatingLabelsInABigBatch {

		private static List<AddressLabel> batch;
		private static List<List<String>> responseBody;

		@BeforeClass
		public static void setUp() throws Exception {
			batch = createLabels(1000);
			responseBody = TestUtil.generateAddressLabelsXLS(batch);
		}

		@Test
		public void returnedXLSContainsHeaderAndEqualAmountOfLabels() throws Exception {
			Assert.assertEquals(batch.size() + 1, responseBody.size());
		}
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

	private static List<String> callGenerateLabels(String firstName,
			String lastName, String addressline, String addressline2, 
			String addressline3, String postalCode,
			String city, String region, String country)
			throws Exception {
		AddressLabel label = new AddressLabel(firstName, lastName, addressline, addressline2, addressline3, postalCode, city, region, country);
		return TestUtil.generateAddressLabelsXLS(Arrays.asList(label)).get(1);
	}
}
