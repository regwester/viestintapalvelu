package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedRecipientReplacementDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipientReplacement;

@Repository
public class ReportedRecipentReplacementDAOImpl extends AbstractJpaDAOImpl<ReportedRecipientReplacement, Long> 
implements ReportedRecipientReplacementDAO {

    /**
     * Find reported recipient replacements
     * 
     * @param reportedRecipient
     * @return List of reported recipient replacements
     */
    public List<ReportedRecipientReplacement> findReportedRecipientReplacements(ReportedRecipient reportedRecipient) {
	return findBy("reportedRecipient", reportedRecipient);
    }
}
