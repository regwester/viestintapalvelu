package fi.vm.sade.viestintapalvelu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;

@RunWith(Enclosed.class)
public class HakutulostaulukkoPDFTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
			"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm", "SL", "Sweden", "FI");

	public static class WhenCreatingLiiteWithOneHakutoive {

		private static Map<String,String> tulos = createHaku("20", "30");
		private static List<List<String>> hakutoivetaulukko;

		@SuppressWarnings("unchecked")
		@BeforeClass
		public static void setUp() throws Exception {
			Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, Arrays.asList(tulos));
			hakutoivetaulukko = generateHakutoivetaulukko(kirje);
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

	public static class WhenCreatingLiiteWithSpecialCharacters {

		private static Map<String,String> tulos = createHaku("20&", "30&");
		private static List<List<String>> hakutoivetaulukko;

		@SuppressWarnings("unchecked")
		@BeforeClass
		public static void setUp() throws Exception {
			Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, Arrays.asList(tulos));
			hakutoivetaulukko = generateHakutoivetaulukko(kirje);
		}

		@Test
		public void aloituspaikatWithSpecialCharactersIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("aloituspaikat"), hakutoivetaulukko.get(1).get(0));
		}

		@Test
		public void varasijaWithSpecialCharactersIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("varasija"), hakutoivetaulukko.get(1).get(1));
		}
	}

	@SuppressWarnings("unchecked")
	public static class WhenCreatingLiiteWithNullInput {

		private static List<List<String>> hakutoivetaulukko;
		private static Map<String,String> tulos = createHaku(null, "30");

		@BeforeClass
		public static void setUp() throws Exception {
			Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, Arrays.asList(tulos));
			hakutoivetaulukko = generateHakutoivetaulukko(kirje);
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

		@BeforeClass
		public static void setUp() throws Exception {
			Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, Arrays.asList(tulos));
			hakutoivetaulukko = generateHakutoivetaulukko(kirje);
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

	private static List<List<String>> generateHakutoivetaulukko(
			Jalkiohjauskirje kirje) throws Exception {
		return Lists.transform(TestUtil.generateHakutulostaulukko(kirje).get(0), new Function<String, List<String>>() {
			public List<String> apply(String row) {
				return Arrays.asList(row.split(" "));
			}
		});
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
