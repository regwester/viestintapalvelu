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
public class AddressLabelsInPDFFormatTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	public static class WhenCreatingLabelForValidForeignAddress {

		private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
				"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330",
				"Stockholm", "SL", "Sweden");
		private static List<String> pdf;

		@BeforeClass
		public static void setUp() throws Exception {
			pdf = TestUtil.generateAddressLabelsPDF(Arrays.asList(label)).get(0);
		}

		@Test
		public void firstNameAndLastNameAreMappedToFirstRow() throws Exception {
			Assert.assertEquals(
					label.getFirstName() + " " + label.getLastName(),
					pdf.get(0));
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
			Assert.assertEquals(label.getPostalCode() + " " + label.getCity(),
					pdf.get(4));
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
							"Brännkyrksgatan 177 B 149", "Södermalm", "13",
							"65330", "Stockholm", "SL", "Sweden").get(0));
		}
	}

	public static class WhenLastNameIsEmpty {
		@Test
		public void firstRowContainsFirstName() throws Exception {
			Assert.assertEquals(
					"Åle",
					callGenerateLabels("Åle", "", "Brännkyrksgatan 177 B 149",
							"Södermalm", "13", "65330", "Stockholm", "SL",
							"Sweden").get(0));
		}
	}

	public static class WhenAddresslineIsEmpty {
		@Test
		public void labelContainsNameAddressline2Addressline3PostOfficeRegionAndCountry()
				throws Exception {
			List<String> label = callGenerateLabels("Åle", "Öistämö", "",
					"Södermalm", "13", "65330", "Stockholm", "SL", "Sweden");
			Assert.assertEquals("Åle Öistämö", label.get(0));
			Assert.assertEquals("Södermalm", label.get(1));
			Assert.assertEquals("13", label.get(2));
			Assert.assertEquals("65330 Stockholm", label.get(3));
			Assert.assertEquals("SL", label.get(4));
			Assert.assertEquals("Sweden", label.get(5));
			Assert.assertEquals(6, label.size());
		}
	}

	public static class WhenAddressline2IsEmpty {
		@Test
		public void labelContainsNameAddresslineAddressline3PostOfficeAndCountry()
				throws Exception {
			List<String> label = callGenerateLabels("Åle", "Öistämö",
					"Brännkyrksgatan 177 B 149", "", "13", "65330",
					"Stockholm", "SL", "Sweden");
			Assert.assertEquals("Åle Öistämö", label.get(0));
			Assert.assertEquals("Brännkyrksgatan 177 B 149", label.get(1));
			Assert.assertEquals("13", label.get(2));
			Assert.assertEquals("65330 Stockholm", label.get(3));
			Assert.assertEquals("SL", label.get(4));
			Assert.assertEquals("Sweden", label.get(5));
			Assert.assertEquals(6, label.size());
		}
	}

	public static class WhenAddressline3IsEmpty {
		@Test
		public void labelContainsNameAddresslineAddressline3PostOfficeAndCountry()
				throws Exception {
			List<String> label = callGenerateLabels("Åle", "Öistämö",
					"Brännkyrksgatan 177 B 149", "Södermalm", "", "65330",
					"Stockholm", "SL", "Sweden");
			Assert.assertEquals("Åle Öistämö", label.get(0));
			Assert.assertEquals("Brännkyrksgatan 177 B 149", label.get(1));
			Assert.assertEquals("Södermalm", label.get(2));
			Assert.assertEquals("65330 Stockholm", label.get(3));
			Assert.assertEquals("SL", label.get(4));
			Assert.assertEquals("Sweden", label.get(5));
			Assert.assertEquals(6, label.size());
		}
	}

	public static class WhenPostalCodeIsEmpty {
		@Test
		public void fifthRowContainsPostOffice() throws Exception {
			Assert.assertEquals(
					"Stockholm",
					callGenerateLabels("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13", "",
							"Stockholm", "SL", "Sweden").get(4));
		}
	}

	public static class WhenCityIsEmpty {
		@Test
		public void fifthRowContainsPostalCode() throws Exception {
			Assert.assertEquals(
					"65330",
					callGenerateLabels("Åle", "Öistämö",
							"Brännkyrksgatan 177 B 149", "Södermalm", "13",
							"65330", "", "SL", "Sweden").get(4));
		}
	}

	public static class WhenCountryIsEmpty {
		@Test
		public void labelContainsNameAddresslineAddressline2Addressline3CityAndRegion()
				throws Exception {
			List<String> label = callGenerateLabels("Åle", "Öistämö",
					"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330",
					"Stockholm", "SL", "");
			Assert.assertEquals("Åle Öistämö", label.get(0));
			Assert.assertEquals("Brännkyrksgatan 177 B 149", label.get(1));
			Assert.assertEquals("Södermalm", label.get(2));
			Assert.assertEquals("13", label.get(3));
			Assert.assertEquals("65330 Stockholm", label.get(4));
			Assert.assertEquals("SL", label.get(5));
			Assert.assertEquals(6, label.size());
		}
	}

	public static class WhenAddressIsLocal {
		@Test
		public void labelHasOnlyThreeRows() throws Exception {
			List<String> label = callGenerateLabels("Åle", "Öistämö",
					"Mannerheimintie 177 B 149", "", "", "65330", "Helsinki",
					"", "Finland");
			Assert.assertEquals("Åle Öistämö", label.get(0));
			Assert.assertEquals("Mannerheimintie 177 B 149", label.get(1));
			Assert.assertEquals("65330 Helsinki", label.get(2));
			Assert.assertEquals(3, label.size());
		}
	}

	public static class WhenAddressIsLocalAndCountryIsUppercaseFINLAND {
		@Test
		public void labelHasOnlyThreeRows() throws Exception {
			List<String> label = callGenerateLabels("Åle", "Öistämö",
					"Mannerheimintie 177 B 149", "", "", "65330", "Helsinki",
					"", "FINLAND");
			Assert.assertEquals("Åle Öistämö", label.get(0));
			Assert.assertEquals("Mannerheimintie 177 B 149", label.get(1));
			Assert.assertEquals("65330 Helsinki", label.get(2));
			Assert.assertEquals(3, label.size());
		}
	}

	public static class WhenCreatingLabelsForDomesticAndForeignAddresses {

		private static AddressLabel domestic = new AddressLabel("Åle",
				"Öistämö", "Mannerheimintie 177 B 149", "", "", "65330",
				"Helsinki", "", "FINLAND");
		private static AddressLabel foreign = new AddressLabel("Åle",
				"Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", "13",
				"65330", "Stockholm", "SL", "Sweden");
		private static List<List<String>> response;

		@BeforeClass
		public static void setUp() throws Exception {
			response = TestUtil.generateAddressLabelsPDF(Arrays.asList(domestic, foreign));
		}

		@Test
		public void responseContainsTwoAddressLabels() throws Exception {
			Assert.assertEquals(2, response.size());
		}

		@Test
		public void domesticAddressHasThreeRows() throws Exception {
			Assert.assertEquals(3, response.get(1).size()); // Order reversed
															// when parsing pdf
															// to html
		}

		@Test
		public void foreignAddressHasSevenRows() throws Exception {
			Assert.assertEquals(7, response.get(0).size()); // Order reversed
															// when parsing pdf
															// to html
		}
	}

	public static class WhenCreatingLabelsInABigBatch {

		private static List<AddressLabel> batch;
		private static List<List<String>> response;

		@BeforeClass
		public static void setUp() throws Exception {
			batch = createLabels(1000);
			response = TestUtil.generateAddressLabelsPDF(batch);
		}

		@Test
		public void returnedCSVContainsEqualAmountOfRows() throws Exception {
			Assert.assertEquals(batch.size(), response.size());
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
			String addressline3, String postalCode, String postOffice,
			String region, String country) throws Exception {
		return TestUtil.generateAddressLabelsPDF(
				Arrays.asList(new AddressLabel(firstName, lastName,
						addressline, addressline2, addressline3, postalCode,
						postOffice, region, country))).get(0);
	}
}
