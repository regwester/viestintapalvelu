package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.List;


public class EmailRecipientMessage extends EmailMessage {
    private boolean isVirusChecked;
    private boolean isInfected;
    private String type;
    private EmailRecipientDTO recipient;
    private List<ReplacementDTO> messageReplacements;

    public List<ReplacementDTO> getMessageReplacements() {
        return messageReplacements;
    }

    public void setMessageReplacements(List<ReplacementDTO> messageReplacements) {
        this.messageReplacements = messageReplacements;
    }

    public void setRecipient(EmailRecipientDTO recipient) {
        this.recipient = recipient;
    }

    public boolean isVirusChecked() {
        return isVirusChecked;
    }

    public void setVirusChecked(boolean isVirusChecked) {
        this.isVirusChecked = isVirusChecked;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean isInfected) {
        this.isInfected = isInfected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EmailRecipientDTO getRecipient() {
        return recipient;
    }

}
