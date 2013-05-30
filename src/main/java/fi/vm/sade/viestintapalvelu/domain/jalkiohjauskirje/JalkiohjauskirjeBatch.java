package fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje;

import java.util.List;

import fi.vm.sade.viestintapalvelu.infrastructure.Batch;

public class JalkiohjauskirjeBatch extends Batch<Jalkiohjauskirje> {
	public JalkiohjauskirjeBatch() {
		super();
	}

	public JalkiohjauskirjeBatch(List<Jalkiohjauskirje> contents) {
		super(contents);
	}

	@Override
	protected Batch<Jalkiohjauskirje> createSubBatch(
			List<Jalkiohjauskirje> contentsOfSubBatch) {
		return new JalkiohjauskirjeBatch(contentsOfSubBatch);
	}
}
