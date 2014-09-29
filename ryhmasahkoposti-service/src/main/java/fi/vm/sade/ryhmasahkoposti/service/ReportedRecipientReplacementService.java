package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageReplacement;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipientReplacement;

/**
 * Interface for {@link ReportedMessageReplacement} object
 * 
 * @author ovmol1
 *
 */
public interface ReportedRecipientReplacementService {  

    /**
     * Same reported recipient replacements
     * 
     * @param reportedRecipientReplacements
     * @return
     */
    public  List<ReportedRecipientReplacement> saveReportedRecipientReplacements(List<ReportedRecipientReplacement> reportedRecipientReplacements);
    
    /**
     * Get reported recipient replacements
     * @param recipient
     * @return
     */
    public List<ReportedRecipientReplacement> getReportedRecipientReplacements(ReportedRecipient recipient);

}
