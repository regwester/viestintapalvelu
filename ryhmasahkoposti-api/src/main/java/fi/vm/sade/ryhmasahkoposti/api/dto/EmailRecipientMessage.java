package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.List;


public class EmailRecipientMessage extends EmailMessage {

    private boolean isVirusChecked;
    private boolean isInfected;
    private String type;
    private EmailRecipientDTO recipient ;
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
    
    public static EmailRecipientMessage getFromEmailMessageDTO(EmailRecipientDTO recipient, EmailMessageDTO original) {
        EmailRecipientMessage result = new EmailRecipientMessage();
        result.setMessageReplacements(original.getMessageReplacements());
        result.setAttachInfo(original.getAttachInfo());
        result.setAttachments(original.getAttachments());
        result.setBody(original.getBody());
        result.setCallingProcess(original.getCallingProcess());
        result.setCharset(original.getCharset());
        result.setFrom(original.getBody());
        result.setHtml(original.isHtml());
        result.setInfected(original.isInfected());
        result.setLanguageCode(original.getLanguageCode());
        result.setOrganizationOid(original.getOrganizationOid());
        result.setReplyTo(original.getReplyTo());
        result.setSender(original.getSender());
        result.setSenderOid(original.getSenderOid());
        result.setSourceRegister(original.getSourceRegister());
        result.setSubject(original.getSubject());
        result.setTemplateName(original.getTemplateName());
        result.setType(original.getType());
        result.setVirusChecked(original.isVirusChecked());
        result.recipient = recipient;
        return result;
    }
}
