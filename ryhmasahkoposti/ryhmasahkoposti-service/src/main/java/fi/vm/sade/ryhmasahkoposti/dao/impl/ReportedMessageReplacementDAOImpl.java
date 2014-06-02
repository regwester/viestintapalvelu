package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageReplacementDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageReplacement;

@Repository
public class ReportedMessageReplacementDAOImpl extends AbstractJpaDAOImpl<ReportedMessageReplacement, Long> 
implements ReportedMessageReplacementDAO {

    /**
     * Find reported message replacements
     * 
     * @param reportedMessage
     * @return List of reported message replacements
     */
    public List<ReportedMessageReplacement> findReportedMessageReplacements(ReportedMessage reportedMessage) {
	return findBy("reportedMessage", reportedMessage);
    }
}
