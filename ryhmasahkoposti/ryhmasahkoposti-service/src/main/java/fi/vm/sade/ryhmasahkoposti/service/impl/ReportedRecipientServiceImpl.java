package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedRecipientDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.route.HenkiloRoute;
import fi.vm.sade.ryhmasahkoposti.service.ReportedRecipientService;
import fi.vm.sade.ryhmasahkoposti.validation.OidValidator;

@Service
public class ReportedRecipientServiceImpl implements ReportedRecipientService {
    private HenkiloRoute henkiloRoute;
	private ReportedRecipientDAO reportedRecipientDAO;
	
	@Autowired
	public ReportedRecipientServiceImpl(HenkiloRoute henkiloRoute, ReportedRecipientDAO reportedRecipientDAO) {
	    this.henkiloRoute = henkiloRoute;
		this.reportedRecipientDAO = reportedRecipientDAO;
	}
	
	@Override
	public Date getLatestReportedRecipientsSendingEndedDate(Long messageID) {
		return reportedRecipientDAO.findMaxValueOfSendingEndedByMessageID(messageID);
	}

	@Override
	public Long getNumberOfSendingFailed(Long messageID) {
		return reportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingSuccesful(messageID, false);
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
		
		Long nbrOfSuccesful = reportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingSuccesful(messageID, true);
		if (nbrOfSuccesful != null) {
			nbrOfSuccesfulAndFailed += nbrOfSuccesful.longValue();
		}
		sendingStatus.setNumberOfSuccesfulSendings(nbrOfSuccesful);
		
		Long nbrOfFailed = reportedRecipientDAO.findNumberOfRecipientsByMessageIDAndSendingSuccesful(messageID, false);
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
		    
    	    if (OidValidator.isHenkiloOID("1.2.246.562.24.16711044433")) {
    		    Henkilo henkilo = henkiloRoute.getHenkilo("1.2.246.562.24.16711044433");
    		    
    		    String searchName = henkilo.getSukunimi() + "," + henkilo.getEtunimet();
    		    reportedRecipient.setSearchName(searchName);
    		    reportedRecipient.setSocialSecurityID(henkilo.getHetu());
    		}
		    
			reportedRecipientDAO.insert(reportedRecipient);
		}
	}

	@Override
	public void updateReportedRecipient(ReportedRecipient reportedRecipient) {
		reportedRecipientDAO.update(reportedRecipient);
	}
}
