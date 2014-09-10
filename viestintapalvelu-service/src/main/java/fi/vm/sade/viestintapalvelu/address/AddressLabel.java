package fi.vm.sade.viestintapalvelu.address;

import org.apache.commons.lang.WordUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Kirjeen vastaanottajan osoitetiedot")
public class AddressLabel {
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

    public String getFirstName() {
        return capitalize(firstName);
    }

    public String getLastName() {
        return capitalize(lastName);
    }

    public String getAddressline() {
        return capitalizeAddress(addressline);
    }

    public String getAddressline2() {
        return capitalizeAddress(addressline2);
    }

    public String getAddressline3() {
        return capitalizeAddress(addressline3);
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return  uppercase(city);
    }

    public String getRegion() {
        return uppercase(region);
    }

    public String getCountry() {
        return uppercase(country);
    }

    public String getCountryCode() {
        return countryCode;
    }

    private String uppercase(String s) {
        if (s != null) {
            return s.toUpperCase();
        }
        return s;
    }
    
    private String capitalizeAddress(String s) {
        if (s != null) {
            // jos PL, PO, PB tai Poste restante osoite jätetään käsittelemättä
            // Ei ehkä täysin globaali ratkaisu. 
            // Poikkeussääntöjä pitänee vielä jatkossa lisätä. 
            if (s.contains("PL ")|| s.contains("PO ") || s.contains("PB ") 
                    || s.contains("Poste restante")) {
                return s;
            }
            return capitalize(s.toLowerCase());
        }
        return s;
    }
    
    private String capitalize(String s) {
        if (s != null) {
            return WordUtils.capitalize(s.toLowerCase());
        }
        return s;
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
