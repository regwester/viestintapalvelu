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

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<AddressLabel> getAddressLabels() {
		return addressLabels;
	}

	public void setAddressLabels(List<AddressLabel> addressLabels) {
		this.addressLabels = addressLabels;
	}
}
