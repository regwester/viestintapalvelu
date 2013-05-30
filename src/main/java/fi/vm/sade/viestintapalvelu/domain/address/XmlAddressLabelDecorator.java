package fi.vm.sade.viestintapalvelu.domain.address;

import org.apache.commons.lang.StringEscapeUtils;

public class XmlAddressLabelDecorator extends PostalAddressDecorator {

	public XmlAddressLabelDecorator(PostalAddress postalAddress) {
		super(postalAddress);
	}

	public String getFirstName() {
		return escape(decoratedPostalAddress.getFirstName());
	}

	public String getLastName() {
		return escape(decoratedPostalAddress.getLastName());
	}

	public String getAddressline() {
		return decorateAddressField(decoratedPostalAddress.getAddressline());
	}

	public String getAddressline2() {
		return decorateAddressField(decoratedPostalAddress.getAddressline2());
	}

	public String getAddressline3() {
		return decorateAddressField(decoratedPostalAddress.getAddressline3());
	}

	public String getPostalCode() {
		return escape(decoratedPostalAddress.getPostalCode());
	}

	public String getCity() {
		return escape(decoratedPostalAddress.getCity());
	}

	public String getRegion() {
		return escape(decoratedPostalAddress.getRegion());
	}

	public String getCountry() {
		return decorateCountry(decoratedPostalAddress.getCountry());
	}

	public String getCountryCode() {
		return escape(decoratedPostalAddress.getCountryCode());
	}

	@Override
	protected String escape(String text) {
		return StringEscapeUtils.escapeXml(text);
	}
}
