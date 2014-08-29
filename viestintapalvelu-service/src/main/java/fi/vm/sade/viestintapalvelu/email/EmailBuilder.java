package fi.vm.sade.viestintapalvelu.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.viestintapalvelu.letter.Letter;

@Component
public class EmailBuilder {

    /**
     * Builds email message of given Letter
     * @param letter
     * @return
     * @throws Exception any of the required fields are missing.
     */
    
    public EmailData buildEmailData(Letter letter) throws Exception {
        List<EmailRecipient> recipients = getRecipients(letter);
        
        if (recipients == null) {
            throw new IllegalArgumentException("Vastaanottajat puuttuu");
        }
        EmailData emailData = new EmailData();
        emailData.setRecipient(getRecipients(letter));
        EmailMessage emailMessage = getEmailMessage(letter);
        
        emailData.setEmail(emailMessage);
        return emailData;
    }

    public EmailMessage getEmailMessage(Letter letter) throws Exception {

        EmailMessage message = new EmailMessage();
        Map<TemplateEmailField, String> emailContext = getEmailContext(letter);
        message.setBody(emailContext.get(TemplateEmailField.BODY));
        message.setSubject(emailContext.get(TemplateEmailField.SUBJECT));
        return message;
    }

    private Map<TemplateEmailField, String> getEmailContext(Letter letter) throws Exception {
        Map<TemplateEmailField, String> emailContext = new HashMap<TemplateEmailField, String>();
        Map<String, Object> templateReplacements = letter.getTemplateReplacements();
        Map<String, Object> letterReplacements = letter.getCustomLetterContents();

        for (TemplateEmailField t : TemplateEmailField.values()) {
            if (letterReplacements.get(t.getFieldName()) != null) {
                emailContext.put(t, (String) letterReplacements.get(t.getFieldName()));
            } else {
                emailContext.put(t, (String) templateReplacements.get(t.getFieldName()));
            }
        }
        checkContext(emailContext);
        return emailContext;
    }

    private boolean checkContext(Map<TemplateEmailField, String> context) throws Exception {
        for (TemplateEmailField t : TemplateEmailField.values()) {
            String value = context.get(t);
            if (value == null || value.isEmpty()) {
                throw new IllegalArgumentException("Pakollinen kenttä puuttuu " + t);
            }
        }
        return true;
    }
    
    private List<EmailRecipient> getRecipients(Letter letter) throws IllegalArgumentException {
        String emailAddress = letter.getEmailAddress();
        if (emailAddress == null || emailAddress.isEmpty()) {
            throw new IllegalArgumentException("Vastaanottajat puuttuu");
        }

        EmailRecipient recipient = new EmailRecipient();
        recipient.setEmail(letter.getEmailAddress());
        recipient.setLanguageCode(letter.getLanguageCode());
        List<EmailRecipient> recipients = new ArrayList<EmailRecipient>();
        return recipients;
    }

}
