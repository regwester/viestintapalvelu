package fi.vm.sade.viestintapalvelu.address;

public abstract class AddressLabelDecorator {
	
	private AddressLabel decoratedLabel;
	
	public AddressLabelDecorator(AddressLabel addressLabel) {
		this.decoratedLabel = addressLabel;
	}

	protected abstract String decorateAddressline(String text);
	protected abstract String decorateAddressField(String text);

	public String getFirstName() {
		return decoratedLabel.getFirstName();
	}

	public String getLastName() {
		return decoratedLabel.getLastName();
	}

	public String getAddressline() {
		return decorateAddressField(decoratedLabel.getAddressline());
	}

	public String getAddressline2() {
		return decorateAddressField(decoratedLabel.getAddressline2());
	}

	public String getAddressline3() {
		return decorateAddressField(decoratedLabel.getAddressline3());
	}

	public String getPostalCode() {
		return decoratedLabel.getPostalCode();
	}

	public String getCity() {
		return decoratedLabel.getCity();
	}

	public String getRegion() {
		return decoratedLabel.getRegion();
	}

	public String getCountry() {
		return decorateCountry(decoratedLabel.getCountry());
	}

	public String getAddress() {
		return decorateAddressline(decoratedLabel.getFirstName() + " " + decoratedLabel.getLastName()) +
				decorateAddressline(decoratedLabel.getAddressline()) +
				decorateAddressline(decoratedLabel.getAddressline2()) +
				decorateAddressline(decoratedLabel.getAddressline3()) +
				decorateAddressline(decoratedLabel.getPostalCode() + " " + decoratedLabel.getCity()) +
				decorateAddressline(decoratedLabel.getRegion()) +
				decorateCountry(decoratedLabel.getCountry());
	}

	private String decorateCountry(String country) {
		return isDomestic(country) ? "" : country;
	}

	private boolean isDomestic(String country) {
		return country == null || country.equalsIgnoreCase("FINLAND") || country.equalsIgnoreCase("SUOMI");
	}
}
