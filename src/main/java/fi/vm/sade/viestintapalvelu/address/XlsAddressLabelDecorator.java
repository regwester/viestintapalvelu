package fi.vm.sade.viestintapalvelu.address;


public class XlsAddressLabelDecorator extends AddressLabelDecorator {
	
	public XlsAddressLabelDecorator(AddressLabel addressLabel) {
		super(addressLabel);
	}
	
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
}
