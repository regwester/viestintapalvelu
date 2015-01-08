/**
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
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
package fi.vm.sade.viestintapalvelu.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.api.message.MessageData;
import fi.vm.sade.viestintapalvelu.api.message.MessageStatusResponse;
import fi.vm.sade.viestintapalvelu.api.rest.MessageResource;
import fi.vm.sade.viestintapalvelu.asiontitili.AsiointitiliService;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliAsyncResponseDto;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliSendBatchDto;
import fi.vm.sade.viestintapalvelu.letter.LetterService;
import fi.vm.sade.viestintapalvelu.message.conversion.ConvertedMessageWrapper;
import fi.vm.sade.viestintapalvelu.message.conversion.MessageToAsiointiTiliConverter;
import fi.vm.sade.viestintapalvelu.message.conversion.MessageToLetterBatchConverter;

/**
 * @author risal1
 *
 */
@Component
public class MessageDataResource implements MessageResource {
    
    @Autowired
    private AsiointitiliService asiointitiliService;
    
    @Autowired
    private LetterService letterService;
    
    private MessageToAsiointiTiliConverter asiointiliConverter = new MessageToAsiointiTiliConverter();
    
    private MessageToLetterBatchConverter letterBatchConverter = new MessageToLetterBatchConverter();
    
    /* (non-Javadoc)
     * @see fi.vm.sade.viestintapalvelu.api.rest.MessageResource#sendMessageViaAsiointiTiliOrEmail(fi.vm.sade.viestintapalvelu.api.message.MessageData)
     */
    @Override
    public MessageStatusResponse sendMessageViaAsiointiTiliOrEmail(MessageData messageData) {
        ConvertedMessageWrapper<AsiointitiliSendBatchDto> wrapper = asiointiliConverter.convert(messageData);
        AsiointitiliAsyncResponseDto response = asiointitiliService.send(wrapper.wrapped);
        //TODO: Additional checks
        //TODO: need to also use receivers that didn't get message via asiointitili in otherways
        if (!wrapper.incompatibleReceivers.isEmpty()) {
            //TODO build emaildata and use ryhmasahkoposti //use pdfprinterresource for attachment
            //TODO: email sending, how will we proceed? should we store something to db even though Asiointitili seems not to do so
        }
        return null;
    }

}
