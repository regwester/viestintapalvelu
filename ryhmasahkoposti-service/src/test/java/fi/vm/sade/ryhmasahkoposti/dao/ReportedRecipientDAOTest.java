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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class })
@Transactional(readOnly = true)
public class ReportedRecipientDAOTest {
    @Autowired
    private ReportedRecipientDAO reportedRecipientDAO;
    @Autowired
    private ReportedMessageDAO reportedMessageDAO;

    @Test
    public void testRecipientFoundByMessageIDAndEmail() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
        reportedRecipient.setSendingEnded(new Date());
        ReportedRecipient savedRecipient = reportedRecipientDAO.insert(reportedRecipient);

        ReportedRecipient searchedReportedRecipient = reportedRecipientDAO.findByRecipientID(savedRecipient.getId());

        assertNotNull(searchedReportedRecipient);
    }

    @Test
    public void testUnhandledMessagesSearchIsSuccessful() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
        reportedRecipient.setSendingStarted(null);
        reportedRecipient.setSendingEnded(null);
        reportedRecipientDAO.insert(reportedRecipient);

        List<ReportedRecipient> recipients = reportedRecipientDAO.findUnhandled();

        assertNotNull(recipients);
        assertTrue(recipients.size() > 0);
    }

    @Test
    public void testFindMaxValueOfSendingEndedByMessageID() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
        reportedRecipient.setRecipientEmail("testFindMaxValueOfSendingEndedByMessageID1@sposti.fi");
        reportedRecipientDAO.insert(reportedRecipient);

        ReportedRecipient reportedRecipient2 = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
        long time = System.currentTimeMillis() + 5000;
        reportedRecipient2.setSendingEnded(new Date(time));
        reportedRecipient2.setRecipientEmail("testFindMaxValueOfSendingEndedByMessageID2@sposti.fi");
        reportedRecipientDAO.insert(reportedRecipient2);

        Date maxDateValue = reportedRecipientDAO.findMaxValueOfSendingEndedByMessageID(savedReportedMessage.getId());

        assertNotNull(maxDateValue);
        assertTrue(maxDateValue.after(reportedRecipient.getSendingEnded()));
        assertTrue(maxDateValue.compareTo(new Date(time)) == 0);
    }

    @Test
    public void testFindMaxValueOfSendingEndedByMessageIDisNull() {
        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();
        ReportedMessage savedReportedMessage = reportedMessageDAO.insert(reportedMessage);

        ReportedRecipient reportedRecipient = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
        reportedRecipient.setSendingEnded(null);
        reportedRecipient.setRecipientEmail("testFindMaxValueOfSendingEndedByMessageID1@sposti.fi");
        reportedRecipientDAO.insert(reportedRecipient);

        ReportedRecipient reportedRecipient2 = RaportointipalveluTestData.getReportedRecipient(savedReportedMessage);
        reportedRecipient2.setSendingEnded(null);
        reportedRecipient2.setRecipientEmail("testFindMaxValueOfSendingEndedByMessageID2@sposti.fi");
        reportedRecipientDAO.insert(reportedRecipient2);

        Date maxDateValue = reportedRecipientDAO.findMaxValueOfSendingEndedByMessageID(savedReportedMessage.getId());

        assertNull(maxDateValue);
    }

}
