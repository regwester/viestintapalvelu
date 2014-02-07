package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.cxf.common.util.StringUtils;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

@ApiModel(value = "Jälkiohjauskirjeeseen sisällytettävät kirjekohtaiset tiedot")
public class Jalkiohjauskirje {
    public Jalkiohjauskirje() {
    }

    public Jalkiohjauskirje(AddressLabel addressLabel, String languageCode,
                            List<Map<String, String>> tulokset) {
        this.addressLabel = addressLabel;
        this.languageCode = StringUtils.isEmpty(languageCode) ? "FI" : languageCode;
        this.tulokset = tulokset;
    }

    @NotNull
    @Valid
    @ApiModelProperty(value = "Osoitetiedot", required=true)
    private AddressLabel addressLabel;

    @ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'")
    private String languageCode;
    @NotNull
    @Size(min = 1)
    @ContainsKeys(value = {"organisaationNimi", "oppilaitoksenNimi", "hakukohteenNimi", "hyvaksytyt", "kaikkiHakeneet",
            "omatPisteet", "alinHyvaksyttyPistemaara"})
    @ApiModelProperty(value = "Hakukoetulokset kirjeen liitteeksi, avaimina: {'organisaationNimi', 'oppilaitoksenNimi', 'hakukohteenNimi', 'hyvaksytyt', 'kaikkiHakeneet', 'omatPisteet', 'alinHyvaksyttyPistemaara'}", required=true)
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
