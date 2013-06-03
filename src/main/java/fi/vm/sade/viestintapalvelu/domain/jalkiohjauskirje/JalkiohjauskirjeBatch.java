package fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje;

import java.util.ArrayList;
import java.util.List;

public abstract class JalkiohjauskirjeBatch {
	public List<JalkiohjauskirjeBatch> split(int limit) {
		List<JalkiohjauskirjeBatch> batches = new ArrayList<JalkiohjauskirjeBatch>();
		split(getLetters(), batches, limit);
		return batches;
	}

	public abstract List<Jalkiohjauskirje> getLetters();

	public void split(List<Jalkiohjauskirje> remaining,
			List<JalkiohjauskirjeBatch> batches, int limit) {
		if (limit >= remaining.size()) {
			batches.add(createSubBatch(remaining));
		} else {
			batches.add(createSubBatch(remaining.subList(0, limit)));
			split(remaining.subList(limit, remaining.size()), batches, limit);
		}
	}

	public JalkiohjauskirjeBatch createSubBatch(
			final List<Jalkiohjauskirje> batch) {
		return new JalkiohjauskirjeBatch() {
			@Override
			public List<Jalkiohjauskirje> getLetters() {
				return batch;
			}
		};
	}
}
