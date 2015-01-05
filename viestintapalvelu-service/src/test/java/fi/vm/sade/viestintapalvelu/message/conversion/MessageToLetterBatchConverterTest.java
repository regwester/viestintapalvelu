package fi.vm.sade.viestintapalvelu.message.conversion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.api.message.MessageData;
import fi.vm.sade.viestintapalvelu.api.message.Receiver;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchDto;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchLetterDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MessageToLetterBatchConverterTest {
    
    private MessageDataConverter<MessageData, AsyncLetterBatchDto> converter = new MessageToLetterBatchConverter();
    
    @Test
    public void convertsToAsyncLetterBatchDto() {
        MessageData message = givenMessageData();
        assertBatch(message, converter.convert(message).wrapped);
    }
    
    private void assertBatch(MessageData message, AsyncLetterBatchDto batch) {
        assertEquals(message.templateName, batch.getTemplateName());
        assertEquals(message.language, batch.getLanguageCode());
        assertEquals(message.commonReplacements, batch.getTemplateReplacements());
        assertMessages(message.receivers, batch.getLetters());
    }
    
    private void assertMessages(final List<Receiver> receivers, List<AsyncLetterBatchLetterDto> list) {
        for (final Receiver receiver : receivers) {
            assertTrue(Iterables.tryFind(list, new Predicate<AsyncLetterBatchLetterDto>() {

                @Override
                public boolean apply(AsyncLetterBatchLetterDto input) {
                    return receiver.addressLabel.equals(input.getAddressLabel()) && receiver.replacements.equals(input.getTemplateReplacements())
                            && receiver.language.equals(input.getLanguageCode());
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
        return new Receiver("test@test.com", new AddressLabel(), new HashMap<String, Object>(), ssn);
    }
}
