package fi.vm.sade.viestintapalvelu.externalinterface.component;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.viestintapalvelu.email.EmailBuilder;
import fi.vm.sade.viestintapalvelu.externalinterface.api.EmailResource;
import fi.vm.sade.viestintapalvelu.letter.Letter;

@Component
public class EmailComponent {
    @Resource
    private EmailResource emailResourceClient;
    
    @Autowired
    private EmailBuilder emailBuilder;
    
    public boolean sendEmail(Letter letter) {
        EmailData emailData;
        try {
            emailData = emailBuilder.buildEmailData(letter);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return checkResponse(emailResourceClient.sendEmail(emailData));
    }
    
    private boolean checkResponse(Response response) {
     // if response is ok.. 
        
        return true;
    }
}
