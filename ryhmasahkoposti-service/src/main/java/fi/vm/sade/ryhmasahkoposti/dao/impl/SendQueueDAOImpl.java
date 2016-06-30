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
package fi.vm.sade.ryhmasahkoposti.dao.impl;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.RecipientReportedAttachmentQueryResult;
import fi.vm.sade.ryhmasahkoposti.dao.SendQueueDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipientReplacement;
import fi.vm.sade.ryhmasahkoposti.model.SendQueue;
import fi.vm.sade.ryhmasahkoposti.model.SendQueueState;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

import static fi.vm.sade.ryhmasahkoposti.dao.DaoHelper.count;
import static fi.vm.sade.ryhmasahkoposti.dao.DaoHelper.firstOrNull;

/**
 * User: ratamaa Date: 15.9.2014 Time: 10:09
 */
@Repository
public class SendQueueDAOImpl extends AbstractJpaDAOImpl<SendQueue, Long> implements SendQueueDAO {

    @Override
    public int getNumberOfUnhandledQueues() {
        return count(getEntityManager().createQuery("select count(q.id) from SendQueue q " + " where q.state = :waitState", Number.class).setParameter(
                "waitState", SendQueueState.WAITING_FOR_HANDLER));
    }

    @Override
    public SendQueue findNextAvailableSendQueue() {
        TypedQuery<SendQueue> q = getEntityManager().createQuery("select q from SendQueue q " + " where q.state = :waitState" + " order by q.createdAt, q.id",
                SendQueue.class).setParameter("waitState", SendQueueState.WAITING_FOR_HANDLER);
        return firstOrNull(q);
    }

    @Override
    public List<SendQueue> findActiveQueues() {
        return getEntityManager()
                .createQuery("select q from SendQueue q " + " where (q.state = :processingState)" + " order by q.createdAt, q.id", SendQueue.class)
                .setParameter("processingState", SendQueueState.PROCESSING).getResultList();
    }

    @Override
    public SendQueue getQueue(long id, long version) {
        TypedQuery<SendQueue> q = getEntityManager().createQuery("select q from SendQueue q " + " where q.id = :id and q.version = :version", SendQueue.class)
                .setParameter("id", id).setParameter("version", version);
        return firstOrNull(q);
    }

    @Override
    public List<ReportedRecipient> findUnhandledRecipeientsInQueue(long queueId) {
        return getEntityManager()
                .createQuery(
                        "select recipient\n" + "   from ReportedRecipient recipient\n" + "       inner join recipient.queue q with q.id = :queueId\n"
                                + "where recipient.sendingStarted is null\n" + "order by recipient.timestamp, recipient.id", ReportedRecipient.class)
                .setParameter("queueId", queueId).getResultList();
    }

    @Override
    public List<RecipientReportedAttachmentQueryResult> findRecipientAttachments(List<Long> reportedRecipientIds) {
        if (reportedRecipientIds.isEmpty()) {
            return new ArrayList<>();
        }
        return getEntityManager()
                .createQuery(
                        "select new fi.vm.sade.ryhmasahkoposti.dao.RecipientReportedAttachmentQueryResult(" + " recipient.id, attachment "
                                + ") from ReportedMessageRecipientAttachment rrAttachment "
                                + "   inner join rrAttachment.recipient recipient with recipient.id in (:ids) "
                                + "   inner join rrAttachment.attachment attachment " + "order by recipient.id, attachment.id",
                        RecipientReportedAttachmentQueryResult.class).setParameter("ids", reportedRecipientIds).getResultList();
    }

    @Override
    public List<ReportedRecipientReplacement> findRecipientReplacements(List<Long> reportedRecipientIds) {
        if (reportedRecipientIds.isEmpty()) {
            return new ArrayList<>();
        }
        return getEntityManager()
                .createQuery(
                        "select replacement from ReportedRecipientReplacement replacement" + "       inner join replacement.reportedRecipient recipient "
                                + "           with recipient.id in (:ids) " + "order by recipient.id, replacement.id", ReportedRecipientReplacement.class)
                .setParameter("ids", reportedRecipientIds).getResultList();
    }
}
