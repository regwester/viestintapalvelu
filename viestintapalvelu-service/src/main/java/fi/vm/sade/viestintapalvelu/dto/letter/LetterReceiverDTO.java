package fi.vm.sade.viestintapalvelu.dto.letter;

import java.io.Serializable;

public class LetterReceiverDTO implements Serializable {
    private static final long serialVersionUID = 1623068176754159720L;
    private Long id;
    private String name;
    private String address1;
    private String address2;
    private String postalCode;
    private String city;
    private String region;
    private String country;
    private Long letterReceiverLetterID;
    private String templateName;
    private String contentType;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress1() {
        return address1;
    }
    
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    
    public String getAddress2() {
        return address2;
    }
    
    public void setAddress2(String address2) {
        this.address2 = address2;
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
    
    public Long getLetterReceiverLetterID() {
        return letterReceiverLetterID;
    }

    public void setLetterReceiverLetterID(Long letterReceiverLetterID) {
        this.letterReceiverLetterID = letterReceiverLetterID;
    }

    public String getTemplateName() {
        return templateName;
    }
    
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
