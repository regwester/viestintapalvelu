package fi.vm.sade.viestintapalvelu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;

import fi.vm.sade.generic.model.BaseEntity;

/**
 * @author migar1
 *
 *CREATE TABLE kirjeet.vastaanottajaosoite (
  id bigint NOT NULL,
  vastaanottaja_id bigint,
  etunimi character varying(255),
  sukunimi character varying(255),
  osoite character varying(255),
  osoite2 character varying(255),
  osoite3 character varying(255),
  postinumero character varying(10),
  kaupunki character varying(255),
  maakunta character varying(255),
  maa character varying(255),
  maakoodi character varying(5),
  CONSTRAINT vastaanottajaosoite_pk PRIMARY KEY (id),
  CONSTRAINT vastaanottajaosoite_vastaanottaja_id FOREIGN KEY (vastaanottaja_id)
      REFERENCES kirjeet.vastaanottaja (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)

 */

@Table(name = "vastaanottajaosoite", schema="kirjeet")
@Entity()
public class LetterReceiverAddress extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @OneToOne()
    @JoinColumn(name = "vastaanottaja_id")
    @JsonBackReference
    private LetterReceivers letterReceivers;

    @Column(name = "etunimi")
    private String firstName = null;
            
    @Column(name = "sukunimi")
    private String lastName = null;
            
    @Column(name = "osoite")
    private String addressline = null;
            
    @Column(name = "osoite2")
    private String addressline2 = null;
            
    @Column(name = "osoite3")
    private String addressline3 = null;
            
    @Column(name = "postinumero")
    private String postalCode = null;
            
    @Column(name = "kaupunki")
    private String city = null;
            
    @Column(name = "maakunta")
    private String region = null;
            
    @Column(name = "maa")
    private String country = null;
            
    @Column(name = "maakoodi")
    private String countryCode = null;            
    
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

	public LetterReceivers getLetterReceivers() {
		return letterReceivers;
	}

	public void setLetterReceivers(LetterReceivers letterReceivers) {
		this.letterReceivers = letterReceivers;
	}

	@Override
	public String toString() {
		return "LetterReceiverAddress [letterReceivers=" + letterReceivers
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", addressline=" + addressline + ", addressline2="
				+ addressline2 + ", addressline3=" + addressline3
				+ ", postalCode=" + postalCode + ", city=" + city + ", region="
				+ region + ", country=" + country + ", countryCode="
				+ countryCode + "]";
	}

}
