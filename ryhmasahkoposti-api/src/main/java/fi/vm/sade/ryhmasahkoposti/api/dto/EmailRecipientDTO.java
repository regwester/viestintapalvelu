package fi.vm.sade.ryhmasahkoposti.api.dto;

public class EmailRecipientDTO extends EmailRecipient {
    private Long recipientID;
    private Long recipientVersion;
    private Long emailMessageID;
    private String firstName;
    private String lastName;
    private String organizationName;
    private String sendSuccessfull;
    private String timestamp;
    
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

    public String getSendSuccessfull() {
        return sendSuccessfull;
    }

    public void setSendSuccessfull(String sendSuccessfull) {
        this.sendSuccessfull = sendSuccessfull;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
