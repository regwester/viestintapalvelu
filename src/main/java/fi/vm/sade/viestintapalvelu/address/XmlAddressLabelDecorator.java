package fi.vm.sade.viestintapalvelu.address;

import org.apache.commons.lang.StringEscapeUtils;

public class XmlAddressLabelDecorator extends AddressLabelDecorator {

	public XmlAddressLabelDecorator(AddressLabel addressLabel) {
		super(addressLabel);
	}

	public String getFirstName() {
		return escape(decoratedLabel.getFirstName());
	}

	public String getLastName() {
		return escape(decoratedLabel.getLastName());
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
		return escape(decoratedLabel.getPostalCode());
	}

	public String getCity() {
		return escape(decoratedLabel.getCity());
	}

	public String getRegion() {
		return escape(decoratedLabel.getRegion());
	}

	public String getCountry() {
		return decorateCountry(decoratedLabel.getCountry());
	}

	public String getCountryCode() {
		return escape(decoratedLabel.getCountryCode());
	}

	@Override
	protected String escape(String text) {
		return StringEscapeUtils.escapeXml(text);
	}
}
