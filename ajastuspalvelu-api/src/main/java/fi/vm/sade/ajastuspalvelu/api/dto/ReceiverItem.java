/*
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

package fi.vm.sade.ajastuspalvelu.api.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * User: ratamaa
 * Date: 22.10.2014
 * Time: 13:27
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiverItem implements Serializable {
    private static final long serialVersionUID = -7769952777161931528L;

    @ApiModelProperty(value = "OID-tyyppi: (henkilo)", required = false)
    private String oidType;
    @ApiModelProperty(value = "OID-arvo", required = false)
    private String oid;
    @ApiModelProperty(value = "Vastaanottajan sähköpostiosoite", required = false)
    private String email;
    @ApiModelProperty(value = "Vastaanottajan kirjeen lähetysosoite", required = false)
    private AddressLabel addressLabel;
    @ApiModelProperty(value = "Vastaanottajakohtaiset korvauskentät")
    private Map<String,Object> context = new HashMap<String, Object>();

    public String getOidType() {
        return oidType;
    }

    public void setOidType(String oidType) {
        this.oidType = oidType;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AddressLabel getAddressLabel() {
        return addressLabel;
    }

    public void setAddressLabel(AddressLabel addressLabel) {
        this.addressLabel = addressLabel;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
}
