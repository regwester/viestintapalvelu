package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;

// TODO vpeurala 22.5.2013: Refactor these tests
public class JalkiohjauskirjeBatchTest {
	private List<Jalkiohjauskirje> letters;
	private JalkiohjauskirjeBatch original;
	private List<JalkiohjauskirjeBatch> afterSplit;

	@Before
	public void setUp() throws Exception {
		letters = new ArrayList<Jalkiohjauskirje>();
		for (int i = 0; i < 9999; i++) {
			letters.add(new Jalkiohjauskirje(new AddressLabel("firstName",
					"lastName", "addressline", "addressline2", "addressline3",
					"postalCode", "city", "region", "country", "countryCode"),
					"ssn", "FI", new ArrayList<Map<String, String>>()));

		}
		original = new JalkiohjauskirjeBatch(letters);
	}

	@Test
	public void splitWhenLimitIsMoreThanNumberOfLetters() {
		afterSplit = original.split(10000);
		assertEquals(1, afterSplit.size());
		assertEquals(original.getLetters(), afterSplit.get(0).getLetters());
	}

	@Test
	public void splitWhenLimitIsEqualToNumberOfLetters() {
		afterSplit = original.split(9999);
		assertEquals(1, afterSplit.size());
		assertEquals(original.getLetters(), afterSplit.get(0).getLetters());
	}

	@Test
	public void splitWhenLimitIsLessToNumberOfLetters() {
		afterSplit = original.split(1000);
		assertEquals(10, afterSplit.size());
		for (int i = 0; i < 10; i++) {
			JalkiohjauskirjeBatch current = afterSplit.get(i);
			if (i < 9) {
				// The first 9 batches contain 1000 letters each
				assertEquals(1000, current.getLetters().size());
			} else {
				// The last batch contains the rest, i.e. 9999 letters
				assertEquals(999, current.getLetters().size());
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
				assertEquals(1000, current.getLetters().size());
			} else {
				// The last batch contains the rest, i.e. 9999 letters
				assertEquals(999, current.getLetters().size());
			}
		}
	}
}
