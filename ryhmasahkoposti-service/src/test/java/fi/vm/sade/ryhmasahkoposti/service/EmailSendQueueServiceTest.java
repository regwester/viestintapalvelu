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
package fi.vm.sade.ryhmasahkoposti.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachmentDTO;
import fi.vm.sade.ryhmasahkoposti.converter.EmailMessageDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.EmailRecipientDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedRecipientReplacementConverter;
import fi.vm.sade.ryhmasahkoposti.dao.RecipientReportedAttachmentQueryResult;
import fi.vm.sade.ryhmasahkoposti.dao.SendQueueDAO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.ryhmasahkoposti.model.*;
import fi.vm.sade.ryhmasahkoposti.service.dto.EmailQueueDtoConverter;
import fi.vm.sade.ryhmasahkoposti.service.dto.EmailQueueHandleDto;
import fi.vm.sade.ryhmasahkoposti.service.impl.EmailSendQueueServiceImpl;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;
import fi.vm.sade.ryhmasahkoposti.util.UpdateVersionAnswer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * User: ratamaa Date: 15.9.2014 Time: 16:11
 */
@RunWith(JUnit4.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = { DirtiesContextTestExecutionListener.class })
public class EmailSendQueueServiceTest {
    private static final long TIMEOUT_MILLIS = 60000l;

    private SendQueueDAO sendQueueDao;

    private EmailSendQueueServiceImpl emailSendQueueService;

    @Before
    public void setup() {
        emailSendQueueService = new EmailSendQueueServiceImpl(sendQueueDao, new EmailQueueDtoConverter(), new EmailRecipientDTOConverter(),
                new EmailMessageDTOConverter(), new ReportedRecipientReplacementConverter(), new ObjectMapperProvider());
        sendQueueDao = mock(SendQueueDAO.class);
        emailSendQueueService.setSendQueueDao(sendQueueDao);
        emailSendQueueService.setMaxEmailRecipientHandleTimeMillis(TIMEOUT_MILLIS);
    }

    private EmailSendQueueServiceImpl getEmailSendQueueService() {
        return emailSendQueueService;
    }

    @Test
    public void testNoQueuesToReserve() {
        when(sendQueueDao.findNextAvailableSendQueue()).thenReturn(null);

        assertNull(getEmailSendQueueService().reserveNextQueueToHandle());
    }

