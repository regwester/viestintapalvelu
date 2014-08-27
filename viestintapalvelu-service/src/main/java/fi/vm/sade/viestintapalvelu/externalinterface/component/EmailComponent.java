package fi.vm.sade.viestintapalvelu.externalinterface.component;

import java.util.*;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.viestintapalvelu.externalinterface.api.EmailResource;
import fi.vm.sade.viestintapalvelu.letter.Letter;

@Component
public class EmailComponent {
    @Resource
    private EmailResource emailResourceClient;
    
    public boolean sendEmail(Letter letter) {
        // ei toiminnassa!!! 
        return false;
        
        //EmailData emailData = buildEmailData(letter);
        //return checkResponse(emailResourceClient.sendEmail(emailData));
    }
    
    private EmailData buildEmailData(Letter letter) {
        EmailData emailData = new EmailData();
        emailData.setRecipient(getRecipients(letter));
        
        emailData.setEmail(getEmailMessage(letter));
        
        return emailData;
    }
    
    private EmailMessage getEmailMessage(Letter letter) {
        EmailMessage message = new EmailMessage();
        return message;
    }
    
    private List<EmailRecipient> getRecipients(Letter letter) {
        EmailRecipient recipient = new EmailRecipient();
        recipient.setEmail(letter.getEmailAddress());
        recipient.setLanguageCode(letter.getLanguageCode());
        List<EmailRecipient> recipients = new ArrayList<EmailRecipient>();
        return recipients;
    }
    
    private boolean checkResponse(Response response) {
     // if response is ok.. 
        
        return true;
    }
}
