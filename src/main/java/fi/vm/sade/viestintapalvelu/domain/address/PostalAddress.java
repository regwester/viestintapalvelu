package fi.vm.sade.viestintapalvelu.domain.address;

public interface PostalAddress {
	String getFirstName();

	String getLastName();

	String getAddressline();

	String getAddressline2();

	String getAddressline3();

	String getPostalCode();

	String getCity();

	String getRegion();

	String getCountry();

	String getCountryCode();
}
