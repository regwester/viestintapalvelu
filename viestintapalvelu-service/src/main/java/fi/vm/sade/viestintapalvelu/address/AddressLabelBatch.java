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
