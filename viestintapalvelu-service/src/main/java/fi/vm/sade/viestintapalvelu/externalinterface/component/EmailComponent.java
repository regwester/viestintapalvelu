package fi.vm.sade.viestintapalvelu.externalinterface.component;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.viestintapalvelu.externalinterface.api.EmailResource;
import fi.vm.sade.viestintapalvelu.letter.Letter;

@Component
public class EmailComponent {
    @Resource
    private EmailResource emailResourceClient;
    
    public boolean sendEmail(Letter letter) {
        EmailData emailData = buildEmailData(letter);
        return checkResponse(emailResourceClient.sendEmail(emailData));
    }
    
    private EmailData buildEmailData(Letter letter) {
        return new EmailData();
    }
    
    private boolean checkResponse(Response response) {
     // if response is ok.. 
        
        return true;
    }
}
