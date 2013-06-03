package fi.vm.sade.viestintapalvelu.test.stub;

import java.util.List;

import fi.vm.sade.viestintapalvelu.domain.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.domain.address.AddressLabelBatch;

public class AddressLabelBatchStub implements AddressLabelBatch {
	private final List<AddressLabel> addressLabels;

	public AddressLabelBatchStub(List<AddressLabel> addressLabels) {
		this.addressLabels = addressLabels;
	}

	@Override
	public List<AddressLabel> getAddressLabels() {
		return addressLabels;
	}
}
