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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import fi.vm.sade.dto.OrganisaatioHenkiloDto;
import fi.vm.sade.dto.PagingAndSortingDTO;
import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.OrganizationDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessagesDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.common.util.MessageUtil;
import fi.vm.sade.ryhmasahkoposti.converter.AttachmentResponseConverter;
import fi.vm.sade.ryhmasahkoposti.converter.EmailMessageDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.EmailRecipientDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedAttachmentConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedMessageConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedMessageDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedMessageReplacementConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedRecipientConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedRecipientReplacementConverter;
import fi.vm.sade.ryhmasahkoposti.dao.SendQueueDAO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.OrganizationComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.service.impl.GroupEmailReportingServiceImpl;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;
import org.apache.commons.fileupload.FileItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(PowerMockRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class })
@PrepareForTest({ MessageUtil.class, ReportedMessageConverter.class, ReportedRecipientConverter.class })
public class GroupEmailReportingServiceTest {
    private GroupEmailReportingService groupEmailReportingService;
    @Mock
    FileItem mockedFileItem;
    @Mock
    private ReportedMessageService mockedReportedMessageService;
    @Mock
    private ReportedRecipientService mockedReportedRecipientService;
    @Mock
    private ReportedAttachmentService mockedReportedAttachmentService;
    @Mock
    private ReportedMessageAttachmentService mockedReportedMessageAttachmentService;
    @Mock
    private ReportedMessageConverter mockedReportedMessageConverter;
    @Mock
    private ReportedRecipientConverter mockedReportedRecipientConverter;
    @Mock
    private ReportedAttachmentConverter mockedReportedAttachmentConverter;
    @Mock
    private AttachmentResponseConverter mockedAttachmentResponseConverter;
    @Mock
    private EmailMessageDTOConverter mockedEmailMessageDTOConverter;
    @Mock
    private EmailRecipientDTOConverter mockedEmailRecipientDTOConverter;
    @Mock
    private ReportedMessageDTOConverter mockedReportedMessageDTOConverter;
    @Mock
    private CurrentUserComponent mockedCurrentUserComponent;
    @Mock
    private OrganizationComponent mockedOrganizationComponent;
    @Mock
    private TemplateService mockedTemplateComponent;
    @Mock
    private ReportedMessageReplacementConverter mokedReportedMessageReplacementConverter;
    @Mock
    private ReportedMessageReplacementService mockReportedMessageReplacementService;
    @Mock
    private ReportedRecipientReplacementConverter mockReportedRecipientReplacementConverter;
    @Mock
    private ReportedRecipientReplacementService mockReportedRecipientReplacementService;
    @Mock
    private SendQueueDAO sendQueueDao;

