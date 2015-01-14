/**
 * Copyright (c) 2015 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */
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
    
    @Test
    public void doesNotConvertReceiversWithoutEmail() {
        MessageData message = givenMessageData(Arrays.asList(givenReceiver("")));
        ConvertedMessageWrapper<EmailData> wrapper = converter.convert(message);
        assertTrue(wrapper.wrapped.getRecipient().isEmpty());
        assertEquals(1, wrapper.incompatibleReceivers.size());
    }

    private void assertEmail(MessageData message, EmailData email) {
        assertEquals(message.templateName, email.getEmail().getTemplateName());
        assertEquals(message.language, email.getEmail().getLanguageCode());
        assertEquals(message.commonReplacements.size(), email.getReplacements().size());
        assertEquals(message.applicationTargetOid, email.getEmail().getHakuOid());
        assertEquals(message.organizationOid, email.getEmail().getOrganizationOid());
        assertEquals(message.senderOid, email.getEmail().getSenderOid());
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
        return givenMessageData(Arrays.asList(givenReceiver("test@test.com")));
    }

    private MessageData givenMessageData(List<Receiver> receivers) {
        return new MessageData("templateName", "FI", receivers, new HashMap<String, Object>(), "1.9.2.44", "1.2342.2323", "1.2.3323");
    }

    private Receiver givenReceiver(String email) {
        return new Receiver("1.9.2.455", email, new AddressLabel(), new HashMap<String, Object>(), null);
    }
}
