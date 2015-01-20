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

import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.viestintapalvelu.api.message.MessageData;
import fi.vm.sade.viestintapalvelu.api.message.MessageStatusResponse;
import fi.vm.sade.viestintapalvelu.api.message.MessageStatusResponse.State;
import fi.vm.sade.viestintapalvelu.api.message.Receiver;
import fi.vm.sade.viestintapalvelu.api.message.ReceiverStatus;
import fi.vm.sade.viestintapalvelu.api.message.ReceiverStatus.SendMethod;
import fi.vm.sade.viestintapalvelu.api.rest.MessageResource;
import fi.vm.sade.viestintapalvelu.asiontitili.AsiointitiliService;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliAsyncResponseDto;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliReceiverStatusDto;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliSendBatchDto;
import fi.vm.sade.viestintapalvelu.externalinterface.component.EmailComponent;
import fi.vm.sade.viestintapalvelu.message.conversion.ConvertedMessageWrapper;
import fi.vm.sade.viestintapalvelu.message.conversion.MessageToAsiointiTiliConverter;
import fi.vm.sade.viestintapalvelu.message.conversion.MessageToEmailConverter;

/**
 * @author risal1
 *
 */
@Component
public class MessageDataResource implements MessageResource {
    
    private static final Logger LOGGER = Logger.getLogger(MessageDataResource.class);
    
    @Autowired
    private AsiointitiliService asiointitiliService;
    
    @Autowired
    private EmailComponent emailComponent;

    private MessageToAsiointiTiliConverter asiointiliConverter = new MessageToAsiointiTiliConverter();

    private MessageToEmailConverter emailConverter = new MessageToEmailConverter();

    /*
     * (non-Javadoc)
     * 
     * @see fi.vm.sade.viestintapalvelu.api.rest.MessageResource#
     * sendMessageViaAsiointiTiliOrEmail
     * (fi.vm.sade.viestintapalvelu.api.message.MessageData)
     */
    @Override
    public MessageStatusResponse sendMessageViaAsiointiTiliOrEmail(MessageData messageData) {
        try {
            ConvertedMessageWrapper<AsiointitiliSendBatchDto> wrapper = asiointiliConverter.convert(messageData);
            AsiointitiliAsyncResponseDto response = asiointitiliService.send(wrapper.wrapped);
            List<Receiver> failedReceivers = getFailedReceiversForAsiointiTili(response.getReceiverStatuses(), messageData.receivers);
            MessageStatusResponse messageResponse = constructMessageResponse(response);
            if (!wrapper.incompatibleReceivers.isEmpty() || !failedReceivers.isEmpty()) {
                failedReceivers.addAll(wrapper.incompatibleReceivers);
                ConvertedMessageWrapper<EmailData> emailWrapper = emailConverter.convert(messageData.copyOf(failedReceivers));
                emailComponent.sendEmail(emailWrapper.wrapped);
                messageResponse = messageResponse.copyWithAdditionalReceiverStatuses(constructMessageResponseReceiverStatusesForEmail(emailWrapper));
            }
            return messageResponse;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());            
            return constructErrorMessageResponse(messageData, e);
        }
    }

    private List<Receiver> getFailedReceiversForAsiointiTili(final List<AsiointitiliReceiverStatusDto> receiverStatuses, List<Receiver> receivers) {
       return ImmutableList.copyOf(Iterables.filter(receivers, new Predicate<Receiver>() {

                @Override
                public boolean apply(final Receiver receiver) {
                    return Iterables.tryFind(receiverStatuses, new Predicate<AsiointitiliReceiverStatusDto>() {

                        @Override
                        public boolean apply(AsiointitiliReceiverStatusDto input) {
                            return input.getReceiverOid().equals(receiver.oid) && input.getStateCode() != Status.OK.getStatusCode();
                        }
                    }).isPresent();
                }
                
            }));
    }

    private List<ReceiverStatus> constructMessageResponseReceiverStatusesForEmail(ConvertedMessageWrapper<EmailData> emailWrapper) {
        List<ReceiverStatus> receiverStatuses = Lists.transform(emailWrapper.wrapped.getRecipient(), new Function<EmailRecipient, ReceiverStatus>() {

            @Override
            public ReceiverStatus apply(EmailRecipient input) {
                return new ReceiverStatus(input.getOid(), SendMethod.EMAIL);
            }
            
        });
        for (Receiver receiver : emailWrapper.incompatibleReceivers) {
            receiverStatuses.add(new ReceiverStatus(receiver.oid, SendMethod.NONE, true, "No suitable email or asiointili given for receiver"));
        }
        return receiverStatuses;
    }

    private MessageStatusResponse constructMessageResponse(AsiointitiliAsyncResponseDto response) {
        List<ReceiverStatus> receiverStatuses = Lists.transform(response.getReceiverStatuses(), new Function<AsiointitiliReceiverStatusDto, ReceiverStatus>() {

            @Override
            public ReceiverStatus apply(AsiointitiliReceiverStatusDto input) {
                boolean sendingFailed = input.getStateCode() != Status.OK.getStatusCode();
                return new ReceiverStatus(input.getReceiverOid(), SendMethod.ASIOINTITILI, sendingFailed, sendingFailed ? input.getStateDescription() : null);
            }
            
        });
        boolean sendingSucceeded = response.getStatusCode() == Status.OK.getStatusCode();
        return new MessageStatusResponse(sendingSucceeded ? State.PROCESSED : State.ERROR, sendingSucceeded ? null : response.getDescription(), null, receiverStatuses);
    }

    private MessageStatusResponse constructErrorMessageResponse(MessageData messageData, final Exception e) {
        List<ReceiverStatus> receiverStatuses = Lists.transform(messageData.receivers, new Function<Receiver, ReceiverStatus>() {

            @Override
            public ReceiverStatus apply(Receiver input) {
                return new ReceiverStatus(input.oid, SendMethod.ASIOINTITILI, true, e.getMessage());
            }
            
        });
        return new MessageStatusResponse(State.ERROR, e.getMessage(), null, receiverStatuses);
    }

}
