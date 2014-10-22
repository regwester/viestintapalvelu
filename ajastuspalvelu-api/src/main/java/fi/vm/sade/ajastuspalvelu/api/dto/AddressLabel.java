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

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Kirjeen vastaanottajan osoitetiedot")
public class AddressLabel implements Serializable {
    private static final long serialVersionUID = 8596158946243672828L;

    @NotEmpty
    @ApiModelProperty(value = "Etunimi. Esim. 'Ville'", required=true)
    private String firstName;

    @NotEmpty
    @ApiModelProperty(value = "Sukunimi. Esim. 'Peurala'", required=true)
    private String lastName;

    @NotEmpty
    @ApiModelProperty(value = "Katuosoite. Esim. 'Pengerkatu 20 B 27'", required=true)
    private String addressline;

    @ApiModelProperty(value = "Toinen osoiterivi ulkomaalaisille osoitteille")
    private String addressline2;

    @ApiModelProperty(value = "Kolmas osoiterivi ulkomaalaisille osoitteille")
    private String addressline3;
    
    @NotEmpty
    @ApiModelProperty(value = "Postinumero. Esim. '00500'", required=true)
    private String postalCode;

    @NotEmpty
    @ApiModelProperty(value = "Postitoimipaikka. Esim. 'Helsinki'", required=true)
    private String city;

    @ApiModelProperty(value = "Maakunta ulkomaalaisille osoitteille")
    private String region;

    @NotEmpty
    @ApiModelProperty(value = "Maa, jos muu kuin Suomi. Esim. 'Sweden'", required=true)
    private String country;

    @NotEmpty
    @Size(min = 2, max = 3)
    @ApiModelProperty(value = "The two letter country code using the ISO3166 standard. If the country code is unknown then use XX. They consist of two characters written in CAPITAL letters,", required=true)
    private String countryCode;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressline() {
        return addressline;
    }

    public void setAddressline(String addressline) {
        this.addressline = addressline;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getAddressline3() {
        return addressline3;
    }

    public void setAddressline3(String addressline3) {
        this.addressline3 = addressline3;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "AddressLabel [firstName=" + firstName + ", lastName="
                + lastName + ", addressline=" + addressline + ", addressline2="
                + addressline2 + ", addressline3=" + addressline3
                + ", postalCode=" + postalCode + ", city=" + city + ", region="
                + region + ", country=" + country + ", countryCode="
                + countryCode + "]";
    }

}
