package fi.vm.sade.viestintapalvelu.domain.address;

import java.util.List;

import fi.vm.sade.viestintapalvelu.infrastructure.Batch;

public class AddressLabelBatch extends Batch<AddressLabel> {
	public AddressLabelBatch() {
		super();
	}

	public AddressLabelBatch(List<AddressLabel> labels) {
		super(labels);
	}

	public List<AddressLabel> getAddressLabels() {
		return getContents();
	}

	@Override
	protected Batch<AddressLabel> createSubBatch(
			List<AddressLabel> contentsOfSubBatch) {
		return new AddressLabelBatch(contentsOfSubBatch);
	}

	@Override
	public String toString() {
		return "AddressLabelBatch [addressLabels=" + getContents() + "]";
	}
}
