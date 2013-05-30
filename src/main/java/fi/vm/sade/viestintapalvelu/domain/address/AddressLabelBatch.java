package fi.vm.sade.viestintapalvelu.domain.address;

import java.util.List;

import fi.vm.sade.viestintapalvelu.application.Batch;

public interface AddressLabelBatch extends Batch<AddressLabel> {
	public List<AddressLabel> getAddressLabels();
}
