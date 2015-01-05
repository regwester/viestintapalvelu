package fi.vm.sade.viestintapalvelu.message.conversion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.api.message.MessageData;
import fi.vm.sade.viestintapalvelu.api.message.Receiver;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliMessageDto;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliSendBatchDto;

public class MessageToAsiointiTiliConverterTest {
    
    private MessageToAsiointiTiliConverter converter = new MessageToAsiointiTiliConverter();
    
    @Test
    public void convertsToAsiointiTiliSendBatchDto() {
        MessageData message = givenMessageData();
        assertBatch(message, converter.convert(message));
    }

    private void assertBatch(MessageData message, AsiointitiliSendBatchDto batch) {
        assertEquals(message.templateName, batch.getTemplateName());
        assertEquals(message.language, batch.getLanguageCode());
        assertEquals(message.commonReplacements, batch.getTemplateReplacements());
        assertMessages(message.receivers, batch.getMessages());
    }
    
    private void assertMessages(final List<Receiver> receivers, List<AsiointitiliMessageDto> messages) {
        for (final Receiver receiver : receivers) {
            assertTrue(Iterables.tryFind(messages, new Predicate<AsiointitiliMessageDto>() {

                @Override
                public boolean apply(AsiointitiliMessageDto input) {
                    return receiver.addressLabel.equals(input.getAddressLabel()) && receiver.hetu.equals(input.getReceiverHetu()) && receiver.replacements.equals(input.getTemplateReplacements());
                }
                
            }).isPresent());
        }
        
    }

    private MessageData givenMessageData() {
        Receiver receiver = new Receiver("test@test.com", new AddressLabel(), new HashMap<String, Object>(), "010101-123N");
        return new MessageData("templateName", "FI", Arrays.asList(receiver), new HashMap<String, Object>());
    }
}
