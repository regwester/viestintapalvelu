package fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje;

import java.util.ArrayList;
import java.util.List;

import fi.vm.sade.viestintapalvelu.application.Batch;
import fi.vm.sade.viestintapalvelu.application.SplittableBatch;
import fi.vm.sade.viestintapalvelu.infrastructure.JalkiohjauskirjeBatchStub;

public abstract class JalkiohjauskirjeBatch implements
		SplittableBatch<Jalkiohjauskirje> {
	@Override
	public List<Batch<Jalkiohjauskirje>> split(int limit) {
		List<Batch<Jalkiohjauskirje>> batches = new ArrayList<Batch<Jalkiohjauskirje>>();
		split(getLetters(), batches, limit);
		return batches;
	}

	@Override
	public abstract List<Jalkiohjauskirje> getLetters();

	private void split(List<Jalkiohjauskirje> remaining,
			List<Batch<Jalkiohjauskirje>> batches, int limit) {
		if (limit >= remaining.size()) {
			batches.add(createSubBatch(remaining));
		} else {
			batches.add(createSubBatch(remaining.subList(0, limit)));
			split(remaining.subList(limit, remaining.size()), batches, limit);
		}
	}

	private Batch<Jalkiohjauskirje> createSubBatch(List<Jalkiohjauskirje> batch) {
		return new JalkiohjauskirjeBatchStub(batch);
	}
}
