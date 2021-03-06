/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.viestintapalvelu.koekutsukirje;

import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.StringUtils;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;

@ApiModel(value = "Koekutsukirjemallipohjaan sisällytettävät kirjekohtaiset tiedot")
public class Koekutsukirje {
    public Koekutsukirje() {
    }

    public Koekutsukirje(AddressLabel addressLabel, String languageCode, String hakukohde, String letterBodyText, List<Map<String, String>> customLetterContents) {
        this.addressLabel = addressLabel;
        this.languageCode = StringUtils.isEmpty(languageCode) ? "FI" : languageCode;
        this.hakukohde = hakukohde;
        this.letterBodyText = letterBodyText;
        this.customLetterContents = customLetterContents;
    }

    @ApiModelProperty(value = "Osoitetiedot", required = true)
    private AddressLabel addressLabel;

    @ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'")
    private String languageCode;

    @ApiModelProperty(value = "Koekutsukirjeen leipäteksti, käyttäjältä HTML-muodossa, ajetaan palvelussa JSoup.clean-filtterin läpi", required = true, notes = "Tämä kenttä on turhaan jokaisen kirjeen osa. Olisi hyvä siirtää osaksi luokkaa <code>KoekutsukirjeBatch<code>")
    private String letterBodyText;

    @ApiModelProperty(value = "Valinnoista tuleva tieto siitä mihin (kouluun, koulutusohjelmaan jne. ) kokeella haetaan.", required = true)
    private String hakukohde;

    @ApiModelProperty(value = "Valinnoista tuleva tieto siitä mihin (kouluun, koulutusohjelmaan jne. ) kokeella haetaan.", required = false)
    private String tarjoaja;

    @ApiModelProperty(value = "Reserved for future", required = false, notes = "Placeholder toistaiseksi. Voidaan välittää avain-arvo-pareina mahdollisia lisätietoja, "
            + "joita voidaan käyttää mahdollisesti jatkossa esimerkiksi _hakukohteen_ tai "
            + "_kokeen_ tietojen täyttämiseksi koekutsukirjeeseen. Tässä vaiheessa (20140124, versio 8?), kun virkailijat saavat "
            + "kirjoittaa koko kirjeen editorilla, ei ole vielä tiedossa mitä mahdollisia lisäkenttiä seuraaviin versioihin tulee / "
            + "mitä mahdollisesti halutaan tähänkin versioon kuitenkinmukaan. "
            + "Tällä ratkaisulla ei tarvita uusia muuttujia tähän luokkaan toistaiseksi, vaan voidaan pikaisesti toteuttaa muutokset "
            + "hakemalla tästä Mapista halutut arvot, jos sellaisia tarvitaan. Jatkossa on ehkä syytä lisätä tähän luokkaan omat "
            + "muuttujansa selvyyden vuoksi (ainakin) niille kentille, jotka ovat pakollisia.")
    private List<Map<String, String>> customLetterContents;

    public AddressLabel getAddressLabel() {
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
        return "Koekutsukirje [" + "addressLabel=" + addressLabel + ", languageCode=" + languageCode + ", hakukohde=" + hakukohde + ", letterBodyText="
                + letterBodyText + "]";
    }
}
