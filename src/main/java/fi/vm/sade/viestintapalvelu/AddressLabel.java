package fi.vm.sade.viestintapalvelu;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AddressLabel {
	/**
	 * Etunimi. Esim. "Ville".
	 */
	@XmlElement
	private String firstName;
	/**
	 * Sukunimi. Esim. "Peurala".
	 */
	@XmlElement
	private String lastName;

	/**
	 * Katuosoite. Esim. "Pengerkatu 20 B 27".
	 */
	@XmlElement
	private String streetAddress;
	/**
	 * Postinumero. Esim. "00500".
	 */
	@XmlElement
	private String postalCode;
	/**
	 * Postitoimipaikka. Esim. "Helsinki".
	 */
	@XmlElement
	private String postOffice;
	/**
	 * Maa, jos muu kuin Suomi. Esim. "Sweden".
	 */
	@XmlElement
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

	@Override
	public String toString() {
		return "AddressLabel [firstName=" + firstName + ", lastName="
				+ lastName + ", streetAddress=" + streetAddress
				+ ", postalCode=" + postalCode + ", postOffice=" + postOffice
				+ ", country=" + country + "]";
	}

}
