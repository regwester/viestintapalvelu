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

package fi.vm.sade.ryhmasahkoposti.service;

import fi.vm.sade.ryhmasahkoposti.service.dto.EmailQueueHandleDto;

/**
 * User: ratamaa
 * Date: 15.9.2014
 * Time: 10:39
 */
public interface EmailSendQueueService {

    /**
     * Reserves next SendQueue for the calling process.
     *
     * @return a queue to be handled or null if no queues exists
     */
    EmailQueueHandleDto reserveNextQueueToHandle();

    /**
     * @return the number of unhandled message queues
     */
    int getNumberOfUnhandledQueues();

    /**
     * Check for email SendQueues to release from PROCESSING state because the last message
     * sending has taken too long.
     */
    void checkForStoppedProcesses();

    /**
     * Call this before handling a recipient in send queue.
     *
     * Marks the lastHandledAt date to current time for given queue.
     *
     * @param queue to be handled
     * @return true iff queue handling should be continued
     */
    boolean continueQueueHandling(EmailQueueHandleDto queue);

    /**
     * Call this after queue processor has handled all the recipients.
     *
     * Sets the SendQueue READY.
     *
     * @param queueId id of the SendQueue
     */
    void queueHandled(long queueId);
}
