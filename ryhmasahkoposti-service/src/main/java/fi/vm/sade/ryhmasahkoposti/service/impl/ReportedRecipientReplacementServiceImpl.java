package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.dao.ReportedRecipientReplacementDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipientReplacement;
import fi.vm.sade.ryhmasahkoposti.service.ReportedMessageReplacementService;
import fi.vm.sade.ryhmasahkoposti.service.ReportedRecipientReplacementService;

/**
 * Implementation class for {@link ReportedMessageReplacementService}
 * 
 * @author ovmol1
 *
 */
@Service
public class ReportedRecipientReplacementServiceImpl implements ReportedRecipientReplacementService {

    /**
     * Reported message replacements
     * 
     */
    private ReportedRecipientReplacementDAO reportedRecipientReplacementDAO;

    @Autowired
    public ReportedRecipientReplacementServiceImpl(ReportedRecipientReplacementDAO reportedRecipientReplacementDAO) {
	this.reportedRecipientReplacementDAO = reportedRecipientReplacementDAO;
    }

    /**
     * Save recipientreplacements
     * 	
     */
    @Override
    public List<ReportedRecipientReplacement> saveReportedRecipientReplacements(
	    List<ReportedRecipientReplacement> reportedRecientReplacements) {

	List<ReportedRecipientReplacement> result = new ArrayList<ReportedRecipientReplacement>();
	if (reportedRecientReplacements != null) {
	    for (ReportedRecipientReplacement receiverReplacement : reportedRecientReplacements) {
		reportedRecipientReplacementDAO.insert(receiverReplacement);
		result.add(receiverReplacement);
	    }
	}
	return result;
    }

    /**
     * Get reported recipient replacements
     */
    @Override
    public List<ReportedRecipientReplacement> getReportedRecipientReplacements(ReportedRecipient recipient){
	return reportedRecipientReplacementDAO.findBy("reportedRecipient", recipient);
    }

}
