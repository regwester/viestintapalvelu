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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedRecipientQueryDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.service.impl.ReportedMessageServiceImpl;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class })
@Transactional(readOnly = true)
public class ReportedMessageServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    ReportedMessageDAO mockedReportedMessageDAO;

    private ReportedMessageServiceImpl reportedMessageService;

    @Before
    public void setup() {
        reportedMessageService = new ReportedMessageServiceImpl(mockedReportedMessageDAO);
        reportedMessageService.setRootOrganizationOID("1.2.246.562.10.00000000001");
    }

    @Test
    public void testReportedMessageSaveIsSuccessful() throws IOException {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();

        ReportedMessage savedReportedMessage = RaportointipalveluTestData.getReportedMessage();
        savedReportedMessage.setId(new Long(1));
        savedReportedMessage.setVersion(new Long(0));

        when(mockedReportedMessageDAO.insert(reportedMessage)).thenReturn(savedReportedMessage);
        savedReportedMessage = reportedMessageService.saveReportedMessage(reportedMessage);

        assertNotNull(savedReportedMessage.getId());
        assertNotNull(savedReportedMessage.getVersion());
    }

    @Test
    public void testGetReportedMessagesByOrganizationOid() throws IOException {
        List<ReportedMessage> reportedMessages = new ArrayList<ReportedMessage>();
        reportedMessages.add(RaportointipalveluTestData.getReportedMessage());

        when(mockedReportedMessageDAO.findByOrganizationOids(eq(null), any(PagingAndSortingDTO.class))).thenReturn(reportedMessages);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        pagingAndSorting.setSortedBy("sendingStarted");

        reportedMessages = reportedMessageService.getReportedMessages("1.2.246.562.10.00000000001", pagingAndSorting);

        assertNotNull(reportedMessages);
        assertNotEquals(0, reportedMessages.size());
    }

    @Test
    public void testGetReportedMessagesBySenderOid() throws IOException {
        List<ReportedMessage> reportedMessages = new ArrayList<ReportedMessage>();
        reportedMessages.add(RaportointipalveluTestData.getReportedMessage());

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();

        when(mockedReportedMessageDAO.findBySenderOid("1.2.246.562.24.42645159413", pagingAndSorting)).thenReturn(reportedMessages);

        List<ReportedMessage> result = reportedMessageService.getUserMessages("1.2.246.562.24.42645159413", pagingAndSorting);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindBySearchCriteriaIsSuccessful() throws IOException {
        List<ReportedMessage> mockedReportedMessages = new ArrayList<ReportedMessage>();
        mockedReportedMessages.add(RaportointipalveluTestData.getReportedMessage());

        ReportedMessageQueryDTO reportedMessageQuery = new ReportedMessageQueryDTO();
        ReportedRecipientQueryDTO reportedRecipientQuery = new ReportedRecipientQueryDTO();
        reportedRecipientQuery.setRecipientEmail("vastaan.ottaja@sposti.fi");
        reportedMessageQuery.setReportedRecipientQueryDTO(reportedRecipientQuery);

        PagingAndSortingDTO pagingAndSorting = RaportointipalveluTestData.getPagingAndSortingDTO();
        pagingAndSorting.setSortedBy("sendingStarted");

        when(mockedReportedMessageDAO.findBySearchCriteria(reportedMessageQuery, pagingAndSorting)).thenReturn(mockedReportedMessages);

        List<ReportedMessage> reportedMessages = reportedMessageService.getReportedMessages(reportedMessageQuery, pagingAndSorting);

        assertNotNull(reportedMessages);
        assertNotEquals(0, reportedMessages.size());
    }

    @Test
    public void testGetReportedMessageByPrimaryKey() throws IOException {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessage.setId(new Long(1));

        when(mockedReportedMessageDAO.read(any(Long.class))).thenReturn(reportedMessage);

        reportedMessage = reportedMessageService.getReportedMessage(new Long(1));

        assertNotNull(reportedMessage);
    }

    @Test
    public void testGetReportedMessageNotFoundByPrimaryKey() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        reportedMessage.setId(new Long(1));

        reportedMessage = reportedMessageService.getReportedMessage(new Long(10));

        assertNull(reportedMessage);
    }

    @Test
    public void testUpdateReportedMessage() throws IOException {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                @SuppressWarnings("unused")
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockedReportedMessageDAO).update(reportedMessage);

        reportedMessage.setSendingEnded(new Date());
        reportedMessageService.updateReportedMessage(reportedMessage);
    }
}
