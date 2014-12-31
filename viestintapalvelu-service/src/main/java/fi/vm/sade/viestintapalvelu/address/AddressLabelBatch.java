package fi.vm.sade.viestintapalvelu.address;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;

@ApiModel(value = "Joukko osoitteita tarratulostuksessa käytettävän dokumentin luontiin")
public class AddressLabelBatch {
	@ApiModelProperty(value = "Yhteen tarratulostuksessa käytettävään dokumenttiin lisättävät osoitteet", required=true)	
    private List<AddressLabel> addressLabels;

    public AddressLabelBatch() {
    }

    public AddressLabelBatch(List<AddressLabel> labels) {
        this.addressLabels = labels;
    }

    public List<AddressLabel> getAddressLabels() {
        return addressLabels;
    }

    @Override
    public String toString() {
        return "AddressLabelBatch [addressLabels=" + addressLabels + "]";
    }
}
