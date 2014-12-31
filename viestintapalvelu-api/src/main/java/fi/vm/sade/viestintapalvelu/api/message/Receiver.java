/**
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */
package fi.vm.sade.viestintapalvelu.api.message;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author risal1
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Vastaanottajan tiedot")
public class Receiver implements Serializable {

    private static final long serialVersionUID = 3726211657144795272L;
    
    @ApiModelProperty(value = "Vastaanottajan sähköpostiosoite", required = false)
    public final String email;
    
    @ApiModelProperty(value = "Vastaanottajan osoite", required = false)
    public final AddressLabel addressLabel;
    
    @ApiModelProperty(value = "Vastaanottajakohtaiset korvauskentät")
    public final Map<String,Object> replacements;
    
    @ApiModelProperty(value = "Vastaanottajan henkilöturvatunnus", required = false)
    public final String hetu;

    public Receiver(String email, AddressLabel addressLabel, Map<String, Object> replacements, String hetu) {
        this.email = email;
        this.addressLabel = addressLabel;
        this.replacements = replacements;
        this.hetu = hetu;
    }

    @Override
    public String toString() {
        return "Receiver [email=" + email + ", addressLabel=" + addressLabel + ", replacements=" + replacements + ", hetu=" + hetu + "]";
    }
    
}
