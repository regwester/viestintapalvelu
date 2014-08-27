package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageReplacement;

/**
 * Interface to access {@link ReportedMessageReplacement}
 * 
 * @author ovmol1
 *
 */
public interface ReportedMessageReplacementDAO extends JpaDAO<ReportedMessageReplacement, Long> {

    /**
     * Find reported message replacements
     * 
     * @param reportedMessage
     * @return List of reported replacements replacements
     */
    public List<ReportedMessageReplacement> findReportedMessageReplacements(ReportedMessage reportedMessage);

}
