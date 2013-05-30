package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import fi.vm.sade.viestintapalvelu.JalkiohjauskirjeStub;
import fi.vm.sade.viestintapalvelu.PostalAddressStub;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.JalkiohjauskirjeBatch;
import fi.vm.sade.viestintapalvelu.infrastructure.AbstractBatch;

// TODO vpeurala 22.5.2013: Refactor these tests
public class JalkiohjauskirjeBatchTest {
	private List<Jalkiohjauskirje> letters;
	private JalkiohjauskirjeBatch original;
	private List<AbstractBatch<Jalkiohjauskirje>> afterSplit;

	@Before
	public void setUp() throws Exception {
		letters = new ArrayList<Jalkiohjauskirje>();
		for (int i = 0; i < 9999; i++) {
			letters.add(new JalkiohjauskirjeStub(new PostalAddressStub(
					"firstName", "lastName", "addressline", "addressline2",
					"addressline3", "postalCode", "city", "region", "country",
					"countryCode"), "FI", new ArrayList<Map<String, String>>()));

		}
		original = new JalkiohjauskirjeBatch(letters);
	}

	@Test
	public void splitWhenLimitIsMoreThanNumberOfLetters() {
		afterSplit = original.split(10000);
		assertEquals(1, afterSplit.size());
		assertEquals(original.getContents(), afterSplit.get(0).getContents());
	}

	@Test
	public void splitWhenLimitIsEqualToNumberOfLetters() {
		afterSplit = original.split(9999);
		assertEquals(1, afterSplit.size());
		assertEquals(original.getContents(), afterSplit.get(0).getContents());
	}

	@Test
	public void splitWhenLimitIsLessToNumberOfLetters() {
		afterSplit = original.split(1000);
		assertEquals(10, afterSplit.size());
		for (int i = 0; i < 10; i++) {
			AbstractBatch<Jalkiohjauskirje> current = afterSplit.get(i);
			if (i < 9) {
				// The first 9 batches contain 1000 letters each
				assertEquals(1000, current.getContents().size());
			} else {
				// The last batch contains the rest, i.e. 9999 letters
				assertEquals(999, current.getContents().size());
			}
		}
	}

	@Test
	public void splitAlsoWorksWithSubclasses() {
		JalkiohjauskirjeBatch ipostBatch = new JalkiohjauskirjeBatch(letters);
		afterSplit = ipostBatch.split(1000);
		for (int i = 0; i < 10; i++) {
			assertEquals(JalkiohjauskirjeBatch.class, afterSplit.get(i)
					.getClass());
			JalkiohjauskirjeBatch current = (JalkiohjauskirjeBatch) afterSplit
					.get(i);
			if (i < 9) {
				// The first 9 batches contain 1000 letters each
				assertEquals(1000, current.getContents().size());
			} else {
				// The last batch contains the rest, i.e. 9999 letters
				assertEquals(999, current.getContents().size());
			}
		}
	}
}
