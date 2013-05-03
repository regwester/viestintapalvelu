package fi.vm.sade.viestintapalvelu;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AddressLabelBatch {
	@XmlElement
	private String templateName;
	@XmlElement
	private List<AddressLabel> addressLabels;

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
