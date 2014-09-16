package fi.vm.sade.ryhmasahkoposti.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailData {

    private List<EmailRecipient> recipient = new LinkedList<EmailRecipient>();
    private List<ReplacementDTO> replacements = new LinkedList<ReplacementDTO>();
    private EmailMessage email = new EmailMessage();

    public EmailData() {
        super();
    }

    public EmailData(List<EmailRecipient> recipient, EmailMessage email) {
        super();
        this.recipient = recipient;
        this.email = email;
    }

    public EmailData(List<EmailRecipient> recipient, List<ReplacementDTO> replacements, EmailMessage email) {
        super();
        this.recipient = recipient;
        this.replacements = replacements;
        this.email = email;
    }

    /**
     * @return the replacements
     */
    public List<ReplacementDTO> getReplacements() {
        return replacements;
    }

    /**
     * @param replacements the replacements to set
     */
    public void setReplacements(List<ReplacementDTO> replacements) {
        this.replacements = replacements;
    }

    public List<EmailRecipient> getRecipient() {
        return recipient;
    }

    public void setRecipient(List<EmailRecipient> recipient) {
        this.recipient = recipient;
    }

    public EmailMessage getEmail() {
        return email;
    }

    public void setEmail(EmailMessage email) {
        this.email = email;
    }

    public void setSenderOid(String senderOid) {
        this.email.setSenderOid(senderOid);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EmailData [recipient=" + recipient + ", replacements="
                + replacements + ", email=" + email + "]";
    }
}
