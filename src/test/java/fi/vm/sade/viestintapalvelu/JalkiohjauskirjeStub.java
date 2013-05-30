package fi.vm.sade.viestintapalvelu;

import java.util.List;
import java.util.Map;

import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.Jalkiohjauskirje;

public class JalkiohjauskirjeStub implements Jalkiohjauskirje {
	private final PostalAddress address;
	private final String languageCode;
	private final List<Map<String, String>> hakutulokset;

	public JalkiohjauskirjeStub(PostalAddress address, String languageCode,
			List<Map<String, String>> hakutulokset) {
		this.address = address;
		this.languageCode = languageCode;
		this.hakutulokset = hakutulokset;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public java.util.List<java.util.Map<String, String>> getTulokset() {
		return hakutulokset;
	}

	public PostalAddress getPostalAddress() {
		return address;
	}
}