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

package fi.vm.sade.viestintapalvelu.service;

import java.util.Arrays;
import java.util.Date;

import javax.ws.rs.NotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedRecipientReplacementDTO;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.viestintapalvelu.externalinterface.component.EmailComponent;
import fi.vm.sade.viestintapalvelu.letter.LetterService;
import fi.vm.sade.viestintapalvelu.letter.impl.LetterEmailServiceImpl;
import fi.vm.sade.viestintapalvelu.letter.impl.LetterServiceImpl;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.template.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateService;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import fi.vm.sade.viestintapalvelu.util.CatchSingleParameterAnswer;

import static fi.vm.sade.viestintapalvelu.util.AnswerChain.atFirstReturn;
import static fi.vm.sade.viestintapalvelu.util.CatchSingleParameterAnswer.catchParameters;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * User: ratamaa
 * Date: 1.10.2014
 * Time: 16:32
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("/test-application-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class LetterEmailServiceTest {
    @Mock
    private LetterBatchDAO letterBatchDAO;
    @Mock
    private EmailComponent emailComponent;
    @Mock
    private TemplateService templateService;
    @Mock
    private LetterServiceImpl letterService;
    @InjectMocks
    private LetterEmailServiceImpl letterEmailService;

    @Before
    public void setup() {
        letterEmailService.setObjectMapperProvider(new ObjectMapperProvider());
        doCallRealMethod().when(letterService).setLetterBatchDAO(any(LetterBatchDAO.class));
        letterService.setLetterBatchDAO(letterBatchDAO);
    }

    @Test(expected = IllegalStateException.class)
    public void testSendEmailInvalidStatus() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(1l);
        letterBatch.setBatchStatus(LetterBatch.Status.processing);
        when(letterBatchDAO.read(eq(Long.valueOf(1l)))).thenReturn(letterBatch);
        letterEmailService.sendEmail(1l);
    }

    @Test(expected = IllegalStateException.class)
    public void testSendEmailInvalidEmailHandlingStatus() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(1l);
        letterBatch.setBatchStatus(LetterBatch.Status.ready);
        letterBatch.setEmailHandlingStarted(new Date());
        when(letterBatchDAO.read(eq(Long.valueOf(1l)))).thenReturn(letterBatch);
        letterEmailService.sendEmail(1l);
    }

    @Test(expected = IllegalStateException.class)
    public void testSendEmailNoTemplate() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(1l);
        letterBatch.setBatchStatus(LetterBatch.Status.ready);
        letterBatch.setTemplateId(null);
        when(letterBatchDAO.read(eq(Long.valueOf(1l)))).thenReturn(letterBatch);
        letterEmailService.sendEmail(1l);
    }

    @Test(expected = NotFoundException.class)
    public void testSendEmailTemplateNotFound() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(1l);
        letterBatch.setBatchStatus(LetterBatch.Status.ready);
        when(letterBatchDAO.read(eq(Long.valueOf(1l)))).thenReturn(letterBatch);
        letterEmailService.sendEmail(1l);
    }

    @Test
    public void testSendEmailNoRecipients() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(1l);
        letterBatch.setBatchStatus(LetterBatch.Status.ready);
        when(letterBatchDAO.read(eq(Long.valueOf(1l)))).thenReturn(letterBatch);
        when(templateService.findById(eq(1l))).thenReturn(DocumentProviderTestData.getTemplate());
        doCallRealMethod().when(letterService).updateBatchProcessingStarted(eq(1l), eq(LetterService.LetterBatchProcess.EMAIL));
        doCallRealMethod().when(letterService).updateBatchProcessingFinished(eq(1l), eq(LetterService.LetterBatchProcess.EMAIL));

        letterEmailService.sendEmail(1l);

        verify(letterService).updateBatchProcessingStarted(eq(1l), eq(LetterService.LetterBatchProcess.EMAIL));
        verify(letterService).updateBatchProcessingFinished(eq(1l), eq(LetterService.LetterBatchProcess.EMAIL));
        assertNotNull(letterBatch.getEmailHandlingStarted());
        assertNotNull(letterBatch.getEmailHandlingFinished());
        verifyZeroInteractions(emailComponent);
    }

    @Test
    public void testSendEmail() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(1l);
        LetterReceivers receiver = letterBatch.getLetterReceivers().iterator().next();
        receiver.setEmailAddress("test@example.com");
        LetterReceiverReplacement replacement = new LetterReceiverReplacement();
        replacement.setName("test");
        replacement.setJsonValue("[1,2,3]");
        receiver.getLetterReceiverReplacement().clear();
        receiver.getLetterReceiverReplacement().add(replacement);

        letterBatch.setBatchStatus(LetterBatch.Status.ready);
        when(letterBatchDAO.read(eq(Long.valueOf(1l)))).thenReturn(letterBatch);
        Template template = DocumentProviderTestData.getTemplate();
        template.setId(1l);
        when(templateService.findById(eq(1l))).thenReturn(template);
        CatchSingleParameterAnswer<Boolean,EmailData> catchEmailInvocations = catchParameters(atFirstReturn(true)
                .thenThrow(new IllegalStateException("Only one email expected.")));
        when(emailComponent.sendEmail(any(EmailData.class))).then(catchEmailInvocations);

        letterEmailService.sendEmail(1l);

        assertEquals(1, catchEmailInvocations.getInvocationCount());
        EmailData data = catchEmailInvocations.getArguments().get(0);
        assertEquals("1", data.getEmail().getTemplateId());
        assertEquals("test_template", data.getEmail().getTemplateName());
        assertEquals("FI", data.getEmail().getLanguageCode());

        assertEquals(1, data.getRecipient().size());
        assertEquals(1, data.getReplacements().size());
        EmailRecipient recipient = data.getRecipient().get(0);
        assertEquals("test@example.com", recipient.getEmail());
        assertEquals(1, recipient.getRecipientReplacements().size());
        ReportedRecipientReplacementDTO replacementDto = recipient.getRecipientReplacements().get(0);
        assertEquals(Arrays.asList(1,2,3), replacementDto.getEffectiveValue());
    }
}
