package fi.vm.sade.viestintapalvelu.address;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

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

    /**
     * Etunimi. Esim. "Ville".
     */
    @NotEmpty
    private String firstName;
    /**
     * Sukunimi. Esim. "Peurala".
     */
    @NotEmpty
    private String lastName;

    /**
     * Katuosoite. Esim. "Pengerkatu 20 B 27".
     */
    @NotEmpty
    private String addressline;
    /**
     * Toinen osoiterivi ulkomaalaisille osoitteille.
     */
    private String addressline2;
    /**
     * Kolmas osoiterivi ulkomaalaisille osoitteille.
     */
    private String addressline3;
    /**
     * Postinumero. Esim. "00500".
     */
    @NotEmpty
    private String postalCode;
    /**
     * Postitoimipaikka. Esim. "Helsinki".
     */
    @NotEmpty
    private String city;
    /**
     * Maakunta ulkomaalaisille osoitteille.
     */
    private String region;
    /**
     * Maa, jos muu kuin Suomi. Esim. "Sweden".
     */
    @NotEmpty
    private String country;
    /**
     * The two letter country code using the ISO3166 standard. If the country code is unknown then use XX.
     * They consist of two characters written in CAPITAL letters.
     */
    @NotEmpty
    @Size(min = 2, max = 3)
    private String countryCode;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddressline() {
        return addressline;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public String getAddressline3() {
        return addressline3;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

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
