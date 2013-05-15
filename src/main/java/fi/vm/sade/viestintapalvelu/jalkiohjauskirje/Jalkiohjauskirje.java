package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import java.util.List;
import java.util.Map;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;


public class Jalkiohjauskirje {
	public Jalkiohjauskirje() {
	}

	public Jalkiohjauskirje(AddressLabel addressLabel, List<Map<String, String>> tulokset) {
		this.addressLabel = addressLabel;
		this.tulokset = tulokset;
	}

	/**
	 * Osoitetiedot.
	 */
	private AddressLabel addressLabel;
	/**
	 * Hakutulokset.
	 */
	private List<Map<String, String>> tulokset;

	public AddressLabel getAddressLabel() {
		return addressLabel;
	}

	public List<Map<String, String>> getTulokset() {
		return tulokset;
	}

	@Override
	public String toString() {
		return "Jalkiohjauskirje [addressLabel=" + addressLabel + ", results="
				+ tulokset + "]";
	}

}