    @Before
    public void setup() {
        groupEmailReportingService = new GroupEmailReportingServiceImpl(mockedReportedMessageService, mockedReportedRecipientService,
                mockedReportedAttachmentService, mockedReportedMessageAttachmentService, mockedReportedMessageConverter, mockedReportedRecipientConverter,
                mockedReportedAttachmentConverter, mockedAttachmentResponseConverter, mockedEmailMessageDTOConverter, mockedEmailRecipientDTOConverter,
                mockedReportedMessageDTOConverter, mockedCurrentUserComponent, mockedOrganizationComponent, mockedTemplateComponent,
                mokedReportedMessageReplacementConverter, mockReportedMessageReplacementService, mockReportedRecipientReplacementConverter,
                mockReportedRecipientReplacementService, sendQueueDao);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAddSendingGroupEmail() throws IOException {
        when(mockedReportedMessageConverter.convert(any(EmailMessage.class))).thenReturn(new ReportedMessage());

        ReportedMessage savedReportedMessage = RaportointipalveluTestData.getReportedMessage();
        savedReportedMessage.setId(2L);
        savedReportedMessage.setVersion(0L);
        when(mockedReportedMessageService.saveReportedMessage(any(ReportedMessage.class))).thenReturn(savedReportedMessage);

        List<ReportedAttachment> reportedAttachments = new ArrayList<>();
        ReportedAttachment reportedAttachment = RaportointipalveluTestData.getReportedAttachment();
        reportedAttachment.setId(3L);
        reportedAttachments.add(reportedAttachment);
        when(mockedReportedAttachmentService.getReportedAttachments(any(List.class))).thenReturn(reportedAttachments);

        doAnswer((Answer<Object>) invocation -> {
            @SuppressWarnings("unused")
            Object[] args = invocation.getArguments();
            return null;
        }).when(mockedReportedMessageAttachmentService).saveReportedMessageAttachments(any(ReportedMessage.class), any(List.class));

        doAnswer((Answer<Object>) invocation -> {
            @SuppressWarnings("unused")
            Object[] args = invocation.getArguments();
            return null;
        }).when(mockedReportedRecipientService).saveReportedRecipients(any(Set.class));

        when(mockedReportedRecipientConverter.convert(any(ReportedMessage.class), eq(new ArrayList<>()))).thenReturn(
            new HashSet<>());

        EmailData emailData = RaportointipalveluTestData.getEmailData();

        EmailMessage emailMessage = RaportointipalveluTestData.getEmailMessage();

        List<EmailRecipient> emailRecipients = new ArrayList<>();
        EmailRecipient emailRecipient = RaportointipalveluTestData.getEmailRecipient();
        emailRecipient.setEmail("testLahetyksenTulosVastaaRaportoitujatietoja@sposti.fi");
        emailRecipients.add(emailRecipient);

        List<EmailAttachment> emailAttachments = new ArrayList<>();
        EmailAttachment emailAttachment = RaportointipalveluTestData.getEmailAttachment();
        emailAttachments.add(emailAttachment);

        List<AttachmentResponse> attachmentResponses = new ArrayList<>();
        AttachmentResponse attachmentResponse = RaportointipalveluTestData.getAttachmentResponse(1L, mockedFileItem);
        attachmentResponses.add(attachmentResponse);

        emailMessage.setAttachments(emailAttachments);
        emailMessage.setAttachInfo(attachmentResponses);

        emailData.setEmail(emailMessage);
        emailData.setRecipient(emailRecipients);

        Long messageID = groupEmailReportingService.addSendingGroupEmail(emailData);

        assertNotNull(messageID);
    }

    @Test
    public void testSaveAttachment() throws IOException {
        byte[] sisalto = { 'k', 'o', 'e', 'k', 'u', 't', 's', 'u' };

        when(mockedFileItem.getName()).thenReturn("Koekutsu");
        when(mockedFileItem.getContentType()).thenReturn("application/pdf");
        when(mockedFileItem.get()).thenReturn(sisalto);

        when(mockedReportedAttachmentService.saveReportedAttachment(any(ReportedAttachment.class))).thenReturn(new Long(1));

        Long liiteID = groupEmailReportingService.saveAttachment(mockedFileItem);

        assertNotNull(liiteID);
        assertTrue(liiteID > 0);
    }

    @Test
    public void testStartSending() {
        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient();
        when(mockedReportedRecipientService.getReportedRecipient(any(Long.class))).thenReturn(reportedRecipient);

        doAnswer((Answer<Object>) invocation -> {
            @SuppressWarnings("unused")
            Object[] args = invocation.getArguments();
            return null;
        }).when(mockedReportedRecipientService).updateReportedRecipient(any(ReportedRecipient.class));

        EmailRecipientDTO recipient = RaportointipalveluTestData.getEmailRecipientDTO();
        boolean successful = groupEmailReportingService.startSending(recipient);

        assertTrue(successful);
    }

    @Test
    public void testRecipientHandledSuccess() {
        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient();
        when(mockedReportedRecipientService.getReportedRecipient(any(Long.class))).thenReturn(reportedRecipient);

        doAnswer((Answer<Object>) invocation -> {
            @SuppressWarnings("unused")
            Object[] args = invocation.getArguments();
            return null;
        }).when(mockedReportedRecipientService).updateReportedRecipient(any(ReportedRecipient.class));

        EmailRecipientDTO emailRecipientDTO = RaportointipalveluTestData.getEmailRecipientDTO();
        groupEmailReportingService.recipientHandledSuccess(emailRecipientDTO, "Lähetys ok");
    }

    @Test
    public void testGetUnhandledMessageRecipients() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessage.setId(1L);
        reportedMessage.setVersion(0L);

        List<ReportedRecipient> reportedRecipients = new ArrayList<>();
        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient();
        reportedRecipient.setReportedMessage(reportedMessage);
        reportedRecipients.add(reportedRecipient);

        List<EmailRecipientDTO> mockedEmailRecipientDTOs = new ArrayList<>();
        mockedEmailRecipientDTOs.add(RaportointipalveluTestData.getEmailRecipientDTO());

        when(mockedReportedRecipientService.getUnhandledReportedRecipients(1)).thenReturn(reportedRecipients);
        when(mockedEmailRecipientDTOConverter.convert(reportedRecipients)).thenReturn(mockedEmailRecipientDTOs);

        List<EmailRecipientDTO> emailRecipientDTOs = groupEmailReportingService.getUnhandledMessageRecipients(1);

        assertNotNull(emailRecipientDTOs);
        assertEquals(1, emailRecipientDTOs.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetMessage() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessage.setId(1L);
        reportedMessage.setVersion(0L);

        Set<ReportedRecipient> reportedRecipients = new HashSet<>();
        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient();
        reportedRecipient.setReportedMessage(reportedMessage);
        reportedRecipients.add(reportedRecipient);

        reportedMessage.setReportedRecipients(reportedRecipients);

        EmailMessageDTO mockedEmailMessageDTO = RaportointipalveluTestData.getEmailMessageDTO();

        when(mockedReportedMessageService.getReportedMessage(any(Long.class))).thenReturn(reportedMessage);
        when(mockedEmailMessageDTOConverter.convert(any(ReportedMessage.class), any(List.class), any(List.class))).thenReturn(mockedEmailMessageDTO);

        EmailMessageDTO emailMessageDTO = groupEmailReportingService.getMessage(1L);

        assertNotNull(emailMessageDTO);
        assertNotNull(emailMessageDTO.getMessageID());
        assertEquals(emailMessageDTO.getMessageID(), new Long(1));
    }

    @Test
    public void testGetSendingStatus() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessage.setId(1L);

        SendingStatusDTO sendingStatusDTO = RaportointipalveluTestData.getSendingStatusDTO();

        when(mockedReportedMessageService.getReportedMessage(any(Long.class))).thenReturn(reportedMessage);
        when(mockedReportedRecipientService.getSendingStatusOfRecipients(any(Long.class))).thenReturn(sendingStatusDTO);

        SendingStatusDTO sendingStatus = groupEmailReportingService.getSendingStatus(1L);

        assertNotNull(sendingStatus);
        assertNotNull(sendingStatus.getMessageID());
        assertNotNull(sendingStatus.getNumberOfRecipients());
        assertEquals(sendingStatus.getNumberOfRecipients(), new Long(10));
        assertNotNull(sendingStatus.getNumberOfSuccessfulSendings());
        assertEquals(sendingStatus.getNumberOfSuccessfulSendings(), new Long(5));
        assertEquals(sendingStatus.getNumberOfBouncedSendings(), new Long(6));
        assertNotNull(sendingStatus.getNumberOfFailedSendings());
        assertEquals(sendingStatus.getNumberOfFailedSendings(), new Long(2));
        assertNull(sendingStatus.getSendingEnded());
    }

