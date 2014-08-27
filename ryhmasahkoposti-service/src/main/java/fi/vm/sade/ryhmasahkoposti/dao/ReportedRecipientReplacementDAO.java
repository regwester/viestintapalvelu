package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipientReplacement;

/**
 * Interface to access {@link ReportedRecipientReplacement}
 * 
 * @author ovmol1
 *
 */
public interface ReportedRecipientReplacementDAO extends JpaDAO<ReportedRecipientReplacement, Long> {

    /**
     * Find reported recipient replacements
     * 
     * @param reportedRecipient
     * @return List of reported recipient replacements
     */
    public List<ReportedRecipientReplacement> findReportedRecipientReplacements(ReportedRecipient reportedRecipient);

}
