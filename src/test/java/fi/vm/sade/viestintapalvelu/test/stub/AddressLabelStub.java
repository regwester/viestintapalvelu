package fi.vm.sade.viestintapalvelu.test.stub;

import fi.vm.sade.viestintapalvelu.domain.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;

public class AddressLabelStub implements AddressLabel {
	private final PostalAddress input;

	public AddressLabelStub(PostalAddress input) {
		this.input = input;
	}

	public AddressLabelStub(String firstName, String lastName,
			String addressline, String addressline2, String addressline3,
			String postalCode, String city, String region, String country,
			String countryCode) {
		input = new PostalAddressStub(firstName, lastName, addressline,
				addressline2, addressline3, postalCode, city, region, country,
				countryCode);
	}

	@Override
	public String getFirstName() {
		return input.getFirstName();
	}

	@Override
	public String getLastName() {
		return input.getLastName();
	}

	@Override
	public String getAddressline() {
		return input.getAddressline();
	}

	@Override
	public String getAddressline2() {
		return input.getAddressline2();
	}

	@Override
	public String getAddressline3() {
		return input.getAddressline3();
	}

	@Override
	public String getPostalCode() {
		return input.getPostalCode();
	}

	@Override
	public String getCity() {
		return input.getCity();
	}

	@Override
	public String getRegion() {
		return input.getRegion();
	}

	@Override
	public String getCountry() {
		return input.getCountry();
	}

	@Override
	public String getCountryCode() {
		return input.getCountryCode();
	}
}