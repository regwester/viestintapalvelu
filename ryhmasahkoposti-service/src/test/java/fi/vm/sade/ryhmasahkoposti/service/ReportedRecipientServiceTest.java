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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedRecipientDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.service.impl.ReportedRecipientServiceImpl;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class })
@Transactional(readOnly = true)
public class ReportedRecipientServiceTest {
    @Mock
    private ReportedRecipientDAO mockedReportedRecipientDAO;
    private ReportedRecipientService reportedRecipientService;

    @Before
    public void setup() {
        reportedRecipientService = new ReportedRecipientServiceImpl(mockedReportedRecipientDAO);
    }

    @Test
    public void testGetSendingStatusOfNumberOfRecipients() {
        when(mockedReportedRecipientDAO.findNumberOfRecipientsByMessageID(any(Long.class))).thenReturn(new Long(10));
        when(mockedReportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingSuccessful(any(Long.class), eq(true))).thenReturn(new Long(8));
        when(mockedReportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingSuccessful(any(Long.class), eq(false))).thenReturn(new Long(2));
        when(mockedReportedRecipientDAO.findMaxValueOfSendingEndedByMessageID(any(Long.class))).thenReturn(new Date());

        SendingStatusDTO sendingStatus = reportedRecipientService.getSendingStatusOfRecipients(new Long(1));

        assertNotNull(sendingStatus);
        assertTrue(sendingStatus.getNumberOfRecipients().longValue() == 10);
        assertTrue(sendingStatus.getNumberOfSuccessfulSendings().longValue() == 8);
        assertTrue(sendingStatus.getNumberOfFailedSendings().longValue() == 2);
        assertNotNull(sendingStatus.getSendingEnded());
    }

    @Test
    public void testGetUnhandledReportedRecipientsFound() {
        List<ReportedRecipient> mockedReportedRecipients = new ArrayList<ReportedRecipient>();
        mockedReportedRecipients.add(RaportointipalveluTestData.getReportedRecipient());
        mockedReportedRecipients.add(RaportointipalveluTestData.getReportedRecipient());
        mockedReportedRecipients.add(RaportointipalveluTestData.getReportedRecipient());

        when(mockedReportedRecipientDAO.findUnhandled()).thenReturn(mockedReportedRecipients);

        List<ReportedRecipient> reportedRecipients = reportedRecipientService.getUnhandledReportedRecipients(3);

        assertNotNull(reportedRecipients);
        assertTrue(reportedRecipients.size() == 3);
    }

    @Test
    public void testGetUnhandledReportedRecipientsFoundMoreThanListSize() {
        List<ReportedRecipient> mockedReportedRecipients = new ArrayList<ReportedRecipient>();
        mockedReportedRecipients.add(RaportointipalveluTestData.getReportedRecipient());
        mockedReportedRecipients.add(RaportointipalveluTestData.getReportedRecipient());
        mockedReportedRecipients.add(RaportointipalveluTestData.getReportedRecipient());

        when(mockedReportedRecipientDAO.findUnhandled()).thenReturn(mockedReportedRecipients);

        List<ReportedRecipient> reportedRecipients = reportedRecipientService.getUnhandledReportedRecipients(2);

        assertNotNull(reportedRecipients);
        assertTrue(reportedRecipients.size() == 2);
    }

    @Test
    public void testGetUnhandledReportedRecipientsNotFound() {
        List<ReportedRecipient> mockedReportedRecipients = new ArrayList<ReportedRecipient>();
        when(mockedReportedRecipientDAO.findUnhandled()).thenReturn(mockedReportedRecipients);

        List<ReportedRecipient> reportedRecipients = reportedRecipientService.getUnhandledReportedRecipients(2);

        assertNotNull(reportedRecipients);
        assertTrue(reportedRecipients.size() == 0);
    }
}
