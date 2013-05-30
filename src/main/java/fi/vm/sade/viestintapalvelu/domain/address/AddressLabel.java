package fi.vm.sade.viestintapalvelu.domain.address;

public class AddressLabel implements HasPostalAddress {
	// FIXME vpeurala 30.5.2013: Remove initial assignment
	private PostalAddress postalAddress = new PostalAddress();

	public AddressLabel(PostalAddress postalAddress) {
		this.postalAddress = postalAddress;
	}

	// FIXME vpeurala 30.5.2013: Remove delegate methods
	public String getFirstName() {
		return postalAddress.getFirstName();
	}

	public String getLastName() {
		return postalAddress.getLastName();
	}

	public String getAddressline() {
		return postalAddress.getAddressline();
	}

	public String getAddressline2() {
		return postalAddress.getAddressline2();
	}

	public String getAddressline3() {
		return postalAddress.getAddressline3();
	}

	public String getPostalCode() {
		return postalAddress.getPostalCode();
	}

	public String getCity() {
		return postalAddress.getCity();
	}

	public String getRegion() {
		return postalAddress.getRegion();
	}

	public String getCountry() {
		return postalAddress.getCountry();
	}

	public String getCountryCode() {
		return postalAddress.getCountryCode();
	}

	@Override
	public PostalAddress postalAddress() {
		return postalAddress;
	}

	public void setFirstName(String firstName) {
		postalAddress.setFirstName(firstName);
	}

	public void setLastName(String lastName) {
		postalAddress.setLastName(lastName);
	}

	public void setAddressline(String addressline) {
		postalAddress.setAddressline(addressline);
	}

	public void setAddressline2(String addressline2) {
		postalAddress.setAddressline2(addressline2);
	}

	public void setAddressline3(String addressline3) {
		postalAddress.setAddressline3(addressline3);
	}

	public void setPostalCode(String postalCode) {
		postalAddress.setPostalCode(postalCode);
	}

	public void setCity(String city) {
		postalAddress.setCity(city);
	}

	public void setRegion(String region) {
		postalAddress.setRegion(region);
	}

	public void setCountry(String country) {
		postalAddress.setCountry(country);
	}

	public void setCountryCode(String countryCode) {
		postalAddress.setCountryCode(countryCode);
	}
}
