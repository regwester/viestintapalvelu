/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
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
        return new MessageData("templateName", "FI", receivers, new HashMap<String, Object>(), null, null, null);
    }

    private Receiver givenReceiver(String ssn) {
        return new Receiver("1.9.2.134", "test@test.com", new AddressLabel(), new HashMap<String, Object>(), ssn);
    }
    
}
