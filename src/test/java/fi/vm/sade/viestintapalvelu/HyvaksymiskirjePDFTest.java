package fi.vm.sade.viestintapalvelu;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.hyvaksymiskirje.Hyvaksymiskirje;

public class HyvaksymiskirjePDFTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static AddressLabel label = new AddressLabel("Åle &", "Öistämö",
			"Brännkyrksgatan @ 177 B 149", "Södermalm", "13", "65330", "Stockholm", "SL", "Sweden");
	private static String koulu = "Haaga & Helia";
	private static String koulutus = "Asentaja & Kokki";
	private static String pdf;

	@BeforeClass
	public static void setUp() throws Exception {
		Hyvaksymiskirje kirje = new Hyvaksymiskirje(label, koulu, koulutus, new ArrayList<Map<String,String>>());
		pdf = TestUtil.generateHyvaksymiskirje(kirje).toString();
	}

	@Test
	public void containsAddressLabel() throws Exception {
		Assert.assertTrue("Firstname Lastname missing", pdf.contains(label.getFirstName() + " " + label.getLastName()));
		Assert.assertTrue("Addressline missing", pdf.contains(label.getAddressline()));
		Assert.assertTrue("Addressline2 missing", pdf.contains(label.getAddressline2()));
		Assert.assertTrue("Addressline3 missing", pdf.contains(label.getAddressline3()));
		Assert.assertTrue("Postalcode city missing", pdf.contains(label.getPostalCode() + " " + label.getCity()));
		Assert.assertTrue("Region missing", pdf.contains(label.getRegion()));
		Assert.assertTrue("Country missing", pdf.contains(label.getCountry()));
	}

	@Test
	public void containsSchool() throws Exception {
		Assert.assertTrue("Hyväksytty koulu puuttuu", pdf.contains(koulu));
	}

	@Test
	public void contains() throws Exception {
		Assert.assertTrue("Hyväksytty koulutus puuttuu", pdf.contains(koulutus));
	}
}
