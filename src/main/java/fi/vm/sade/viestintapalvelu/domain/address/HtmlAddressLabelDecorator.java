package fi.vm.sade.viestintapalvelu.domain.address;

import org.apache.commons.lang.StringEscapeUtils;

public class HtmlAddressLabelDecorator extends PostalAddressDecorator {

	public HtmlAddressLabelDecorator(PostalAddress postalAddress) {
		super(postalAddress);
		System.out.println("HtmlAddressLabelDecorator ctor: " + postalAddress);
		if (postalAddress == null) {
			throw new RuntimeException(
					"Null postal address in HtmlAddressLabelDecorator!");
		}
	}

	private String decorateAddressline(String textString) {
		return textString != null && !"".equals(textString.trim()) ? decorateAddressField(textString)
				+ "<br/>"
				: "";
	}

	public String getAddress() {
		return decorateAddressline(decoratedPostalAddress.getFirstName() + " "
				+ decoratedPostalAddress.getLastName())
				+ decorateAddressline(decoratedPostalAddress.getAddressline())
				+ decorateAddressline(decoratedPostalAddress.getAddressline2())
				+ decorateAddressline(decoratedPostalAddress.getAddressline3())
				+ decorateAddressline(decoratedPostalAddress.getPostalCode()
						+ " " + decoratedPostalAddress.getCity())
				+ decorateAddressline(decoratedPostalAddress.getRegion())
				+ decorateCountry(decoratedPostalAddress.getCountry());
	}

	public String toString() {
		return getAddress();
	}

	@Override
	protected String escape(String text) {
		return StringEscapeUtils.escapeHtml(text);
	}
}
