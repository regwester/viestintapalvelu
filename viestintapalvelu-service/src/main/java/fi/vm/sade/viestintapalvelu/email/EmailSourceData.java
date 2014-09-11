package fi.vm.sade.viestintapalvelu.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.viestintapalvelu.letter.Letter;

public class EmailSourceData {

    private static Logger LOGGER = LoggerFactory.getLogger(EmailSourceData.class);
    
    private Map<String, Object> templateDataContext;
    private Letter letter;
    
    private Map<String, byte[]> attachmentData;
    private Map<String, String> attachmentContentType;
    private Map<TemplateEmailField, Object> emailContext;
    private List<EmailRecipient> recipients;

    @SuppressWarnings("unused")
    private EmailSourceData() {}
    
    public EmailSourceData(Letter letter, Map<String, Object> templateDataContext) throws Exception {
        this.templateDataContext = templateDataContext;
        this.letter = letter;

        initRecipient();
        initEmailContext();
    }

    public void addAttachment(String name, byte[] data, String contentType) {
        if (attachmentData == null) {
            attachmentData = new HashMap<String, byte[]>();
            attachmentContentType = new HashMap<String, String>();
        }
        this.attachmentData.put(name, data);
        this.attachmentContentType.put(name, contentType);
    }
    

    public Map<String, Object> getTemplateDataContext() {
        return templateDataContext;
    }

    public Letter getLetter() {
        return letter;
    }

    public Map<String, byte[]> getAttachmentData() {
        return attachmentData;
    }

    public Map<String, String> getAttachmentContentType() {
        return attachmentContentType;
    }

    public List<EmailRecipient> getRecipients() throws IllegalArgumentException {
        return recipients;
    }

    public Map<TemplateEmailField, Object> getEmailContext() throws Exception {
        
        return emailContext;
    }

    @Override
    public String toString() {
        return "EmailDataSource for letter " + letter;
    }

    private void initRecipient() throws IllegalArgumentException {
     // handle recipient
        String emailAddress = letter.getEmailAddress();
        if (emailAddress == null || emailAddress.isEmpty()) {
            throw new IllegalArgumentException("Vastaanottajat puuttuu");
        }

        EmailRecipient recipient = new EmailRecipient();
        recipient.setEmail(letter.getEmailAddress());
        recipient.setLanguageCode(letter.getLanguageCode());
        recipients = new ArrayList<EmailRecipient>();
        recipients.add(recipient);
    }
    
    private void initEmailContext() throws Exception {
        // handle email context
       emailContext = new HashMap<TemplateEmailField, Object>();
       
       LOGGER.debug("CONTEXT START");
       for (String k: this.templateDataContext.keySet()) {
           LOGGER.debug("'"+k+"'"+ " " +this.templateDataContext.get(k));
       }
       LOGGER.debug("CONTEXT END");
       
       for (TemplateEmailField field : TemplateEmailField.values()) {
           emailContext.put(field, this.templateDataContext.get(field.getFieldName()));
       }
       
       validateContext();
    }

    private void validateContext() throws Exception {
        
        for (TemplateEmailField t : TemplateEmailField.values()) {
            Object value = emailContext.get(t);
            
            if (t.isMandatory()) {
                if (value == null) {
                    LOGGER.debug("EmailData building fails missing mandatory field " + t);
                    throw new IllegalArgumentException("Pakollinen kenttä puuttuu " + t);
                } 
                if (t.getType() != null && String.class == t.getType()) {
                    if (value instanceof String) {
                        String stringValue = (String) value;
                        if (stringValue.isEmpty()) {
                            LOGGER.debug("EmailData building fails missing mandatory field " + t);
                            throw new IllegalArgumentException("Pakollinen kenttä puuttuu " + t);
                        }
                    } else {
                        LOGGER.debug("Expected String but got something else ");
                        throw new IllegalArgumentException("Tyyppitarkistus epännistui kentälle " + t + " arvolla " + value);
                    }
                }
            }
        }
    }
}
