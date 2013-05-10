package fi.vm.sade.viestintapalvelu;

import java.util.List;

public class AddressLabelBatch {
	private String templateName;
	private List<AddressLabel> addressLabels;

	public AddressLabelBatch() {
	}

	public AddressLabelBatch(String templateName, List<AddressLabel> labels) {
		this.templateName = templateName;
		this.addressLabels = labels;
	}

	public String getTemplateName() {
		return templateName;
	}

	public List<AddressLabel> getAddressLabels() {
		return addressLabels;
	}

	@Override
	public String toString() {
		return "AddressLabelBatch [templateName=" + templateName
				+ ", addressLabels=" + addressLabels + "]";
	}
}
