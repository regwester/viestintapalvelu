/*
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

package fi.vm.sade.ajastuspalvelu.service.runner.dto.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import fi.vm.sade.ajastuspalvelu.api.dto.ReceiverItem;
import fi.vm.sade.ajastuspalvelu.api.dto.SchedulerResponse;
import fi.vm.sade.ajastuspalvelu.service.external.email.dto.EmailDetailsDto;
import fi.vm.sade.ajastuspalvelu.service.external.email.dto.EmailReceiver;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.ScheduledTaskExecutionDetailsDto;

/**
 * User: ratamaa
 * Date: 23.10.2014
 * Time: 15:42
 */
@Component
public class EmailDetailsDtoConverter {

    public EmailDetailsDto convert(ScheduledTaskExecutionDetailsDto from, SchedulerResponse response,
                                   EmailDetailsDto to) {
        to.setHakuOid(from.getHakuOid());
        to.setTemplateName(from.getTemplateName());
        if (response.getCommonContext() != null) {
            to.setReplacements(response.getCommonContext());
        } else {
            to.setReplacements(new HashMap<String, Object>());
        }
        to.setReceivers(convert(response.getItems(), new ArrayList<EmailReceiver>()));
        return to;
    }

    public List<EmailReceiver> convert(List<ReceiverItem> from, ArrayList<EmailReceiver> to) {
        to.clear();
        for (ReceiverItem fromItem : from) {
            to.add(convert(fromItem, new EmailReceiver()));
        }
        return to;
    }

    public EmailReceiver convert(ReceiverItem from, EmailReceiver to) {
        // TODO: what if from.getEmail() is null? fetch info from henkiloService?
        to.setEmail(from.getEmail());
        to.setOidType(from.getOidType());
        to.setOid(from.getOid());
        if (from.getContext() != null) {
            to.setReplacements(from.getContext());
        } else {
            to.setReplacements(new HashMap<String, Object>());
        }
        return to;
    }
}
