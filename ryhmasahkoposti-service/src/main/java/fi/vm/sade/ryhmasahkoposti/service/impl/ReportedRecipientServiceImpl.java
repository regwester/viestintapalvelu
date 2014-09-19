package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
	return reportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingSuccessful(messageID, false);
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
    public List<ReportedRecipient> getReportedRecipientsByStatusSendingUnsuccesful(Long messageID, 
	    PagingAndSortingDTO pagingAndSorting) {
	return reportedRecipientDAO.findByMessageIdAndSendingUnsuccessful(messageID, pagingAndSorting);
    }

    @Override
    public List<ReportedRecipient> getReportedRecipients() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public SendingStatusDTO getSendingStatusOfRecipients(Long messageID) {
	SendingStatusDTO sendingStatus = new SendingStatusDTO();

	long nbrOfSuccesfulAndFailed = 0;

	Long nbrOfRecipients = reportedRecipientDAO.findNumberOfRecipientsByMessageID(messageID);
	sendingStatus.setNumberOfReciepients(nbrOfRecipients);

	Long nbrOfSuccesful = reportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingSuccessful(messageID, true);
	if (nbrOfSuccesful != null) {
	    nbrOfSuccesfulAndFailed += nbrOfSuccesful.longValue();
	}
	sendingStatus.setNumberOfSuccesfulSendings(nbrOfSuccesful);

	Long nbrOfFailed = reportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingSuccessful(messageID, false);
	if (nbrOfFailed != null) {
	    nbrOfSuccesfulAndFailed += nbrOfFailed.longValue();
	} 	
	sendingStatus.setNumberOfFailedSendings(nbrOfFailed);

	if (nbrOfSuccesfulAndFailed == nbrOfRecipients.longValue()) {
	    Date latestSendingEnded = reportedRecipientDAO.findMaxValueOfSendingEndedByMessageID(messageID);
	    sendingStatus.setSendingEnded(latestSendingEnded);
	}

	return sendingStatus;
    }

    @Override
    public List<ReportedRecipient> getUnhandledReportedRecipients(int listSize) {		
	List<ReportedRecipient> reportedRecipients = reportedRecipientDAO.findUnhandled();

	if (reportedRecipients == null || reportedRecipients.isEmpty()) {
	    return new ArrayList<ReportedRecipient>();
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
}
