package fi.vm.sade.viestintapalvelu.test.stub;

import java.util.List;
import java.util.Map;

import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;
import fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje.Hyvaksymiskirje;

public class HyvaksymiskirjeStub implements Hyvaksymiskirje {
	public HyvaksymiskirjeStub(PostalAddress postalAddress,
			String languageCode, String koulu, String koulutus,
			List<Map<String, String>> tulokset) {
		this.postalAddress = postalAddress;
		this.languageCode = languageCode;
		this.tulokset = tulokset;
		this.koulu = koulu;
		this.koulutus = koulutus;
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

	public List<Map<String, String>> getTulokset() {
		return tulokset;
	}

	public String getKoulu() {
		return koulu;
	}

	public String getKoulutus() {
		return koulutus;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	@Override
	public PostalAddress getPostalAddress() {
		return postalAddress;
	}
}
