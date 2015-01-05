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
package fi.vm.sade.viestintapalvelu.message.conversion;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import fi.vm.sade.viestintapalvelu.api.message.MessageData;
import fi.vm.sade.viestintapalvelu.api.message.Receiver;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliMessageDto;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliSendBatchDto;

/**
 * @author risal1
 *
 */
@Component
public class MessageToAsiointiTiliConverter implements MessageDataConverter<MessageData, AsiointitiliSendBatchDto> {

    @Override
    public AsiointitiliSendBatchDto convert(MessageData data) {
        AsiointitiliSendBatchDto batch = new AsiointitiliSendBatchDto();
        batch.setTemplateName(data.templateName);
        batch.setTemplateReplacements(data.commonReplacements);
        batch.setLanguageCode(data.language);
        batch.setMessages(convertMessages(data.receivers));
        //TODO optional fields? are they necessary to convert here?
        return batch;
    }

    private List<AsiointitiliMessageDto> convertMessages(List<Receiver> receivers) {
        return Lists.transform(receivers, new Function<Receiver, AsiointitiliMessageDto>() {

            @Override
            public AsiointitiliMessageDto apply(Receiver input) {
                AsiointitiliMessageDto dto = new AsiointitiliMessageDto();
                dto.setAddressLabel(input.addressLabel);
                dto.setTemplateReplacements(input.replacements);
                dto.setReceiverHetu(input.hetu);
                //TODO: dto.setLanguageCode()?
                return dto;
            }
            
        });
    }

}
