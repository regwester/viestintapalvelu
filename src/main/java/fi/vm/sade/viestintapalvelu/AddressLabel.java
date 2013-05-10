package fi.vm.sade.viestintapalvelu;

import org.codehaus.jackson.annotate.JsonIgnore;


public class AddressLabel {
	public AddressLabel() {
	}

	public AddressLabel(String firstName, String lastName,
			String addressline, String addressline2, String postalCode, String postOffice,
			String county, String country) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.addressline = addressline;
		this.addressline2 = addressline2;
		this.postalCode = postalCode;
		this.postOffice = postOffice;
		this.county = county;
		this.country = country;
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
	 * Postinumero. Esim. "00500".
	 */
	private String postalCode;
	/**
	 * Postitoimipaikka. Esim. "Helsinki".
	 */
	private String postOffice;
	/**
	 * Maakunta ulkomaalaisille osoitteille.
	 */
	private String county;
	/**
	 * Maa, jos muu kuin Suomi. Esim. "Sweden".
	 */
	private String country;

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

	public String getPostalCode() {
		return postalCode;
	}

	public String getPostOffice() {
		return postOffice;
	}

	public String getCounty() {
		return county;
	}

	public String getCountry() {
		return country;
	}

	@JsonIgnore
	public String getUpperCaseCountry() {
		return country == null ? "" : country.toUpperCase();
	}

	@Override
	public String toString() {
		return "AddressLabel [firstName=" + firstName + ", lastName="
				+ lastName + ", addressline=" + addressline + ", addressline2=" + addressline2
				+ ", postalCode=" + postalCode + ", postOffice=" + postOffice
				+ ", county=" + county + ", country=" + country + "]";
	}

}
