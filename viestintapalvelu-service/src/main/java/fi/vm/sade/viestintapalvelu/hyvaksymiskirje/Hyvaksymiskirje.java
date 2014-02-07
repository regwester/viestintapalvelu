package fi.vm.sade.viestintapalvelu.hyvaksymiskirje;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;

import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.StringUtils;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Hyväksymiskirjeeseen sisällytettävät kirjekohtaiset tiedot")
public class Hyvaksymiskirje {
    public Hyvaksymiskirje() {
    }

    public Hyvaksymiskirje(AddressLabel addressLabel, String languageCode,
                           String koulu, String koulutus, List<Map<String, String>> tulokset) {
        this.addressLabel = addressLabel;
        this.languageCode = StringUtils.isEmpty(languageCode) ? "FI" : languageCode;
        this.tulokset = tulokset;
        this.koulu = koulu;
        this.koulutus = koulutus;
    }

    @ApiModelProperty(value = "Osoitetiedot", required=true)
    private AddressLabel addressLabel;

    @ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'")
    private String languageCode;
    
    @ApiModelProperty(value = "Koulu, johon hyväksytty", required=true)
    private String koulu;

    @ApiModelProperty(value = "Koulutus, johon hyväksytty", required=true)
    private String koulutus;

    @ApiModelProperty(value = "Hakukoetulokset kirjeen liitteeksi, avaimina: {'organisaationNimi', 'oppilaitoksenNimi', 'hakukohteenNimi', 'hyvaksytyt', 'kaikkiHakeneet', 'omatPisteet', 'alinHyvaksyttyPistemaara'}", required=true)
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
