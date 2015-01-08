package fi.vm.sade.viestintapalvelu.message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.verify;

import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.api.message.MessageData;
import fi.vm.sade.viestintapalvelu.api.message.Receiver;
import fi.vm.sade.viestintapalvelu.asiontitili.AsiointitiliService;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliSendBatchDto;

@RunWith(MockitoJUnitRunner.class)
public class MessageDataResourceTest {
    
    @Mock
    private AsiointitiliService asiointitiliService;
    
    @InjectMocks
    private MessageDataResource resource = new MessageDataResource();
    
    @Test
    public void usesAsiointitiliService() {
        resource.sendMessageViaAsiointiTiliOrEmail(givenMessageData());
        verify(asiointitiliService).send(any(AsiointitiliSendBatchDto.class));
    }
    
    @Test
    public void usesEmailServiceForSendingWhenSsnIsMissing() {
        
    }
    
    private MessageData givenMessageData() {
        return givenMessageData(Arrays.asList(givenReceiver("010101-123N")));
    }
    
    private MessageData givenMessageData(List<Receiver> receivers) {
        return new MessageData("templateName", "FI", receivers, new HashMap<String, Object>());
    }

    private Receiver givenReceiver(String ssn) {
        return new Receiver("1.9.2.134", "test@test.com", new AddressLabel(), new HashMap<String, Object>(), ssn);
    }
    
}
