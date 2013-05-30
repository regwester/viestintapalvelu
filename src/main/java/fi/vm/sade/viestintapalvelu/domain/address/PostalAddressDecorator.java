package fi.vm.sade.viestintapalvelu.domain.address;

public abstract class PostalAddressDecorator {
	protected PostalAddress decoratedPostalAddress;

	public PostalAddressDecorator(PostalAddress postalAddress) {
		this.decoratedPostalAddress = postalAddress;
	}

	protected abstract String escape(String text);

	protected String decorateAddressField(String textString) {
		return textString != null && !"".equals(textString.trim()) ? escape(
				textString).replace("\n", "<br/>") : "";
	}

	protected String decorateCountry(String country) {
		return isDomestic(country) ? "" : escape(country);
	}

	private boolean isDomestic(String country) {
		return country == null || country.equalsIgnoreCase("FINLAND")
				|| country.equalsIgnoreCase("SUOMI");
	}
}
