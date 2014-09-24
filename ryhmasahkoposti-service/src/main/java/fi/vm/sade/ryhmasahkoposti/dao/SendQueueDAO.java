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

package fi.vm.sade.ryhmasahkoposti.dao;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.SendQueue;

import java.util.List;

/**
 * User: ratamaa
 * Date: 15.9.2014
 * Time: 10:08
 */
public interface SendQueueDAO extends JpaDAO<SendQueue, Long> {

    /**
     * @return the next queue in WAITING_FOR_HANDLER state by creation time and id
     */
    SendQueue findNextAvailableSendQueue();

    /**
     * @return all queues in PROCESSING state
     */
    List<SendQueue> findActiveQueues();

    /**
     * @param id of the queue
     * @param version of the queue
     * @return the queue or null if not found
     */
    SendQueue getQueue(long id, long version);

    /**
     * @param queueId id of the SendQueue
     * @return the reported recipients whose sending has not been started for given queue in
     * their timstamp order
     */
    List<ReportedRecipient> findUnhandledRecipeientsInQueue(long queueId);

    /**
     * @param reportedRecipientIds ids of ReportedRecipients for which to collect all ReportedAttachment
     *                             related to their ReportedMessageRecipientAttachments
     * @return reported attachments for given recipient ids (with related ID to avoid unnecessary fetching)
     */
    List<RecipientReportedAttachmentQueryResult> findRecipientAttachments(List<Long> reportedRecipientIds);

    /**
     * @return the number of unhandled message queues
     */
    int getNumberOfUnhandledQueues();
}
