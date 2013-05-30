package fi.vm.sade.viestintapalvelu.domain.address;

public class PostalAddress {
	// FIXME vpeurala 30.5.2013: Remove if possible
	public PostalAddress() {
	}

	public PostalAddress(String firstName, String lastName, String addressline,
			String addressline2, String addressline3, String postalCode,
			String city, String region, String country, String countryCode) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.addressline = addressline;
		this.addressline2 = addressline2;
		this.addressline3 = addressline3;
		this.postalCode = postalCode;
		this.city = city;
		this.region = region;
		this.country = country;
		this.countryCode = countryCode;
	}

	/**
	 * Etunimi. Esim. "Ville".
	 */
	private String firstName;
	/**
	 * Sukunimi. Esim. "Peurala".
	 */
	private String lastName;

	/**
	 * Katuosoite. Esim. "Pengerkatu 20 B 27".
	 */
	private String addressline;
	/**
	 * Toinen osoiterivi ulkomaalaisille osoitteille.
	 */
	private String addressline2;
	/**
	 * Kolmas osoiterivi ulkomaalaisille osoitteille.
	 */
	private String addressline3;
	/**
	 * Postinumero. Esim. "00500".
	 */
	private String postalCode;
	/**
	 * Postitoimipaikka. Esim. "Helsinki".
	 */
	private String city;
	/**
	 * Maakunta ulkomaalaisille osoitteille.
	 */
	private String region;
	/**
	 * Maa, jos muu kuin Suomi. Esim. "Sweden".
	 */
	private String country;
	/**
	 * Maakoodi ISO3166.
	 */
	private String countryCode;

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getAddressline() {
		return addressline;
	}

	public String getAddressline2() {
		return addressline2;
	}

	public String getAddressline3() {
		return addressline3;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCity() {
		return city;
	}

	public String getRegion() {
		return region;
	}

	public String getCountry() {
		return country;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setAddressline(String addressline) {
		this.addressline = addressline;
	}

	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}

	public void setAddressline3(String addressline3) {
		this.addressline3 = addressline3;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public String toString() {
		return "PostalAddress [firstName=" + firstName + ", lastName="
				+ lastName + ", addressline=" + addressline + ", addressline2="
				+ addressline2 + ", addressline3=" + addressline3
				+ ", postalCode=" + postalCode + ", city=" + city + ", region="
				+ region + ", country=" + country + ", countryCode="
				+ countryCode + "]";
	}

	// FIXME vpeurala 30.5.2013: Remove setters

}
