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

import fi.vm.sade.ryhmasahkoposti.common.util.MessageUtil;
import fi.vm.sade.ryhmasahkoposti.converter.EmailRecipientDTOConverter;
import fi.vm.sade.ryhmasahkoposti.dao.SendQueueDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.SendQueue;
import fi.vm.sade.ryhmasahkoposti.model.SendQueueState;
import fi.vm.sade.ryhmasahkoposti.service.dto.EmailQueueDTOConverter;
import fi.vm.sade.ryhmasahkoposti.service.dto.EmailQueueHandleDto;
import fi.vm.sade.ryhmasahkoposti.service.impl.EmailSendQueueServiceImpl;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;
import fi.vm.sade.ryhmasahkoposti.util.UpdateVersionAnswer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * User: ratamaa
 * Date: 15.9.2014
 * Time: 16:11
 */
@RunWith(PowerMockRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@PrepareForTest({ MessageUtil.class })
public class EmailSendQueueServiceTest {
    private static final long TIMEOUT_MILLIS = 60000l;

    private EmailSendQueueServiceImpl emailSendQueueService;
    @Mock
    private SendQueueDAO sendQueueDao;;


    @Before
    public void setup() {
        emailSendQueueService = new EmailSendQueueServiceImpl(sendQueueDao,
                new EmailQueueDTOConverter(), new EmailRecipientDTOConverter());
        emailSendQueueService.setMaxEmailRecipientHandleTimeMillis(TIMEOUT_MILLIS);
    }

    @Test
    public void testNoQueuesToReserve() {
        when(sendQueueDao.findNextAvailableSendQueue()).thenReturn(null);

        assertNull(emailSendQueueService.reserveNextQueueToHandle());
    }

    @Test
    public void testReserveQueue() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.WAITING_FOR_HANDLER);
        when(sendQueueDao.findNextAvailableSendQueue()).thenReturn(queue);
        doAnswer(new UpdateVersionAnswer()).when(sendQueueDao).update(any(SendQueue.class));
        ReportedMessage message = RaportointipalveluTestData.getReportedMessage();
        when(sendQueueDao.getUnhandledRecipeientsInQueue(eq(100l))).thenReturn(new ArrayList<ReportedRecipient>(Arrays.asList(
            RaportointipalveluTestData.getReportedRecipient(message)
        )));

        EmailQueueHandleDto result = emailSendQueueService.reserveNextQueueToHandle();
        assertNotNull(result);
        assertEquals(SendQueueState.PROCESSING, result.getState());
        assertEquals(100l, result.getId());
        assertEquals(2l, result.getVersion());
        assertNotNull(result.getRecipients());
        assertEquals(1, result.getRecipients().size());
    }

    @Test
    public void testContinueQueueHandling() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.PROCESSING);
        queue.setLastHandledAt(new Date());
        when(sendQueueDao.getQueue(eq(100l), eq(1l))).thenReturn(queue);

        EmailQueueHandleDto queueDto = new EmailQueueHandleDto();
        queueDto.setId(100l);
        queueDto.setVersion(1l);
        assertTrue(emailSendQueueService.continueQueueHandling(queueDto));
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
        assertFalse(emailSendQueueService.continueQueueHandling(queueDto));
    }

    @Test
    public void testNotContinueQueueHandlingDueToTooOld() {
        Date now = new Date();
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.PROCESSING);
        queue.setLastHandledAt(new Date(now.getTime()-emailSendQueueService.getMaxEmailRecipientHandleTimeMillis()-1));
        when(sendQueueDao.getQueue(eq(100l), eq(1l))).thenReturn(queue);

        EmailQueueHandleDto queueDto = new EmailQueueHandleDto();
        queueDto.setId(100l);
        queueDto.setVersion(1l);
        assertFalse(emailSendQueueService.continueQueueHandling(queueDto));
    }

    @Test
    public void testNotContinueQueueHandlingDueToFalseState() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.WAITING_FOR_HANDLER);
        when(sendQueueDao.getQueue(eq(100l), eq(1l))).thenReturn(queue);

        EmailQueueHandleDto queueDto = new EmailQueueHandleDto();
        queueDto.setId(100l);
        queueDto.setVersion(1l);
        assertFalse(emailSendQueueService.continueQueueHandling(queueDto));
    }

    @Test
    public void testNotContinueQueueHandlingDueToFalseState2() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.PROCESSING);
        queue.setFinishedAt(new Date());
        when(sendQueueDao.getQueue(eq(100l), eq(1l))).thenReturn(queue);

        EmailQueueHandleDto queueDto = new EmailQueueHandleDto();
        queueDto.setId(100l);
        queueDto.setVersion(1l);
        assertFalse(emailSendQueueService.continueQueueHandling(queueDto));
    }

    @Test
    public void testQueueHandled() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.PROCESSING);
        when(sendQueueDao.read(eq(100l))).thenReturn(queue);
        doAnswer(new UpdateVersionAnswer()).when(sendQueueDao).update(any(SendQueue.class));

        emailSendQueueService.queueHandled(100l);
        assertEquals(SendQueueState.READY, queue.getState());
        assertNotNull(queue.getFinishedAt());
        assertEquals(Long.valueOf(2l), queue.getVersion());
    }

    @Test(expected = IllegalStateException.class)
    public void testQueueHandledForIncorrectQueue() {
        SendQueue queue = RaportointipalveluTestData.sendQueue(100l, SendQueueState.READY);
        when(sendQueueDao.read(eq(100l))).thenReturn(queue);

        emailSendQueueService.queueHandled(100l);
    }

    @Test
    public void testCheckForStoppedProcesses() {
        Date now = new Date();
        SendQueue queue1 = RaportointipalveluTestData.sendQueue(100l, SendQueueState.PROCESSING),
                queue2 = RaportointipalveluTestData.sendQueue(200l, SendQueueState.PROCESSING),
                queue3 = RaportointipalveluTestData.sendQueue(300l, SendQueueState.PROCESSING);
        queue1.setLastHandledAt(new Date(now.getTime()-emailSendQueueService.getMaxEmailRecipientHandleTimeMillis()+1000l));
        queue2.setLastHandledAt(new Date(now.getTime()-emailSendQueueService.getMaxEmailRecipientHandleTimeMillis()-1));
        queue3.setLastHandledAt(null);
        when(sendQueueDao.findActiveQueues()).thenReturn(new ArrayList<SendQueue>(Arrays.asList(
                queue1, queue2, queue3
        )));

        emailSendQueueService.checkForStoppedProcesses();
        assertEquals(SendQueueState.PROCESSING, queue1.getState());
        assertEquals(SendQueueState.WAITING_FOR_HANDLER, queue2.getState());
        assertEquals(SendQueueState.PROCESSING, queue3.getState());
    }

    @Test
    public void testGetNumberOfUnhandledQueues() {
        when(sendQueueDao.getNumberOfUnhandledQueues()).thenReturn(15);
        assertEquals(15, emailSendQueueService.getNumberOfUnhandledQueues());
    }
}
