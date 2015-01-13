package fi.vm.sade.viestintapalvelu.message.conversion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.api.message.MessageData;
import fi.vm.sade.viestintapalvelu.api.message.Receiver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MessageToEmailConverterTest {
    
    private MessageDataConverter<MessageData, EmailData> converter = new MessageToEmailConverter();
    
    @Test
    public void convertsToEmail() {
        MessageData message = givenMessageData();
        assertEmail(message, converter.convert(message).wrapped);
    }
    
    private void assertEmail(MessageData message, EmailData email) {
        assertEquals(message.templateName, email.getEmail().getTemplateName());
        assertEquals(message.language, email.getEmail().getLanguageCode());
        assertEquals(message.commonReplacements.size(), email.getReplacements().size());
        assertMessages(message.receivers, email.getRecipient());
    }
    
    private void assertMessages(final List<Receiver> receivers, List<EmailRecipient> list) {
        for (final Receiver receiver : receivers) {
            assertTrue(Iterables.tryFind(list, new Predicate<EmailRecipient>() {

                @Override
                public boolean apply(EmailRecipient input) {
                    return receiver.email.equals(input.getEmail()) && receiver.language.equals(input.getLanguageCode());
                }
                
            }).isPresent());
        }
    }

    private MessageData givenMessageData() {
        return givenMessageData(Arrays.asList(givenReceiver("010101-123N")));
    }
    
    private MessageData givenMessageData(List<Receiver> receivers) {
        return new MessageData("templateName", "FI", receivers, new HashMap<String, Object>());
    }

    private Receiver givenReceiver(String ssn) {
        return new Receiver("1.9.2.455", "test@test.com", new AddressLabel(), new HashMap<String, Object>(), ssn);
    }
}
