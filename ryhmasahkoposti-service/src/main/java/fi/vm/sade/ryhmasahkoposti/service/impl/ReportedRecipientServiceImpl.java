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
package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import fi.vm.sade.ryhmasahkoposti.api.constants.GroupEmailConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedRecipientDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.service.ReportedRecipientService;

@Service
public class ReportedRecipientServiceImpl implements ReportedRecipientService {
    private ReportedRecipientDAO reportedRecipientDAO;

    @Autowired
    public ReportedRecipientServiceImpl(ReportedRecipientDAO reportedRecipientDAO) {
        this.reportedRecipientDAO = reportedRecipientDAO;
    }

    @Override
    public Date getLatestReportedRecipientsSendingEndedDate(Long messageID) {
        return reportedRecipientDAO.findMaxValueOfSendingEndedByMessageID(messageID);
    }

    @Override
    public Long getNumberOfSendingFailed(Long messageID) {
        return reportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingStatus(messageID, GroupEmailConstants.SENDING_FAILED);
    }

    @Override
    public ReportedRecipient getReportedRecipient(Long id) {
        return reportedRecipientDAO.findByRecipientID(id);
    }

    @Override
    public List<ReportedRecipient> getReportedRecipients(Long messageID, PagingAndSortingDTO pagingAndSorting) {
        return reportedRecipientDAO.findByMessageId(messageID, pagingAndSorting);
    }

    @Override
    public List<ReportedRecipient> getReportedRecipientsByStatusSendingUnsuccessful(Long messageID, PagingAndSortingDTO pagingAndSorting) {
        return reportedRecipientDAO.findByMessageIdAndSendingUnsuccessful(messageID, pagingAndSorting);
    }

    @Override
    public List<ReportedRecipient> getReportedRecipientsByStatusSendingBounced(Long messageID, PagingAndSortingDTO pagingAndSorting) {
        return reportedRecipientDAO.findByMessageIdAndSendingBounced(messageID, pagingAndSorting);
    }

    @Override
    public List<ReportedRecipient> getReportedRecipients() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SendingStatusDTO getSendingStatusOfRecipients(Long messageID) {
        SendingStatusDTO sendingStatus = new SendingStatusDTO();

        long nbrOfSuccessfulAndFailedAndBounced = 0;

        Long nbrOfRecipients = reportedRecipientDAO.findNumberOfRecipientsByMessageID(messageID);
        sendingStatus.setNumberOfRecipients(nbrOfRecipients);

        Long nbrOfSuccessful = reportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingStatus(messageID, "1");
        if (nbrOfSuccessful != null) {
            nbrOfSuccessfulAndFailedAndBounced += nbrOfSuccessful;
        }
        sendingStatus.setNumberOfSuccessfulSendings(nbrOfSuccessful);

        Long nbrOfFailed = reportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingStatus(messageID, "0");
        if (nbrOfFailed != null) {
            nbrOfSuccessfulAndFailedAndBounced += nbrOfFailed;
        }
        sendingStatus.setNumberOfFailedSendings(nbrOfFailed);

        Long nbrOfBounced = reportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingStatus(messageID, "2");
        if (nbrOfBounced != null) {
            nbrOfSuccessfulAndFailedAndBounced += nbrOfBounced;
        }
        sendingStatus.setNumberOfBouncedSendings(nbrOfBounced);

        if (nbrOfSuccessfulAndFailedAndBounced == nbrOfRecipients) {
            Date latestSendingEnded = reportedRecipientDAO.findMaxValueOfSendingEndedByMessageID(messageID);
            sendingStatus.setSendingEnded(latestSendingEnded);
        }

        return sendingStatus;
    }

    @Override
    public List<ReportedRecipient> getUnhandledReportedRecipients(int listSize) {
        List<ReportedRecipient> reportedRecipients = reportedRecipientDAO.findUnhandled();

        if (reportedRecipients == null || reportedRecipients.isEmpty()) {
            return new ArrayList<>();
        }

        if (listSize > reportedRecipients.size()) {
            return reportedRecipients;
        }

        return reportedRecipients.subList(0, listSize);
    }

    @Override
    public void saveReportedRecipients(Set<ReportedRecipient> reportedRecipients) {
        for (ReportedRecipient reportedRecipient : reportedRecipients) {
            reportedRecipientDAO.insert(reportedRecipient);
        }
    }

    @Override
    public void saveReportedRecipient(ReportedRecipient reportedRecipient) {
        reportedRecipientDAO.insert(reportedRecipient);
    }

    @Override
    public void updateReportedRecipient(ReportedRecipient reportedRecipient) {
        reportedRecipientDAO.update(reportedRecipient);
    }

    @Override
    public List<ReportedRecipient> findByLetterHash(String letterHash) {
        return reportedRecipientDAO.findByLetterHash(letterHash);
    }
}