    @Test
    public void testReserveQueue() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.WAITING_FOR_HANDLER);
        when(sendQueueDao.findNextAvailableSendQueue()).thenReturn(queue);
        doAnswer(new UpdateVersionAnswer()).when(sendQueueDao).update(any(SendQueue.class));
        ReportedMessage message = RaportointipalveluTestData.getReportedMessage();
        when(sendQueueDao.findUnhandledRecipeientsInQueue(eq(100l))).thenReturn(
                new ArrayList<ReportedRecipient>(Arrays.asList(RaportointipalveluTestData.getReportedRecipient(message))));

        EmailQueueHandleDto result = getEmailSendQueueService().reserveNextQueueToHandle();
        assertNotNull(result);
        assertEquals(SendQueueState.PROCESSING, result.getState());
        assertEquals(100l, result.getId());
        assertEquals(2l, result.getVersion());
        assertNotNull(result.getRecipients());
        assertEquals(1, result.getRecipients().size());
    }

    @Test
    public void testReceiverSpecificAttachments() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.WAITING_FOR_HANDLER);
        when(sendQueueDao.findNextAvailableSendQueue()).thenReturn(queue);
        doAnswer(new UpdateVersionAnswer()).when(sendQueueDao).update(any(SendQueue.class));
        ReportedMessage message = RaportointipalveluTestData.getReportedMessage();

        ReportedAttachment attachment1 = RaportointipalveluTestData.getReportedAttachment(), attachment2 = RaportointipalveluTestData.getReportedAttachment();
        attachment1.setId(301l);
        attachment2.setId(302l);
        ReportedRecipient recipient1 = RaportointipalveluTestData.getReportedRecipient(message), recipient2 = RaportointipalveluTestData
                .getReportedRecipient(message), recipient3 = RaportointipalveluTestData.getReportedRecipient(message);
        recipient1.setId(201l);
        recipient2.setId(202l);
        recipient3.setId(203l);

        when(sendQueueDao.findUnhandledRecipeientsInQueue(eq(100l))).thenReturn(Arrays.asList(recipient1, recipient2, recipient3));
        when(sendQueueDao.findRecipientAttachments(any(List.class))).thenReturn(
                Arrays.asList(new RecipientReportedAttachmentQueryResult(201l, attachment1), new RecipientReportedAttachmentQueryResult(201l, attachment2),
                        new RecipientReportedAttachmentQueryResult(202l, attachment1)));

        EmailQueueHandleDto result = getEmailSendQueueService().reserveNextQueueToHandle();
        assertNotNull(result);
        assertEquals(100l, result.getId());
        assertEquals(2l, result.getVersion());

        // 3 recipients:
        assertNotNull(result.getRecipients());
        assertEquals(3, result.getRecipients().size());

        // first being 201 with attachments 301 and 302:
        assertEquals(Long.valueOf(201l), result.getRecipients().get(0).getRecipientID());
        assertNotNull(result.getRecipients().get(0).getAttachments());
        assertEquals(2, result.getRecipients().get(0).getAttachments().size());
        assertTrue(result.getRecipients().get(0).getAttachments().get(0) instanceof EmailAttachmentDTO);
        assertEquals(Long.valueOf(301l), ((EmailAttachmentDTO) result.getRecipients().get(0).getAttachments().get(0)).getAttachmentID());
        assertEquals(Long.valueOf(302l), ((EmailAttachmentDTO) result.getRecipients().get(0).getAttachments().get(1)).getAttachmentID());

        // second being 202 with attachment 301:
        assertEquals(Long.valueOf(202l), result.getRecipients().get(1).getRecipientID());
        assertEquals(1, result.getRecipients().get(1).getAttachments().size());
        assertEquals(Long.valueOf(301l), ((EmailAttachmentDTO) result.getRecipients().get(1).getAttachments().get(0)).getAttachmentID());

        // and third 203 with empty attachment list:
        assertEquals(Long.valueOf(203l), result.getRecipients().get(2).getRecipientID());
        assertNotNull(result.getRecipients().get(2).getAttachments());
        assertEquals(0, result.getRecipients().get(2).getAttachments().size());
    }

    @Test
    public void testContinueQueueHandling() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.PROCESSING);
        queue.setLastHandledAt(new Date());
        when(sendQueueDao.getQueue(eq(100l), eq(1l))).thenReturn(queue);

        EmailQueueHandleDto queueDto = new EmailQueueHandleDto();
        queueDto.setId(100l);
        queueDto.setVersion(1l);
        assertTrue(getEmailSendQueueService().continueQueueHandling(queueDto));
        assertNotNull(queue.getLastHandledAt());
        assertNull(queue.getFinishedAt());
    }

    @Test
    public void testNotContinueQueueHandling() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.PROCESSING);
        when(sendQueueDao.getQueue(eq(100l), eq(1l))).thenReturn(queue);

        EmailQueueHandleDto queueDto = new EmailQueueHandleDto();
        queueDto.setId(100l);
        queueDto.setVersion(2l);
        assertFalse(getEmailSendQueueService().continueQueueHandling(queueDto));
    }

    @Test
    public void testNotContinueQueueHandlingDueToTooOld() {
        Date now = new Date();
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.PROCESSING);
        queue.setLastHandledAt(new Date(now.getTime() - getEmailSendQueueService().getMaxEmailRecipientHandleTimeMillis() - 1));
        when(sendQueueDao.getQueue(eq(100l), eq(1l))).thenReturn(queue);

        EmailQueueHandleDto queueDto = new EmailQueueHandleDto();
        queueDto.setId(100l);
        queueDto.setVersion(1l);
        assertFalse(getEmailSendQueueService().continueQueueHandling(queueDto));
    }

    @Test
    public void testNotContinueQueueHandlingDueToFalseState() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.WAITING_FOR_HANDLER);
        when(sendQueueDao.getQueue(eq(100l), eq(1l))).thenReturn(queue);

        EmailQueueHandleDto queueDto = new EmailQueueHandleDto();
        queueDto.setId(100l);
        queueDto.setVersion(1l);
        assertFalse(getEmailSendQueueService().continueQueueHandling(queueDto));
    }

    @Test
    public void testNotContinueQueueHandlingDueToFalseState2() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.PROCESSING);
        queue.setFinishedAt(new Date());
        when(sendQueueDao.getQueue(eq(100l), eq(1l))).thenReturn(queue);

        EmailQueueHandleDto queueDto = new EmailQueueHandleDto();
        queueDto.setId(100l);
        queueDto.setVersion(1l);
        assertFalse(getEmailSendQueueService().continueQueueHandling(queueDto));
    }

    @Test
    public void testQueueHandled() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.PROCESSING);
        when(sendQueueDao.read(eq(100l))).thenReturn(queue);
        doAnswer(new UpdateVersionAnswer()).when(sendQueueDao).update(any(SendQueue.class));

        getEmailSendQueueService().queueHandled(100l);
        assertEquals(SendQueueState.READY, queue.getState());
        assertNotNull(queue.getFinishedAt());
        assertEquals(Long.valueOf(2l), queue.getVersion());
    }

    @Test(expected = IllegalStateException.class)
    public void testQueueHandledForIncorrectQueue() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.READY);
        when(sendQueueDao.read(eq(100l))).thenReturn(queue);

        getEmailSendQueueService().queueHandled(100l);
    }

    @Test
    public void testCheckForStoppedProcesses() {
        Date now = new Date();
        SendQueue queue1 = RaportointipalveluTestData.sendQueue(100l, SendQueueState.PROCESSING), queue2 = RaportointipalveluTestData.sendQueue(200l,
                SendQueueState.PROCESSING), queue3 = RaportointipalveluTestData.sendQueue(300l, SendQueueState.PROCESSING);
        queue1.setLastHandledAt(new Date(now.getTime() - getEmailSendQueueService().getMaxEmailRecipientHandleTimeMillis() + 1000l));
        queue2.setLastHandledAt(new Date(now.getTime() - getEmailSendQueueService().getMaxEmailRecipientHandleTimeMillis() - 1));
        queue3.setLastHandledAt(null);
        when(sendQueueDao.findActiveQueues()).thenReturn(new ArrayList<SendQueue>(Arrays.asList(queue1, queue2, queue3)));

        getEmailSendQueueService().checkForStoppedProcesses();
        assertEquals(SendQueueState.PROCESSING, queue1.getState());
        assertEquals(SendQueueState.WAITING_FOR_HANDLER, queue2.getState());
        assertEquals(SendQueueState.PROCESSING, queue3.getState());
    }

    @Test
    public void testGetNumberOfUnhandledQueues() {
        when(sendQueueDao.getNumberOfUnhandledQueues()).thenReturn(15);
        assertEquals(15, getEmailSendQueueService().getNumberOfUnhandledQueues());
    }
}
