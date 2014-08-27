package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageReplacementDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageReplacement;
import fi.vm.sade.ryhmasahkoposti.service.ReportedMessageReplacementService;
import fi.vm.sade.ryhmasahkoposti.service.ReportedRecipientReplacementService;

/**
 * Implementation class for {@link ReportedRecipientReplacementService}
 * 
 * @author ovmol1
 *
 */
@Service
public class ReportedMessageReplacementServiceImpl implements ReportedMessageReplacementService {

    /**
     * Reported message replacements
     * 
     */
    private ReportedMessageReplacementDAO reportedMessageReplacementDAO;

    @Autowired
    public ReportedMessageReplacementServiceImpl(ReportedMessageReplacementDAO reportedMessageReplacementDAO) {
	this.reportedMessageReplacementDAO = reportedMessageReplacementDAO;
    }

    /**
     * Save message replacements
     * 	
     */
    @Override
    public List<ReportedMessageReplacement> saveReportedMessageReplacements(
	    List<ReportedMessageReplacement> reportedMessageReplacements) {

	List<ReportedMessageReplacement> result = new ArrayList<ReportedMessageReplacement>();
	if (reportedMessageReplacements != null) {
	    for (ReportedMessageReplacement messageReplacement : reportedMessageReplacements) {
		reportedMessageReplacementDAO.insert(messageReplacement);
		result.add(messageReplacement);
	    }
	}
	return result;
    }

    /**
     * Save reported message replacement
     */
    public ReportedMessageReplacement saveReportedMessageReplacement(ReportedMessageReplacement reportedMessageReplacement) {
	return reportedMessageReplacementDAO.insert(reportedMessageReplacement);
    }
    
    /**
     * Get reported message replacements
     */
    public List<ReportedMessageReplacement> getReportedMessageReplacements(ReportedMessage message){
	return reportedMessageReplacementDAO.findBy("reportedMessage", message);
    }


}
