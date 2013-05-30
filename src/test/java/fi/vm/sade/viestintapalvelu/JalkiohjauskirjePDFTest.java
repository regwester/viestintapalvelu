package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import fi.vm.sade.viestintapalvelu.domain.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.Jalkiohjauskirje;

public class JalkiohjauskirjePDFTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static AddressLabel label = new AddressLabel(
			Fixture.addressWithSpecialCharacters);
	private static String pdf;

	@BeforeClass
	public static void setUp() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label.postalAddress(),
				"FI", new ArrayList<Map<String, String>>());
		pdf = TestUtil.generateJalkiohjauskirje(kirje).toString();
	}

	@Test
	public void isPrintedWithAddressLabel() throws Exception {
		assertTrue("Firstname Lastname missing",
				pdf.contains(label.getFirstName() + " " + label.getLastName()));
		assertTrue("Addressline missing", pdf.contains(label.getAddressline()));
		assertTrue("Addressline2 missing",
				pdf.contains(label.getAddressline2()));
		assertTrue("Addressline3 missing",
				pdf.contains(label.getAddressline3()));
		assertTrue("Postalcode city missing",
				pdf.contains(label.getPostalCode() + " " + label.getCity()));
		assertTrue("Region missing", pdf.contains(label.getRegion()));
		assertTrue("Country missing", pdf.contains(label.getCountry()));
	}

	@Test
	public void canBePrintedInEN() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label.postalAddress(),
				"EN", new ArrayList<Map<String, String>>());
		assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}

	@Test
	public void canBePrintedInSE() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label.postalAddress(),
				"SE", new ArrayList<Map<String, String>>());
		assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}

	@Test
	public void canBePrintedWithoutLanguageCode() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label.postalAddress(),
				null, new ArrayList<Map<String, String>>());
		assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}

	@Test
	public void canBePrintedInSQ() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label.postalAddress(),
				"SQ", new ArrayList<Map<String, String>>());
		assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}
}
