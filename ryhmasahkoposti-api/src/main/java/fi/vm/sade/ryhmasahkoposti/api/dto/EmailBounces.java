package fi.vm.sade.ryhmasahkoposti.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

public class EmailBounces {

    @JsonProperty("bounced_emails")
    private List<EmailBounce> bouncedEmails = new LinkedList<>();

    public List<EmailBounce> getBouncedEmails() {
        return bouncedEmails;
    }

    public void setBouncedEmails(List<EmailBounce> bouncedEmails) {
        this.bouncedEmails = bouncedEmails;
    }
}
