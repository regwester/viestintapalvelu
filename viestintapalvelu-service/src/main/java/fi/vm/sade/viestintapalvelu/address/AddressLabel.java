package fi.vm.sade.viestintapalvelu.address;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.letter.dto.AddressLabelDetails;

@ApiModel(value = "Kirjeen vastaanottajan osoitetiedot")
public class AddressLabel implements AddressLabelDetails {
    public AddressLabel() {
    }

    public AddressLabel(String firstName, String lastName, String addressline,
                        String addressline2, String addressline3, String postalCode,
                        String city, String region, String country, String countryCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressline = addressline;
        this.addressline2 = addressline2;
        this.addressline3 = addressline3;
        this.postalCode = postalCode;
        this.city = city;
        this.region = region;
        this.country = country;
        this.countryCode = countryCode;
    }

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

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getAddressline() {
        return addressline;
    }

    @Override
    public String getAddressline2() {
        return addressline2;
    }

    @Override
    public String getAddressline3() {
        return addressline3;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getCountryCode() {
        return countryCode;
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
