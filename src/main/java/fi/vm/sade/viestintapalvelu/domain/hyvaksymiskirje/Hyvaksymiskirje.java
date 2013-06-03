package fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje;

import java.util.List;
import java.util.Map;

import fi.vm.sade.viestintapalvelu.domain.address.HasPostalAddress;

public interface Hyvaksymiskirje extends HasPostalAddress {
	public String getKoulu();

	public String getKoulutus();

	public String getLanguageCode();

	public List<Map<String, String>> getTulokset();
}
