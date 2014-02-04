package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.dao.ReportedRecipientDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.service.ReportedRecipientService;

@Service
public class ReportedRecipientServiceServiceImpl implements ReportedRecipientService {
	private ReportedRecipientDAO reportedRecipientDAO;
	
	@Autowired
	public ReportedRecipientServiceServiceImpl(ReportedRecipientDAO reportedRecipientDAO) {
		this.reportedRecipientDAO = reportedRecipientDAO;
	}
	
	@Override
	public List<ReportedRecipient> getReportedRecipients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReportedRecipient> getUnhandledReportedRecipients(int listSize
		) {		
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
	public ReportedRecipient getReportedRecipient(Long id) {
		return reportedRecipientDAO.findByRecipientID(id);
	}

	@Override
	public ReportedRecipient getReportedRecipient(Long messageID, String recipientEmail) {
		return reportedRecipientDAO.findByMessageIdAndRecipientEmail(messageID, recipientEmail);
	}
	
	@Override
	public Long getNumberOfRecipients(Long messageID) {
		return reportedRecipientDAO.findNumberOfRecipientsByMessageID(messageID);
	}

	@Override
	public Long getNumerOfReportedRecipients(Long messageID, boolean sendingSuccesful) {
		return reportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingSuccesful(
			messageID, sendingSuccesful);
	}

	@Override
	public void updateReportedRecipient(ReportedRecipient reportedRecipient) {
		reportedRecipientDAO.update(reportedRecipient);
	}

	@Override
	public void saveReportedRecipients(Set<ReportedRecipient> reportedRecipients) {
		for (ReportedRecipient reportedRecipient : reportedRecipients) {
			reportedRecipientDAO.insert(reportedRecipient);
		}
	}
}
