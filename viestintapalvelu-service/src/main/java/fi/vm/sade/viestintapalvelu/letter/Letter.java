package fi.vm.sade.viestintapalvelu.letter;

import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.StringUtils;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.address.ExtendedAddressLabel;

@ApiModel(value = "Kirjemallipohjaan sisällytettävät kirjekohtaiset tiedot")
public class Letter {
	public Letter() {
	}

	public Letter(ExtendedAddressLabel addressLabel, String languageCode,
			String hakukohde, String letterBodyText,
			List<Map<String, String>> customLetterContents) {
		this.addressLabel = addressLabel;
		this.languageCode = StringUtils.isEmpty(languageCode) ? "FI"
				: languageCode;
		this.hakukohde = hakukohde;
		this.letterBodyText = letterBodyText;
		this.customLetterContents = customLetterContents;
	}

	@ApiModelProperty(value = "Osoitetiedot", required = true)
	private ExtendedAddressLabel addressLabel;

	@ApiModelProperty(value = "Kirjepohjan tunniste")
	private String templateId;

	@ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'")
	private String languageCode;

	@ApiModelProperty(value = "Kirjeen leipäteksti, käyttäjältä HTML-muodossa, ajetaan palvelussa JSoup.clean-filtterin läpi", required = true, notes = "Tämä kenttä on turhaan jokaisen kirjeen osa. Olisi hyvä siirtää osaksi luokkaa <code>KoekutsukirjeBatch<code>")
	private String letterBodyText;

	@ApiModelProperty(value = "Tieto hakukohteesta, johon kirje liittyy.", required = false)
	private String hakukohde;

	@ApiModelProperty(value = "Tieto koulutuksen tarjoajasta, johon tämä kije liittyy..", required = false)
	private String tarjoaja;

	@ApiModelProperty(value = "Kirjeen yleiset personointikentät", required = false, notes = "Placeholder toistaiseksi. Voidaan välittää avain-arvo-pareina mahdollisia lisätietoja, "
			+ "joita voidaan käyttää mahdollisesti jatkossa esimerkiksi _hakukohteen_ tai "
			+ "_kokeen_ tietojen täyttämiseksi koekutsukirjeeseen. Tässä vaiheessa (20140124, versio 8?), kun virkailijat saavat "
			+ "kirjoittaa koko kirjeen editorilla, ei ole vielä tiedossa mitä mahdollisia lisäkenttiä seuraaviin versioihin tulee / "
			+ "mitä mahdollisesti halutaan tähänkin versioon kuitenkinmukaan. "
			+ "Tällä ratkaisulla ei tarvita uusia muuttujia tähän luokkaan toistaiseksi, vaan voidaan pikaisesti toteuttaa muutokset "
			+ "hakemalla tästä Mapista halutut arvot, jos sellaisia tarvitaan. Jatkossa on ehkä syytä lisätä tähän luokkaan omat "
			+ "muuttujansa selvyyden vuoksi (ainakin) niille kentille, jotka ovat pakollisia.")
	private List<Map<String, String>> customLetterContents;

	public ExtendedAddressLabel getAddressLabel() {
		return addressLabel;
	}

	public String getLetterBodyText() {
		return letterBodyText;
	}

	public String getHakukohde() {
		return hakukohde;
	}

	public String getTarjoaja() {
		return tarjoaja;
	}

	public List<Map<String, String>> getCustomLetterContents() {
		return customLetterContents;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	@Override
	public String toString() {
		return "Koekutsukirje [" + "addressLabel=" + addressLabel
				+ ", languageCode=" + languageCode + ", hakukohde=" + hakukohde
				+ ", letterBodyText=" + letterBodyText + "]";
	}
}
