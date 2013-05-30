package fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje;

import java.util.List;
import java.util.Map;

import fi.vm.sade.viestintapalvelu.domain.address.AddressLabel;

public class Jalkiohjauskirje {
	public Jalkiohjauskirje() {
	}

	public Jalkiohjauskirje(AddressLabel addressLabel, String languageCode,
			List<Map<String, String>> tulokset) {
		this.addressLabel = addressLabel;
		this.languageCode = languageCode;
		this.tulokset = tulokset;
	}

	/**
	 * Osoitetiedot.
	 */
	private AddressLabel addressLabel;
	/**
	 * Kielikoodi ISO 639-1.
	 */
	private String languageCode;
	/**
	 * Hakutulokset.
	 */
	private List<Map<String, String>> tulokset;

	public AddressLabel getAddressLabel() {
		return addressLabel;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public List<Map<String, String>> getTulokset() {
		return tulokset;
	}

	@Override
	public String toString() {
		return "Jalkiohjauskirje [addressLabel=" + addressLabel
				+ ", languageCode=" + languageCode + ", results=" + tulokset
				+ "]";
	}

}
