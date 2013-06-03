package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.test.stub.JalkiohjauskirjeStub;

public class JalkiohjauskirjePDFTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static PostalAddress label = Fixture.addressWithSpecialCharacters;
	private static String pdf;

	@Before
	public void setUp() throws Exception {
		final List<Map<String, String>> hakutulokset = new ArrayList<Map<String, String>>();
		Jalkiohjauskirje kirje = new JalkiohjauskirjeStub(label, "EN",
				hakutulokset);
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
		Jalkiohjauskirje kirje = new JalkiohjauskirjeStub(label, "EN",
				new ArrayList<Map<String, String>>());
		assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}

	@Test
	public void canBePrintedInSE() throws Exception {
		Jalkiohjauskirje kirje = new JalkiohjauskirjeStub(label, "SE",
				new ArrayList<Map<String, String>>());
		assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}

	@Test
	public void canBePrintedWithoutLanguageCode() throws Exception {
		Jalkiohjauskirje kirje = new JalkiohjauskirjeStub(label, null,
				new ArrayList<Map<String, String>>());
		assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}

	@Test
	public void canBePrintedInSQ() throws Exception {
		Jalkiohjauskirje kirje = new JalkiohjauskirjeStub(label, "SQ",
				new ArrayList<Map<String, String>>());
		assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
	}
}
