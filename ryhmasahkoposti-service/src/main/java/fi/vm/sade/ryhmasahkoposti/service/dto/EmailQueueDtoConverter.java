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
package fi.vm.sade.ryhmasahkoposti.service.dto;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.model.SendQueue;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: ratamaa
 * Date: 15.9.2014
 * Time: 10:56
 */
@Component
public class EmailQueueDtoConverter {

    public EmailQueueHandleDto convert(SendQueue from, EmailQueueHandleDto to, List<EmailRecipientDTO> recipients) {
        to.setId(from.getId());
        to.setVersion(from.getVersion());
        to.setState(from.getState());
        to.setRecipients(recipients);
        return to;
    }

}