    @Test
    public void testGetSendingStatusWhenSendingHasEnded() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessage.setId(1L);

        SendingStatusDTO sendingStatusDTO = RaportointipalveluTestData.getSendingStatusDTO();
        sendingStatusDTO.setNumberOfSuccessfulSendings(8L);
        sendingStatusDTO.setSendingEnded(new Date());

        when(mockedReportedMessageService.getReportedMessage(any(Long.class))).thenReturn(reportedMessage);
        when(mockedReportedRecipientService.getSendingStatusOfRecipients(any(Long.class))).thenReturn(sendingStatusDTO);
        when(mockedReportedRecipientService.getLatestReportedRecipientsSendingEndedDate(any(Long.class))).thenReturn(new Date());

        SendingStatusDTO sendingStatus = groupEmailReportingService.getSendingStatus(1L);

        assertNotNull(sendingStatus);
        assertNotNull(sendingStatus.getMessageID());
        assertNotNull(sendingStatus.getNumberOfRecipients());
        assertEquals(sendingStatus.getNumberOfRecipients(), new Long(10));
        assertNotNull(sendingStatus.getNumberOfSuccessfulSendings());
        assertEquals(sendingStatus.getNumberOfSuccessfulSendings(), new Long(8));
        assertNotNull(sendingStatus.getNumberOfFailedSendings());
        assertEquals(sendingStatus.getNumberOfFailedSendings(), new Long(2));
        assertNotNull(sendingStatus.getSendingEnded());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetReportedMessagesByOrganizationOid() {
        PowerMockito.mockStatic(MessageUtil.class);
        PowerMockito.when(MessageUtil.getMessage("ryhmasahkoposti.lahetys_epaonnistui", 2L)).thenReturn("Lahetyksiä epäonnistui");

        List<ReportedMessage> mockedReportedMessages = new ArrayList<>();
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessage.setId(1L);
        reportedMessage.setVersion(0L);

        Set<ReportedRecipient> reportedRecipients = new HashSet<>();
        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient();
        reportedRecipient.setReportedMessage(reportedMessage);
        reportedRecipients.add(reportedRecipient);

        reportedMessage.setReportedRecipients(reportedRecipients);
        mockedReportedMessages.add(reportedMessage);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        pagingAndSorting.setSortedBy("sendingStarted");

        when(mockedReportedMessageService.getReportedMessages(any(String.class), any(PagingAndSortingDTO.class))).thenReturn(mockedReportedMessages);

        SendingStatusDTO sendingStatus = RaportointipalveluTestData.getSendingStatusDTO();
        when(mockedReportedRecipientService.getSendingStatusOfRecipients(any(Long.class))).thenReturn(sendingStatus);

        List<ReportedMessageDTO> mockedReportedMessageDTOs = new ArrayList<>();
        mockedReportedMessageDTOs.add(RaportointipalveluTestData.getReportedMessageDTO());
        when(mockedReportedMessageDTOConverter.convert(any(List.class), any(Map.class))).thenReturn(mockedReportedMessageDTOs);

        ReportedMessagesDTO reportedMessagesDTO = groupEmailReportingService.getReportedMessagesByOrganizationOid("1.2.246.562.10.00000000001",
                pagingAndSorting);

        assertNotNull(reportedMessagesDTO);
        assertEquals(1, reportedMessagesDTO.getReportedMessages().size());
        assertNotNull(reportedMessagesDTO.getReportedMessages().get(0).getStatusReport());
        assertTrue(reportedMessagesDTO.getReportedMessages().get(0).getStatusReport().equalsIgnoreCase("Lahetyksiä epäonnistui"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetReportedMessagesBySenderOid() {
        List<ReportedMessage> mockedReportedMessages = new ArrayList<>();
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessage.setId(1L);
        reportedMessage.setVersion(0L);

        Set<ReportedRecipient> reportedRecipients = new HashSet<>();
        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient();
        reportedRecipient.setReportedMessage(reportedMessage);
        reportedRecipients.add(reportedRecipient);

        reportedMessage.setReportedRecipients(reportedRecipients);
        mockedReportedMessages.add(reportedMessage);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();

        when(mockedReportedMessageService.getUserMessages("1.2.246.562.24.42645159413", pagingAndSorting)).thenReturn(mockedReportedMessages);

        List<ReportedMessageDTO> mockedReportedMessageDTOs = new ArrayList<>();
        mockedReportedMessageDTOs.add(RaportointipalveluTestData.getReportedMessageDTO());
        when(mockedReportedMessageDTOConverter.convert(mockedReportedMessages)).thenReturn(mockedReportedMessageDTOs);

        ReportedMessagesDTO reportedMessagesDTO = groupEmailReportingService.getReportedMessagesBySenderOid("1.2.246.562.24.42645159413", null,
                pagingAndSorting);

        assertNotNull(reportedMessagesDTO);
        assertEquals(1, reportedMessagesDTO.getReportedMessages().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetReportedMessagesBySearchArgument() {
        ReportedMessageQueryDTO query = RaportointipalveluTestData.getReportedMessageQueryDTO();

        PowerMockito.mockStatic(MessageUtil.class);
        when(MessageUtil.getMessage(any(String.class))).thenReturn("lahetys kesken");

        List<ReportedMessage> mockedReportedMessages = new ArrayList<>();
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessage.setId(1L);
        reportedMessage.setVersion(0L);

        Set<ReportedRecipient> reportedRecipients = new HashSet<>();
        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient();
        reportedRecipient.setReportedMessage(reportedMessage);
        reportedRecipients.add(reportedRecipient);

        reportedMessage.setReportedRecipients(reportedRecipients);
        mockedReportedMessages.add(reportedMessage);

        when(mockedReportedMessageService.getReportedMessages(any(ReportedMessageQueryDTO.class), any(PagingAndSortingDTO.class))).thenReturn(
                mockedReportedMessages);

        SendingStatusDTO sendingStatus = RaportointipalveluTestData.getSendingStatusDTO();
        when(mockedReportedRecipientService.getSendingStatusOfRecipients(any(Long.class))).thenReturn(sendingStatus);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        pagingAndSorting.setSortedBy("sendingStarted");

        List<ReportedMessageDTO> mockedReportedMessageDTOs = new ArrayList<>();
        mockedReportedMessageDTOs.add(RaportointipalveluTestData.getReportedMessageDTO());
        when(mockedReportedMessageDTOConverter.convert(any(List.class), any(Map.class))).thenReturn(mockedReportedMessageDTOs);

        ReportedMessagesDTO reportedMessagesDTO = groupEmailReportingService.getReportedMessages(query, pagingAndSorting);

        assertNotNull(reportedMessagesDTO);
        assertEquals(1, reportedMessagesDTO.getReportedMessages().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetReportedMessageWithReports() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessage.setId(1L);
        reportedMessage.setVersion(0L);

        Set<ReportedRecipient> reportedRecipients = new HashSet<>();
        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient();
        reportedRecipient.setReportedMessage(reportedMessage);
        reportedRecipients.add(reportedRecipient);

        reportedMessage.setReportedRecipients(reportedRecipients);

        when(mockedReportedMessageService.getReportedMessage(any(Long.class))).thenReturn(reportedMessage);

        SendingStatusDTO sendingStatusDTO = RaportointipalveluTestData.getSendingStatusDTO();
        when(mockedReportedRecipientService.getSendingStatusOfRecipients(any(Long.class))).thenReturn(sendingStatusDTO);

        PowerMockito.mockStatic(MessageUtil.class);
        PowerMockito.when(MessageUtil.getMessage("ryhmasahkoposti.lahetys_raportti", 5L, 2L)).thenReturn(
                "2 lahetystä epäonnistui");
        PowerMockito.when(MessageUtil.getMessage("ryhmasahkoposti.lahetys_epaonnistui", 2L)).thenReturn("Lahetyksiä epäonnistui");

        when(mockedReportedAttachmentService.getReportedAttachments(any(Set.class))).thenReturn(new ArrayList<ReportedAttachment>());

        ReportedMessageDTO mockedReportedMessageDTO = RaportointipalveluTestData.getReportedMessageDTO();
        when(mockedReportedMessageDTOConverter.convert(any(ReportedMessage.class), any(List.class), any(SendingStatusDTO.class))).thenReturn(
                mockedReportedMessageDTO);

        ReportedMessageDTO reportedMessageDTO = groupEmailReportingService.getReportedMessage(1L);

        assertNotNull(reportedMessageDTO);
        assertEquals(reportedMessageDTO.getMessageID(), new Long(1));
        assertNotNull(reportedMessageDTO.getStatusReport());
        assertNotNull(reportedMessageDTO.getSendingReport());
    }

    @Test
    public void testGetUserOrganization() {
        List<OrganisaatioHenkiloDto> henkilonOrganisaatiot = RaportointipalveluTestData.getHenkilonOrganisaatiot();
        when(mockedCurrentUserComponent.getCurrentUserOrganizations()).thenReturn(henkilonOrganisaatiot);
        when(mockedOrganizationComponent.getOrganization(any(String.class))).thenReturn(RaportointipalveluTestData.getOrganisaatioRDTO());
        when(mockedOrganizationComponent.getNameOfOrganisation(any(OrganisaatioRDTO.class))).thenReturn("OPH");

        List<OrganizationDTO> organizationDTOs = groupEmailReportingService.getUserOrganizations();

        assertNotNull(organizationDTOs);
        assertEquals(1, organizationDTOs.size());
        assertEquals("1.2.246.562.10.00000000001", organizationDTOs.get(0).getOid());
        assertTrue(organizationDTOs.get(0).getName().equalsIgnoreCase("OPH"));
    }

    @Test
    public void exceptionInTemplateLoadingCausesCreationOfSendingGroupEmailToCrash() throws IOException {
        EmailMessage emailMessage = new EmailMessage("testProcess",
            "testSender",
            "noreply@example.com",
            "Hello from test",
            "testtemplate",
            "FI",
            Collections.emptyList());
        RuntimeException expectedException = new RuntimeException("Boom-kah!");
        when(mockedTemplateComponent.getTemplate("testtemplate", "FI", "email", null)).thenThrow(expectedException);

        try {
            groupEmailReportingService.createSendingGroupEmail(new EmailData(Collections.emptyList(), emailMessage));
            fail("Should have exited with exception, because template loading failed.");
        } catch (RuntimeException e) {
            assertEquals(expectedException, e);
        }

        Mockito.verifyZeroInteractions(mockedReportedMessageService);
        Mockito.verify(mockedTemplateComponent).getTemplate("testtemplate", "FI", "email", null);
        Mockito.verifyNoMoreInteractions(mockedTemplateComponent);
    }
}
