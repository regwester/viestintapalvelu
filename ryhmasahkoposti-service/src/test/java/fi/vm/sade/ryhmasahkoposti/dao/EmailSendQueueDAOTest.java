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
package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.model.*;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

import static org.junit.Assert.*;

/**
 * User: ratamaa Date: 15.9.2014 Time: 17:10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class })
@Transactional(readOnly = true)
public class EmailSendQueueDAOTest {
    @Autowired
    private SendQueueDAO sendQueueDAO;

    @Autowired
    private ReportedMessageDAO reportedMessageDAO;

    @Autowired
    private ReportedRecipientDAO reportedRecipientDAO;

    @Autowired
    private ReportedAttachmentDAO reportedAttachmentDAO;

    @Test
    public void testGetQueueByIdAndVersion() {
        SendQueue queue = new SendQueue();
        sendQueueDAO.insert(queue);

        SendQueue result = sendQueueDAO.getQueue(queue.getId(), queue.getVersion());
        assertNotNull(result);

        result = sendQueueDAO.getQueue(queue.getId(), queue.getVersion() - 1);
        assertNull(result);
    }

    @Test
    public void testFindNextAvailableSendQueue() {
        SendQueue queue1 = new SendQueue(), queue2 = new SendQueue(), queue3 = new SendQueue(), queue4 = new SendQueue(), queue5 = new SendQueue();
        queue1.setState(SendQueueState.CREATED);
        queue2.setState(SendQueueState.PROCESSING);
        queue3.setState(SendQueueState.WAITING_FOR_HANDLER);
        queue4.setState(SendQueueState.WAITING_FOR_HANDLER);
        queue5.setState(SendQueueState.READY);
        sendQueueDAO.insert(queue1);
        sendQueueDAO.insert(queue2);
        sendQueueDAO.insert(queue3);
        sendQueueDAO.insert(queue4);
        sendQueueDAO.insert(queue5);

        SendQueue queue = sendQueueDAO.findNextAvailableSendQueue();
        assertNotNull(queue);
        assertEquals(queue3.getId(), queue.getId());
    }

    @Test
    public void testFindNextAvailableSendQueueWhenNone() {
        SendQueue queue1 = new SendQueue();
        queue1.setState(SendQueueState.READY);
        sendQueueDAO.insert(queue1);

        SendQueue queue = sendQueueDAO.findNextAvailableSendQueue();
        assertNull(queue);
    }

    @Test
    public void testFindActiveQueues() {
        SendQueue queue1 = new SendQueue(), queue2 = new SendQueue();
        queue2.setState(SendQueueState.PROCESSING);
        sendQueueDAO.insert(queue1);
        sendQueueDAO.insert(queue2);

        List<SendQueue> results = sendQueueDAO.findActiveQueues();
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(queue2.getId(), results.get(0).getId());
    }

    @Test
    public void testFindActiveQueuesWhenNone() {
        List<SendQueue> results = sendQueueDAO.findActiveQueues();
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testFindUnhandledRecipeientsInQueue() {
        ReportedMessage message = RaportointipalveluTestData.getReportedMessage();
        reportedMessageDAO.insert(message);
        ReportedRecipient recipient1 = RaportointipalveluTestData.getReportedRecipient(message), recipient2 = RaportointipalveluTestData
                .getReportedRecipient(message), recipient3 = RaportointipalveluTestData.getReportedRecipient(message), recipient4 = RaportointipalveluTestData
                .getReportedRecipient(message);
        recipient1.setSendingStarted(null);
        recipient2.setSendingStarted(null);
        recipient3.setSendingStarted(null);
        recipient4.setSendingStarted(new Date());
        reportedRecipientDAO.insert(recipient1);
        reportedRecipientDAO.insert(recipient2);
        reportedRecipientDAO.insert(recipient3);
        reportedRecipientDAO.insert(recipient4);
        SendQueue queue = RaportointipalveluTestData.sendQueue(null, SendQueueState.PROCESSING);
        sendQueueDAO.insert(queue);
        recipient1.setQueue(queue);
        queue.getRecipients().add(recipient1);
        recipient3.setQueue(queue);
        queue.getRecipients().add(recipient3);
        recipient4.setQueue(queue);
        queue.getRecipients().add(recipient4);
        reportedRecipientDAO.update(recipient1);
        reportedRecipientDAO.update(recipient3);
        reportedRecipientDAO.update(recipient4);

        List<ReportedRecipient> recipients = sendQueueDAO.findUnhandledRecipeientsInQueue(queue.getId());
        assertNotNull(recipients);
        assertEquals(2, recipients.size());
        assertEquals(recipient1.getId(), recipients.get(0).getId());
        assertEquals(recipient3.getId(), recipients.get(1).getId());
    }

    @Test
    public void testGetNumberOfUnhandledQueues() {
        SendQueue queue1 = new SendQueue(), queue2 = new SendQueue(), queue3 = new SendQueue(), queue4 = new SendQueue(), queue5 = new SendQueue();
        queue1.setState(SendQueueState.CREATED);
        queue2.setState(SendQueueState.PROCESSING);
        queue3.setState(SendQueueState.WAITING_FOR_HANDLER);
        queue4.setState(SendQueueState.WAITING_FOR_HANDLER);
        queue5.setState(SendQueueState.READY);
        sendQueueDAO.insert(queue1);
        sendQueueDAO.insert(queue2);
        sendQueueDAO.insert(queue3);
        sendQueueDAO.insert(queue4);
        sendQueueDAO.insert(queue5);

        assertEquals(2, sendQueueDAO.getNumberOfUnhandledQueues());
    }

    @Test
    public void testFindRecipientAttachmentsWithEmptyIdList() {
        assertEquals(0, sendQueueDAO.findRecipientAttachments(new ArrayList<Long>()).size());
    }

    @Test
    public void testFindRecipientAttachments() {
        assertEquals(0, sendQueueDAO.findRecipientAttachments(Arrays.asList(1l)).size());

        ReportedMessage message = RaportointipalveluTestData.getReportedMessage();
        reportedMessageDAO.insert(message);
        ReportedAttachment attachment1 = RaportointipalveluTestData.getReportedAttachment(), attachment2 = RaportointipalveluTestData.getReportedAttachment();
        reportedAttachmentDAO.saveReportedAttachments(Arrays.asList(attachment1, attachment2));

        ReportedRecipient recipient1 = RaportointipalveluTestData.getReportedRecipient(message), recipient2 = RaportointipalveluTestData
                .getReportedRecipient(message), recipient3 = RaportointipalveluTestData.getReportedRecipient(message), recipient4 = RaportointipalveluTestData
                .getReportedRecipient(message);
        reportedRecipientDAO.insert(recipient1);
        reportedRecipientDAO.insert(recipient2);
        reportedRecipientDAO.insert(recipient3);
        reportedRecipientDAO.insert(recipient4);

        // Not found yet:
        assertEquals(0, sendQueueDAO.findRecipientAttachments(Arrays.asList(recipient1.getId())).size());

        reportedAttachmentDAO.insertAndFlush(new ReportedMessageRecipientAttachment(recipient1, attachment1));
        reportedAttachmentDAO.insertAndFlush(new ReportedMessageRecipientAttachment(recipient1, attachment2));
        reportedAttachmentDAO.insertAndFlush(new ReportedMessageRecipientAttachment(recipient2, attachment1));
        reportedAttachmentDAO.insertAndFlush(new ReportedMessageRecipientAttachment(recipient4, attachment1));

        List<RecipientReportedAttachmentQueryResult> results = sendQueueDAO.findRecipientAttachments(Arrays.asList(recipient1.getId(), recipient2.getId(),
                recipient3.getId()));

        // Should not contain the attachment from recipient4 or any row for
        // recipient3
        assertEquals(3, results.size());

        // Should contain the correct results in recipient, attachment pk order:
        assertEquals(attachment1.getId(), results.get(0).getAttachment().getId());
        assertEquals(recipient1.getId(), results.get(0).getReportedRecipientId());

        assertEquals(attachment2.getId(), results.get(1).getAttachment().getId());
        assertEquals(recipient1.getId(), results.get(1).getReportedRecipientId());

        assertEquals(attachment1.getId(), results.get(2).getAttachment().getId());
        assertEquals(recipient2.getId(), results.get(2).getReportedRecipientId());
    }

    @Test
    public void testFindRecipientReplacements() {
        // TODO
    }
}
