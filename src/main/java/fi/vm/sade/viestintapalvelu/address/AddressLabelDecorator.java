package fi.vm.sade.viestintapalvelu.address;

public abstract class AddressLabelDecorator {
	
	protected AddressLabel decoratedLabel;
	
	public AddressLabelDecorator(AddressLabel addressLabel) {
		this.decoratedLabel = addressLabel;
	}

	protected String decorateAddressField(String textString) {
		return textString != null && !"".equals(textString.trim()) ? textString.replace("\n", "<br/>") : "";
	}
	
	protected String decorateCountry(String country) {
		return isDomestic(country) ? "" : country;
	}

	private boolean isDomestic(String country) {
		return country == null || country.equalsIgnoreCase("FINLAND") || country.equalsIgnoreCase("SUOMI");
	}
}
