package fi.vm.sade.viestintapalvelu.address;


public class HtmlAddressLabelDecorator extends AddressLabelDecorator {
	
	public HtmlAddressLabelDecorator(AddressLabel addressLabel) {
		super(addressLabel);
	}
	
	protected String decorateAddressline(String textString) {
		return textString != null && !"".equals(textString.trim()) ? decorateAddressField(textString) + "<br/>" : "";
	}
	
	protected String decorateAddressField(String textString) {
		return textString != null && !"".equals(textString.trim()) ? textString.replace("\n", "<br/>") : "";
	}
	
	public String toString() {
		return getAddress();
	}
}
