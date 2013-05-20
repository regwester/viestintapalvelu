package fi.vm.sade.viestintapalvelu.hyvaksymiskirje;

import java.util.List;
import java.util.Map;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;


public class Hyvaksymiskirje {
	public Hyvaksymiskirje() {
	}

	public Hyvaksymiskirje(AddressLabel addressLabel, String koulu, String koulutus, List<Map<String, String>> tulokset) {
		this.addressLabel = addressLabel;
		this.tulokset = tulokset;
		this.koulu = koulu;
		this.koulutus = koulutus;
	}

	/**
	 * Osoitetiedot.
	 */
	private AddressLabel addressLabel;
	/**
	 * Koulu johon hyväksytty.
	 */
	private String koulu;
	/**
	 * Koulutus johon hyväksytty.
	 */
	private String koulutus;
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
	
	public String getKoulu() {
		return koulu;
	}
	
	public String getKoulutus() {
		return koulutus;
	}

	@Override
	public String toString() {
		return "Hyvaksymiskirje [addressLabel=" + addressLabel + ", koulu=" + koulu
				+ ", koulutus=" + koulutus + ", results=" + tulokset + "]";
	}
}
