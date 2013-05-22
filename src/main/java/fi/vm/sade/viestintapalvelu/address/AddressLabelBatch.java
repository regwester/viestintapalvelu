package fi.vm.sade.viestintapalvelu.address;

import java.util.List;

public class AddressLabelBatch {
	private List<AddressLabel> addressLabels;

	public AddressLabelBatch() {
	}

	public AddressLabelBatch(List<AddressLabel> labels) {
		this.addressLabels = labels;
	}

	public List<AddressLabel> getAddressLabels() {
		return addressLabels;
	}

	@Override
	public String toString() {
		return "AddressLabelBatch [addressLabels=" + addressLabels + "]";
	}
}
