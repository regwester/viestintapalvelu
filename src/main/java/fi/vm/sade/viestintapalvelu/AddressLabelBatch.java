package fi.vm.sade.viestintapalvelu;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

// TODO vpeurala 6.5.2013: Do this without annotations
@XmlRootElement
public class AddressLabelBatch {
	@XmlElement
	private String templateName;
	@XmlElement
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
