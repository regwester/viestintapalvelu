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

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentContainer;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.common.util.MessageUtil;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import fi.vm.sade.ryhmasahkoposti.model.SendQueueState;
import fi.vm.sade.ryhmasahkoposti.service.dto.EmailQueueHandleDto;
import fi.vm.sade.ryhmasahkoposti.service.impl.EmailAVChecker;
import fi.vm.sade.ryhmasahkoposti.service.impl.EmailSender;
import fi.vm.sade.ryhmasahkoposti.service.impl.EmailServiceImpl;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;
import fi.vm.sade.ryhmasahkoposti.util.AnswerChain;
import fi.vm.sade.ryhmasahkoposti.util.CallCounterVoidAnswer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import static fi.vm.sade.ryhmasahkoposti.util.AnswerChain.atFirstReturn;
import static fi.vm.sade.ryhmasahkoposti.util.AnswerChain.atFirstThrow;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doThrow;

/**
 * User: ratamaa
 * Date: 16.9.2014
 * Time: 9:49
 */
@RunWith(PowerMockRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@PrepareForTest({ MessageUtil.class })
public class EmailServiceTest {
    @InjectMocks
    private EmailServiceImpl emailService;
    @Mock
    private EmailSendQueueService emailQueueService;
    @Mock
    private GroupEmailReportingService rrService;
    @Mock
    private ReportedAttachmentService liiteService;
    @Mock
    private EmailSender emailSender;
    @Mock
    private ReportedMessageDAO emailDao;
    @Mock
    private EmailAVChecker emailAVChecker;
    @Mock
    private ThreadPoolTaskExecutor emailExecutor;

    @Test
    public void testZeroNumberOfUnhandledQueues() {
        when(emailQueueService.getNumberOfUnhandledQueues()).thenReturn(0);
        CallCounterVoidAnswer stoppedProcessesCall = new CallCounterVoidAnswer();
        doAnswer(stoppedProcessesCall).when(emailQueueService).checkForStoppedProcesses();

        // Expect no executables at this time:
        doThrow(new IllegalStateException()).when(emailExecutor).submit(any(Runnable.class));

        emailService.checkEmailQueues();
        assertEquals(1, stoppedProcessesCall.getCallCount());
    }

    @Test
    public void testQueues() {
        when(emailQueueService.getNumberOfUnhandledQueues()).thenReturn(1);
        CallCounterVoidAnswer counter = new CallCounterVoidAnswer();
        doAnswer(counter).when(emailExecutor).submit(any(Runnable.class));

        emailService.checkEmailQueues();
        assertEquals(1, counter.getCallCount());
    }

    @Test
    public void testQueuesMaxValue() {
        emailService.setMaxTasksToStartAtOnce(42);
        when(emailQueueService.getNumberOfUnhandledQueues()).thenReturn(58);
        CallCounterVoidAnswer counter = new CallCounterVoidAnswer();
        doAnswer(counter).when(emailExecutor).submit(any(Runnable.class));

        emailService.checkEmailQueues();
        assertEquals(42, counter.getCallCount());
    }

    @Test
    public void testHandleEmailQueue() throws Exception {
        emailService.setMaxTasksToStartAtOnce(42);
        emailService.setVirusCheckRequired(false);
        EmailMessageDTO message = RaportointipalveluTestData.getEmailMessageDTO();
        EmailQueueHandleDto queue = createQueue(message, 3);

        doAnswer(atFirstThrow(new OptimisticLockException()).times(40).thenReturn(queue))
                .when(emailQueueService).reserveNextQueueToHandle();

        AnswerChain<Boolean> answers = atFirstReturn(true).thenReturn(false)
                .thenThrow(new IllegalStateException("Should not be called after returning false!"));
        doAnswer(answers).when(emailQueueService).continueQueueHandling(any(EmailQueueHandleDto.class));

        when(rrService.startSending(any(EmailRecipientDTO.class))).thenReturn(true);
        when(rrService.getMessage(eq(1l))).thenReturn(message);
        CallCounterVoidAnswer handleMail = new CallCounterVoidAnswer();
        doAnswer(handleMail).when(emailSender).handleMail(any(EmailMessage.class), any(String.class),
                any(Optional.class));

        AnswerChain<Boolean> successHandler = atFirstReturn(true);
        when(rrService.recipientHandledSuccess(eq(queue.getRecipients().get(0)),
                any(String.class))).then(successHandler);
        doThrow(new IllegalStateException("Queue not handled fully. Should not be called!"))
                .when(emailQueueService).queueHandled(any(long.class));

        emailService.handleEmailQueue();
        assertEquals(2, answers.getTotalCallCount());
        assertEquals(1, handleMail.getCallCount());
        assertEquals(1, successHandler.getTotalCallCount());
    }

    protected EmailQueueHandleDto createQueue(EmailMessageDTO message, int recipientsCount) {
        EmailQueueHandleDto queue = new EmailQueueHandleDto();
        queue.setId(42l);
        queue.setVersion(2l);
        queue.setState(SendQueueState.PROCESSING);
        List<EmailRecipientDTO> recipients = new ArrayList<EmailRecipientDTO>();
        for (int i = 0; i < recipientsCount; ++i) {
            recipients.add(RaportointipalveluTestData.getEmailRecipientDTO(message));
        }
        queue.setRecipients(recipients);
        return queue;
    }

    @Test
    public void testExceptionInHandleMessageCauses() throws Exception {
        emailService.setMaxTasksToStartAtOnce(42);
        emailService.setVirusCheckRequired(false);
        EmailMessageDTO message = RaportointipalveluTestData.getEmailMessageDTO();
        EmailQueueHandleDto queue = createQueue(message, 3);

        when(emailQueueService.reserveNextQueueToHandle()).thenReturn(queue);
        when(emailQueueService.continueQueueHandling(any(EmailQueueHandleDto.class))).thenReturn(true);
        when(rrService.getMessage(eq(1l))).thenReturn(message);
        doThrow(new IllegalStateException("Email sending failed!"))
                .when(emailSender).handleMail(any(EmailMessage.class), any(String.class),
                any(Optional.class));
        when(rrService.startSending(any(EmailRecipientDTO.class))).thenReturn(true);
        when(rrService.recipientHandledSuccess(any(EmailRecipientDTO.class), any(String.class)))
                .thenThrow(new IllegalStateException("Should not be successful!"));
        AnswerChain<Boolean> failureHandler = atFirstReturn(true);
        when(rrService.recipientHandledFailure(any(EmailRecipientDTO.class), any(String.class)))
                .then(failureHandler);
        CallCounterVoidAnswer queueHandled = new CallCounterVoidAnswer();
        doAnswer(queueHandled).when(emailQueueService).queueHandled(any(long.class));

        emailService.handleEmailQueue();
        assertEquals(3, failureHandler.getTotalCallCount());
        assertEquals(1, queueHandled.getCallCount());
    }
}
