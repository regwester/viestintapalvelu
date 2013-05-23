package fi.vm.sade.viestintapalvelu;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.hyvaksymiskirje.Hyvaksymiskirje;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;

public class JalkiohjauskirjePDFTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static AddressLabel label = new AddressLabel("Åle &", "Öistämö",
			"Brännkyrksgatan @ 177 B 149", "Södermalm", "13", "65330", "Stockholm", "SL", "Sweden", "SE");
	private static String pdf;

	@BeforeClass
	public static void setUp() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, "FI", new ArrayList<Map<String,String>>());
		pdf = TestUtil.generateJalkiohjauskirje(kirje).toString();
	}

	@Test
	public void isPrintedWithAddressLabel() throws Exception {
		Assert.assertTrue("Firstname Lastname missing", pdf.contains(label.getFirstName() + " " + label.getLastName()));
		Assert.assertTrue("Addressline missing", pdf.contains(label.getAddressline()));
		Assert.assertTrue("Addressline2 missing", pdf.contains(label.getAddressline2()));
		Assert.assertTrue("Addressline3 missing", pdf.contains(label.getAddressline3()));
		Assert.assertTrue("Postalcode city missing", pdf.contains(label.getPostalCode() + " " + label.getCity()));
		Assert.assertTrue("Region missing", pdf.contains(label.getRegion()));
		Assert.assertTrue("Country missing", pdf.contains(label.getCountry()));
	}
	
	@Test
	public void canBePrintedInEN() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, "EN", new ArrayList<Map<String,String>>());
		Assert.assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}

	@Test
	public void canBePrintedInSE() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, "SE", new ArrayList<Map<String,String>>());
		Assert.assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}

	@Test
	public void canBePrintedWithoutLanguageCode() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, null, new ArrayList<Map<String,String>>());
		Assert.assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}

	@Test
	public void canBePrintedInSQ() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, "SQ", new ArrayList<Map<String,String>>());
		Assert.assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}

}
