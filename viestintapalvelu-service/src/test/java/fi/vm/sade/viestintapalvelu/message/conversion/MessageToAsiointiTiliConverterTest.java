package fi.vm.sade.viestintapalvelu.message.conversion;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import fi.vm.sade.viestintapalvelu.api.message.MessageData;
import fi.vm.sade.viestintapalvelu.api.message.Receiver;
import static org.junit.Assert.assertNotNull;

public class MessageToAsiointiTiliConverterTest {
    
    private MessageToAsiointiTiliConverter converter = new MessageToAsiointiTiliConverter();
    
    @Test
    public void convertsToAsiointiTiliSendBatchDto() {
        assertNotNull(converter.convert(givenMessageData()));
    }
    
    private MessageData givenMessageData() {
        return new MessageData("templateName", "FI", new ArrayList<Receiver>(), new HashMap<String, Object>());
    }
}
