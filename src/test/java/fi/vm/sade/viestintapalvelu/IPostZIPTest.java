package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;

@Ignore
public class IPostZIPTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
			"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330",
			"Stockholm", "SL", "Sweden", "SE");
	private static AddressLabel labelWithSpecialCharacters = new AddressLabel(
			"Åle &", "Öistämö &", "Brännkyrksgatan & 177 B 149", "Södermalm &",
			"13&", "65330 &", "Stockholm &", "SL&", "Sweden&", "SE");
	private static byte[] zip;
	private static List<byte[]> subZips;
	private static Set<String> filenames;
	private static Document ipostXML;

	@BeforeClass
	public static void setUp() throws Exception {
		Jalkiohjauskirje kirje1 = new Jalkiohjauskirje(label,
				new ArrayList<Map<String, String>>());
		Jalkiohjauskirje kirje2 = new Jalkiohjauskirje(
				labelWithSpecialCharacters,
				new ArrayList<Map<String, String>>());
		zip = TestUtil.generateIPostZIP(Arrays.asList(kirje1, kirje2));
		subZips = extractSubZips(zip);
		System.out.println(zip.length);
		System.out.println(subZips);
		System.out.println(subZips.size());
		System.out.println(subZips.get(0).length);
		// TODO vpeurala 23.5.2013: Fix this hack
		filenames = extractFilenames(subZips.get(0));
		ipostXML = exctractIPostXML(subZips.get(0));
	}

	@Test
	public void zipContainsJalkiohjauskirjePDF() throws Exception {
		Assert.assertTrue("contains jalkiohjauskirje.pdf",
				filenames.contains("jalkiohjauskirje.pdf"));
	}

	@Test
	public void zipContainsJalkiohjauskirjeXML() throws Exception {
		Assert.assertTrue("contains jalkiohjauskirje.xml",
				filenames.contains("jalkiohjauskirje.xml"));
	}

	@Test
	public void xmlContainsStartPageAttribute() throws Exception {
		Assert.assertEquals("1", xpath("(//lb:Location)[1]/@startPage"));
		Assert.assertEquals("3", xpath("(//lb:Location)[2]/@startPage"));
	}

	@Test
	public void xmlContainsPagesAttribute() throws Exception {
		Assert.assertEquals("2", xpath("(//lb:Location)[1]/@pages"));
		Assert.assertEquals("2", xpath("(//lb:Location)[2]/@pages"));
	}

	@Test
	public void xmlContainsReceiverName() throws Exception {
		Assert.assertEquals(label.getFirstName() + " " + label.getLastName(),
				xpath("(//lb:Eu1)[1]/@name"));
		Assert.assertEquals(labelWithSpecialCharacters.getFirstName() + " "
				+ labelWithSpecialCharacters.getLastName(),
				xpath("(//lb:Eu1)[2]/@name"));
	}

	@Test
	public void xmlContainsReceiverAddress() throws Exception {
		Assert.assertEquals(label.getAddressline(),
				xpath("(//lb:Eu1)[1]/@address"));
		Assert.assertEquals(labelWithSpecialCharacters.getAddressline(),
				xpath("(//lb:Eu1)[2]/@address"));
	}

	@Test
	public void xmlContainsReceiverPostalCode() throws Exception {
		Assert.assertEquals(label.getPostalCode(),
				xpath("(//lb:Eu1)[1]/@postalCode"));
		Assert.assertEquals(labelWithSpecialCharacters.getPostalCode(),
				xpath("(//lb:Eu1)[2]/@postalCode"));
	}

	@Test
	public void xmlContainsReceiverCity() throws Exception {
		Assert.assertEquals(label.getCity(), xpath("(//lb:Eu1)[1]/@city"));
		Assert.assertEquals(labelWithSpecialCharacters.getCity(),
				xpath("(//lb:Eu1)[2]/@city"));
	}

	@Test
	public void xmlContainsReceiverCountryCode() throws Exception {
		Assert.assertEquals(label.getCountryCode(),
				xpath("(//lb:Eu1)[1]/@countryCode"));
		Assert.assertEquals(labelWithSpecialCharacters.getCountryCode(),
				xpath("(//lb:Eu1)[2]/@countryCode"));
	}

	private static List<byte[]> extractSubZips(byte[] mainZip)
			throws IOException {
		ZipInputStream in = new ZipInputStream(
				new ByteArrayInputStream(mainZip));
		ZipEntry entry;
		byte b;
		List<Byte> bytesOfCurrentEntry = new ArrayList<Byte>();
		List<byte[]> results = new ArrayList<byte[]>();
		while ((entry = in.getNextEntry()) != null) {
			System.out.println("Entry size: " + entry.getSize());
			while ((b = (byte) in.read()) != -1) {
				bytesOfCurrentEntry.add(b);
			}
			System.out.println("BytesofCurrentEntry: "
					+ bytesOfCurrentEntry.size());
			byte[] resultArray = new byte[bytesOfCurrentEntry.size()];
			for (int i = 0; i < bytesOfCurrentEntry.size(); i++) {
				resultArray[i] = bytesOfCurrentEntry.get(i);
			}
			results.add(resultArray);
			in.closeEntry();
		}
		in.close();
		return results;

	}

	private static Set<String> extractFilenames(byte[] zip) throws IOException {
		System.out.println("extractFilenames: " + zip.length);
		ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zip));
		Set<String> fileNames = new HashSet<String>();
		ZipEntry entry;
		while ((entry = in.getNextEntry()) != null) {
			fileNames.add(entry.getName());
			in.closeEntry();
		}
		in.close();
		return fileNames;
	}

	private static String xpath(String selector) {
		Map<String, String> namespaceUris = new HashMap<String, String>();
		namespaceUris.put("lb",
				"urn:itella.com:/schema/ipost/letterbundle/v1x0");
		XPath xPath = DocumentHelper.createXPath(selector);
		xPath.setNamespaceURIs(namespaceUris);
		return xPath.selectSingleNode(ipostXML).getText();
	}

	private static Document exctractIPostXML(byte[] zip2) throws IOException,
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
