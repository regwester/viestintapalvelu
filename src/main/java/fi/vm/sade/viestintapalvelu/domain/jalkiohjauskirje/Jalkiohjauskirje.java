package fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje;

import java.util.List;
import java.util.Map;

import fi.vm.sade.viestintapalvelu.domain.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.domain.address.HasPostalAddress;

public interface Jalkiohjauskirje extends HasPostalAddress {
	/**
	 * Kielikoodi ISO 639-1.
	 */
	public String getLanguageCode();

	public List<Map<String, String>> getTulokset();

	public AddressLabel getAddressLabel();
}
