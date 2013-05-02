package fi.vm.sade.viestintapalvelu;

public class AddressLabel {
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
	private String streetAddress;
	/**
	 * Postinumero. Esim. "00500".
	 */
	private String postalCode;
	/**
	 * Postitoimipaikka. Esim. "Helsinki".
	 */
	private String postOffice;
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

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getPostOffice() {
		return postOffice;
	}

	public String getCountry() {
		return country;
	}
}
