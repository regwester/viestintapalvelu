package fi.vm.sade.viestintapalvelu.domain.address;

import org.apache.commons.lang.StringEscapeUtils;

public class HtmlAddressLabelDecorator extends AddressLabelDecorator {

	public HtmlAddressLabelDecorator(AddressLabel addressLabel) {
		super(addressLabel);
	}

	private String decorateAddressline(String textString) {
		return textString != null && !"".equals(textString.trim()) ? decorateAddressField(textString)
				+ "<br/>"
				: "";
	}

	public String getAddress() {
		return decorateAddressline(decoratedLabel.getFirstName() + " "
				+ decoratedLabel.getLastName())
				+ decorateAddressline(decoratedLabel.getAddressline())
				+ decorateAddressline(decoratedLabel.getAddressline2())
				+ decorateAddressline(decoratedLabel.getAddressline3())
				+ decorateAddressline(decoratedLabel.getPostalCode() + " "
						+ decoratedLabel.getCity())
				+ decorateAddressline(decoratedLabel.getRegion())
				+ decorateCountry(decoratedLabel.getCountry());
	}

	public String toString() {
		return getAddress();
	}

	@Override
	protected String escape(String text) {
		return StringEscapeUtils.escapeHtml(text);
	}
}
