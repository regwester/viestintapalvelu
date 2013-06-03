package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.test.stub.JalkiohjauskirjeStub;

public class IPostZIPTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static Set<String> filenames;
	private static Document ipostXML;
	private static PostalAddress label = Fixture.address;
	private static PostalAddress labelWithSpecialCharacters = Fixture.addressWithSpecialCharacters;

	@BeforeClass
	public static void setUp() throws Exception {
		Jalkiohjauskirje kirje1 = new JalkiohjauskirjeStub(Fixture.address,
				"FI", new ArrayList<Map<String, String>>());
		Jalkiohjauskirje kirje2 = new JalkiohjauskirjeStub(
				Fixture.addressWithSpecialCharacters, "FI",
				new ArrayList<Map<String, String>>());
		byte[] mainZip = TestUtil.generateIPostZIP(Arrays
				.asList(kirje1, kirje2));
		Map<String, byte[]> zipEntryNamesAndContents = ZipUtil
				.zipEntryNamesAndContents(mainZip);
		byte[] zip = zipEntryNamesAndContents.get("jalkiohjauskirje_1.zip");
		Map<String, byte[]> subZipEntryNamesAndContents = ZipUtil
				.zipEntryNamesAndContents(zip);
		filenames = subZipEntryNamesAndContents.keySet();
		ipostXML = exctractIPostXML(zip);
	}

	@Test
	public void zipContainsJalkiohjauskirjePDF() throws Exception {
		assertTrue("contains jalkiohjauskirje.pdf",
				filenames.contains("jalkiohjauskirje.pdf"));
	}

	@Test
	public void zipContainsJalkiohjauskirjeXML() throws Exception {
		assertTrue("contains jalkiohjauskirje.xml",
				filenames.contains("jalkiohjauskirje.xml"));
	}

	@Test
	public void xmlContainsStartPageAttribute() throws Exception {
		assertEquals("1", xpath("(//lb:Location)[1]/@startPage"));
		assertEquals("3", xpath("(//lb:Location)[2]/@startPage"));
	}

	@Test
	public void xmlContainsPagesAttribute() throws Exception {
		assertEquals("2", xpath("(//lb:Location)[1]/@pages"));
		assertEquals("2", xpath("(//lb:Location)[2]/@pages"));
	}

	@Test
	public void xmlContainsReceiverName() throws Exception {
		assertEquals(label.getFirstName() + " " + label.getLastName(),
				xpath("(//lb:Eu1)[1]/@name"));
		assertEquals(labelWithSpecialCharacters.getFirstName() + " "
				+ labelWithSpecialCharacters.getLastName(),
				xpath("(//lb:Eu1)[2]/@name"));
	}

	@Test
	public void xmlContainsReceiverAddress() throws Exception {
		assertEquals(label.getAddressline(), xpath("(//lb:Eu1)[1]/@address"));
		assertEquals(labelWithSpecialCharacters.getAddressline(),
				xpath("(//lb:Eu1)[2]/@address"));
	}

	@Test
	public void xmlContainsReceiverPostalCode() throws Exception {
		assertEquals(label.getPostalCode(), xpath("(//lb:Eu1)[1]/@postalCode"));
		assertEquals(labelWithSpecialCharacters.getPostalCode(),
				xpath("(//lb:Eu1)[2]/@postalCode"));
	}

	@Test
	public void xmlContainsReceiverCity() throws Exception {
		assertEquals(label.getCity(), xpath("(//lb:Eu1)[1]/@city"));
		assertEquals(labelWithSpecialCharacters.getCity(),
				xpath("(//lb:Eu1)[2]/@city"));
	}

	@Test
	public void xmlContainsReceiverCountryCode() throws Exception {
		assertEquals(label.getCountryCode(),
				xpath("(//lb:Eu1)[1]/@countryCode"));
		assertEquals(labelWithSpecialCharacters.getCountryCode(),
				xpath("(//lb:Eu1)[2]/@countryCode"));
	}

	private static String xpath(String selector) {
		Map<String, String> namespaceUris = new HashMap<String, String>();
		namespaceUris.put("lb",
				"urn:itella.com:/schema/ipost/letterbundle/v1x0");
		XPath xPath = DocumentHelper.createXPath(selector);
		xPath.setNamespaceURIs(namespaceUris);
		return xPath.selectSingleNode(ipostXML).getText();
	}

	private static Document exctractIPostXML(byte[] zip) throws IOException,
			DocumentException {
		ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zip));
		ZipEntry entry;
		while ((entry = in.getNextEntry()) != null) {
			if ("jalkiohjauskirje.xml".equals(entry.getName())) {
				SAXReader reader = new SAXReader();
				Document doc = reader.read(in);
				in.close();
				return doc;
			}
		}
		in.close();
		return null;
	}
}
