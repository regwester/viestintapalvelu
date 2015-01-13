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
package fi.vm.sade.viestintapalvelu.asiontitili;

import javax.ws.rs.NotFoundException;

import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliAsyncResponseDto;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.AsiointitiliSendBatchDto;

/**
 * User: ratamaa
 * Date: 27.10.2014
 * Time: 14:38
 */
public interface AsiointitiliService {

    /**
     * Firsts checks for customer states, fetches the related template and sends messages to those
     * receivers having an Asiointitili.
     *
     * @param sendBatch the message to send
     * @return the status for the send process
     * @throws javax.ws.rs.NotFoundException if template was not found
     */
    AsiointitiliAsyncResponseDto send(AsiointitiliSendBatchDto sendBatch) throws NotFoundException;

}
