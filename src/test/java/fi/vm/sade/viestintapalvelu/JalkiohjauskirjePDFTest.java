package fi.vm.sade.viestintapalvelu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;

public class JalkiohjauskirjePDFTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
			"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm", "SL", "Sweden");
	private static List<String> pdfLabel;

	@BeforeClass
	public static void setUp() throws Exception {
		Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, new ArrayList<Map<String,String>>());
		List<List<String>> pdf = TestUtil.generateJalkiohjauskirje(kirje);
		pdfLabel = findAddressLabel(pdf);
	}

	@Test
	public void isPrintedWithAddressLabel() throws Exception {
		Assert.assertEquals(label.getFirstName() + " " + label.getLastName(), pdfLabel.get(0));
		Assert.assertEquals(label.getAddressline(), pdfLabel.get(1));
		Assert.assertEquals(label.getAddressline2(), pdfLabel.get(2));
		Assert.assertEquals(label.getAddressline3(), pdfLabel.get(3));
		Assert.assertEquals(label.getPostalCode() + " " + label.getCity(), pdfLabel.get(4));
		Assert.assertEquals(label.getRegion(), pdfLabel.get(5));
		Assert.assertEquals(label.getCountry(), pdfLabel.get(6));
	}

	private static List<String> findAddressLabel(List<List<String>> pdf) {
		List<String> address = Iterables.find(pdf, new Predicate<List<String>>() {
			public boolean apply(List<String> element) {
				return element.size() > 0 && element.get(0).startsWith("Priority PP Finlande");
			}
		});
		return address.subList(1, address.size());
	}
}
