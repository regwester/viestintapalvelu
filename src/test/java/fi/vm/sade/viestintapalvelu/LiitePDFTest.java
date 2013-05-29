package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;

@RunWith(Enclosed.class)
public class LiitePDFTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
			"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330",
			"Stockholm", "SL", "Sweden", "FI");

	public static class WhenCreatingLiiteWithOneHakutoive {

		private static Map<String, String> tulos = createHaku("Asentaja", "20",
				"30");
		private static List<List<String>> liite;
		private static String[] hakutoiverivi;

		@SuppressWarnings("unchecked")
		@BeforeClass
		public static void setUp() throws Exception {
			Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, "FI",
					Arrays.asList(tulos));
			liite = TestUtil.generateLiite(kirje);
			hakutoiverivi = findHakutoiveRivi("Asentaja", liite).split(" ");
		}

		@Test
		public void aloituspaikatHeaderIsPrinted() throws Exception {
			assertTrue(findFromLiite(Arrays.asList("Aloitus-", "paikat"), liite));
		}

		@Test
		public void varasijaHeaderIsPrinted() throws Exception {
			assertTrue(findFromLiite(Arrays.asList("Vara-", "sija"), liite));
		}

		@Test
		public void aloituspaikatIsPrinted() throws Exception {
			assertEquals(tulos.get("aloituspaikat"), hakutoiverivi[3]);
		}

		@Test
		public void varasijaIsPrinted() throws Exception {
			assertEquals(tulos.get("varasija"), hakutoiverivi[4]);
		}
	}

	public static class WhenCreatingLiiteWithSpecialCharacters {

		private static Map<String, String> tulos = createHaku("Asentaja",
				"20&", "30&");
		private static String[] hakutoiverivi;

		@SuppressWarnings("unchecked")
		@BeforeClass
		public static void setUp() throws Exception {
			Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, "FI",
					Arrays.asList(tulos));
			List<List<String>> liite = TestUtil.generateLiite(kirje);
			hakutoiverivi = findHakutoiveRivi("Asentaja", liite).split(" ");
		}

		@Test
		public void aloituspaikatWithSpecialCharactersIsPrinted()
				throws Exception {
			assertEquals(tulos.get("aloituspaikat"), hakutoiverivi[3]);
		}

		@Test
		public void varasijaWithSpecialCharactersIsPrinted() throws Exception {
			assertEquals(tulos.get("varasija"), hakutoiverivi[4]);
		}
	}

	@SuppressWarnings("unchecked")
	public static class WhenCreatingLiiteWithNullInput {

		private static Map<String, String> tulos = createHaku("Asentaja", null,
				"30");
		private static List<List<String>> liite;
		private static String[] hakutoiverivi;

		@BeforeClass
		public static void setUp() throws Exception {
			Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, "FI",
					Arrays.asList(tulos));
			liite = TestUtil.generateLiite(kirje);
			hakutoiverivi = findHakutoiveRivi("Asentaja", liite).split(" ");
		}

		@Test
		public void aloituspaikatHeaderIsPrinted() throws Exception {
			assertTrue(findFromLiite(Arrays.asList("Aloitus-", "paikat"), liite));
		}

		@Test
		public void varasijaHeaderIsPrinted() throws Exception {
			assertTrue(findFromLiite(Arrays.asList("Vara-", "sija"), liite));
		}

		@Test
		public void aloituspaikatIsEmptyStringAndVarasijaIsPrinted()
				throws Exception {
			assertEquals("30", hakutoiverivi[3]);
			assertEquals(4, hakutoiverivi.length);
		}
	}

	@SuppressWarnings("unchecked")
	public static class WhenCreatingLiiteWithWithoutOnePredefinedColumn {

		private static Map<String, String> tulos = createHaku("Asentaja", "20");
		private static List<List<String>> liite;
		private static String[] hakutoiverivi;

		@BeforeClass
		public static void setUp() throws Exception {
			Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, "FI",
					Arrays.asList(tulos));
			liite = TestUtil.generateLiite(kirje);
			hakutoiverivi = findHakutoiveRivi("Asentaja", liite).split(" ");
		}

		@Test
		public void sentHeadersArePrinted() throws Exception {
			assertTrue(findFromLiite(Arrays.asList("Aloitus-", "paikat"), liite));
			assertFalse(findFromLiite(Arrays.asList("Vara-", "sija"), liite));
		}

		@Test
		public void sentColumnsArePrinted() throws Exception {
			assertEquals(tulos.get("aloituspaikat"), hakutoiverivi[3]);
			assertEquals(4, hakutoiverivi.length);
		}
	}

	private static String findHakutoiveRivi(final String hakutoive,
			List<List<String>> liite) {
		return Iterables.find(liite, new Predicate<List<String>>() {
			public boolean apply(List<String> paragraph) {
				return hakutoive.equals(Iterables.getFirst(paragraph, ""));
			}
		}).get(1);
	}

	public static boolean findFromLiite(List<String> searchParagraph,
			List<List<String>> liite) {
		for (List<String> paragraph : liite) {
			boolean found = true;
			for (String row : paragraph) {
				if (found && !searchParagraph.contains(row)) {
					found = false;
				}
			}
			if (found && searchParagraph.size() == paragraph.size()) {
				return true;
			}
		}
		return false;
	}

	private static Map<String, String> createHaku(String hakutoive,
			String aloituspaikat, String varasija) {
		Map<String, String> toive = new HashMap<String, String>();
		toive.put("koulu", "Haaga-Helia");
		toive.put("hakutoive", hakutoive);
		toive.put("ensisijaisetHakijat", "101");
		toive.put("kaikkiHakijat", "200");
		toive.put("aloituspaikat", aloituspaikat);
		toive.put("varasija", varasija);
		return toive;
	}

	private static Map<String, String> createHaku(String hakutoive,
			String aloituspaikat) {
		Map<String, String> toive = new HashMap<String, String>();
		toive.put("koulu", "Haaga-Helia");
		toive.put("hakutoive", hakutoive);
		toive.put("ensisijaisetHakijat", "101");
		toive.put("kaikkiHakijat", "200");
		toive.put("aloituspaikat", aloituspaikat);
		return toive;
	}
}
