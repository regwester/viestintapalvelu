package fi.vm.sade.viestintapalvelu.address;


public class DocxAddressLabelDecorator extends AddressLabelDecorator {
	
	public DocxAddressLabelDecorator(AddressLabel addressLabel) {
		super(addressLabel);
	}
	
	protected String decorateAddressline(String textString) {
		return textString != null && !"".equals(textString.trim()) ? textString.trim() + "\n" : "";
	}
	
	protected String decorateAddressField(String textString) {
		return textString != null && !"".equals(textString.trim()) ? textString.trim() : "";
	}
	
	public String toString() {
		return getAddress();
	}
}
