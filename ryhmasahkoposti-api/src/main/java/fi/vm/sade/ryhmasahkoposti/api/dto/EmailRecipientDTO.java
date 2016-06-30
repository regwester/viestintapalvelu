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
package fi.vm.sade.ryhmasahkoposti.api.dto;

public class EmailRecipientDTO extends EmailRecipient {
    private Long recipientID;
    private Long recipientVersion;
    private Long emailMessageID;
    private String firstName;
    private String lastName;
    private String organizationName;
    private String sendSuccessful;
    private String timestamp;
    private String letterHash;
    
    public Long getRecipientID() {
        return recipientID;
    }
    
    public void setRecipientID(Long recipientID) {
        this.recipientID = recipientID;
    }

    public Long getRecipientVersion() {
        return recipientVersion;
    }

    public void setRecipientVersion(Long recipientVersion) {
        this.recipientVersion = recipientVersion;
    }

    public Long getEmailMessageID() {
        return emailMessageID;
    }
    public void setEmailMessageID(Long emailMessageID) {
        this.emailMessageID = emailMessageID;
    }

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

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getSendSuccessful() {
        return sendSuccessful;
    }

    public void setSendSuccessful(String sendSuccessful) {
        this.sendSuccessful = sendSuccessful;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLetterHash() {
        return letterHash;
    }

    public void setLetterHash(String letterHash) {
        this.letterHash = letterHash;
    }
}
