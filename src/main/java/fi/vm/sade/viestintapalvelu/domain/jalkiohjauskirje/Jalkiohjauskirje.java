package fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje;

import java.util.List;
import java.util.Map;

import fi.vm.sade.viestintapalvelu.domain.address.HasPostalAddress;
import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;

public class Jalkiohjauskirje implements HasPostalAddress {
	public Jalkiohjauskirje() {
	}

	public Jalkiohjauskirje(PostalAddress postalAddress, String languageCode,
			List<Map<String, String>> tulokset) {
		this.postalAddress = postalAddress;
		this.languageCode = languageCode;
		this.tulokset = tulokset;
	}

	/**
	 * Osoitetiedot.
	 */
	private PostalAddress postalAddress;
	/**
	 * Kielikoodi ISO 639-1.
	 */
	private String languageCode;
	/**
	 * Hakutulokset.
	 */
	private List<Map<String, String>> tulokset;

	@Override
	public PostalAddress postalAddress() {
		return postalAddress;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public List<Map<String, String>> getTulokset() {
		return tulokset;
	}

	@Override
	public String toString() {
		return "Jalkiohjauskirje [postalAddress=" + postalAddress
				+ ", languageCode=" + languageCode + ", results=" + tulokset
				+ "]";
	}

}
