package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

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
    @NotNull
    @Valid
    private AddressLabel addressLabel;
    /**
     * Kielikoodi ISO 639-1.
     */
    private String languageCode;
    /**
     * Hakutulokset.
     */
    @NotNull
    @Size(min = 1)
    @ContainsKeys(value = {"organisaationNimi", "oppilaitoksenNimi", "hakukohteenNimi", "hyvaksytyt", "kaikkiHakeneet",
            "omatPisteet", "alinHyvaksyttyPistemaara"})
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
