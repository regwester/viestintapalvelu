package fi.vm.sade.viestintapalvelu.hyvaksymiskirje;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;

import java.util.List;
import java.util.Map;

public class Hyvaksymiskirje {
    public Hyvaksymiskirje() {
    }

    public Hyvaksymiskirje(AddressLabel addressLabel, String languageCode,
                           String koulu, String koulutus, List<Map<String, String>> tulokset) {
        this.addressLabel = addressLabel;
        this.languageCode = languageCode;
        this.tulokset = tulokset;
        this.koulu = koulu;
        this.koulutus = koulutus;
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

    public String getLanguageCode() {
        return languageCode;
    }

    @Override
    public String toString() {
        return "Hyvaksymiskirje [addressLabel=" + addressLabel
                + ", languageCode=" + languageCode + ", koulu=" + koulu
                + ", koulutus=" + koulutus + ", results=" + tulokset + "]";
    }
}
