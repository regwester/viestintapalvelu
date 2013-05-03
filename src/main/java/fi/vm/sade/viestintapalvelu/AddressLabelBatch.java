package fi.vm.sade.viestintapalvelu;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AddressLabelBatch {
	private String templateName;
	private List<AddressLabel> addressLabels;

	public String getTemplateName() {
		return templateName;
	}

	public List<AddressLabel> getAddressLabels() {
		return addressLabels;
	}
}
