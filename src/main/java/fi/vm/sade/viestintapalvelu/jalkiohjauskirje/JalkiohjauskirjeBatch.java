package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import java.util.ArrayList;
import java.util.List;

public class JalkiohjauskirjeBatch {
	private List<Jalkiohjauskirje> letters;

	public JalkiohjauskirjeBatch() {
	}

	public JalkiohjauskirjeBatch(List<Jalkiohjauskirje> letters) {
		this.letters = letters;
	}

	public List<Jalkiohjauskirje> getLetters() {
		return letters;
	}

	@Override
	public String toString() {
		return "JalkiohjauskirjeBatch [letters=" + letters + "]";
	}

	public List<JalkiohjauskirjeBatch> split(int limit) {
		List<JalkiohjauskirjeBatch> batches = new ArrayList<JalkiohjauskirjeBatch>();
		split(letters, batches, limit);
		return batches;
	}

	protected JalkiohjauskirjeBatch createSubBatch(
			List<Jalkiohjauskirje> lettersOfSubBatch) {
		return new JalkiohjauskirjeBatch(new ArrayList<Jalkiohjauskirje>(lettersOfSubBatch));
	}

	private void split(List<Jalkiohjauskirje> remaining,
			List<JalkiohjauskirjeBatch> batches, int limit) {
		if (limit >= remaining.size()) {
			batches.add(createSubBatch(remaining));
		} else {
			batches.add(createSubBatch(new ArrayList<Jalkiohjauskirje>(
					remaining.subList(0, limit))));
			split(remaining.subList(limit, remaining.size()), batches, limit);
		}
	}
}
