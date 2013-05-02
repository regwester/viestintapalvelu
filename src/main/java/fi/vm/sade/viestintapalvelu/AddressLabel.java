package fi.vm.sade.viestintapalvelu;

public class AddressLabel {
	/**
	 * Etunimi. Esim. "Ville".
	 */
	private final String firstName;
	/**
	 * Toinen nimi. Esim. "Aleksi".
	 */
	private final String middleName;
	/**
	 * Sukunimi. Esim. "Peurala".
	 */
	private final String lastName;

	/**
	 * Katuosoite. Esim. "Pengerkatu 20 B 27".
	 */
	private final String streetAddress;
	/**
	 * Postinumero. Esim. "00500".
	 */
	private final String postalCode;
	/**
	 * Postitoimipaikka. Esim. "Helsinki".
	 */
	private final String postOffice;
	/**
	 * Maa, jos muu kuin Suomi. Esim. "Sweden".
	 */
	private final String country;

	public AddressLabel(String firstName, String middleName, String lastName,
			String streetAddress, String postalCode, String postOffice,
			String country) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.streetAddress = streetAddress;
		this.postalCode = postalCode;
		this.postOffice = postOffice;
		this.country = country;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
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
