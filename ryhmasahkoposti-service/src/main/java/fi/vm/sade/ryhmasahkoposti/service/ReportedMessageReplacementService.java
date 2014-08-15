package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageReplacement;

/**
 * Interface for {@link ReportedMessageReplacement} object
 * 
 * @author ovmol1
 *
 */
public interface ReportedMessageReplacementService {  

    /**
     * Same reported messgae replacements
     * 
     * @param reportedMessageReplacements
     * @return
     */
    public  List<ReportedMessageReplacement> saveReportedMessageReplacements(List<ReportedMessageReplacement> reportedMessageReplacements);

    /**
     * Save message replacement
     * 
     * @param reportedMessageReplacement
     * @return
     */
    public ReportedMessageReplacement saveReportedMessageReplacement(ReportedMessageReplacement reportedMessageReplacement);
    
    /**
     * Get reported message replacements
     * 
     * @param messageID
     * @return
     */
    public List<ReportedMessageReplacement> getReportedMessageReplacements(ReportedMessage message);
	
}
