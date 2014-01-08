package fi.vm.sade.viestintapalvelu.address;

public abstract class AddressLabelDecorator {

    protected AddressLabel decoratedLabel;

    public AddressLabelDecorator(AddressLabel addressLabel) {
        this.decoratedLabel = addressLabel;
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
