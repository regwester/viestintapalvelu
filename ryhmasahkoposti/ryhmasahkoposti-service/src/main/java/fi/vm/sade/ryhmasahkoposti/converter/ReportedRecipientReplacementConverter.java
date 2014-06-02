package fi.vm.sade.ryhmasahkoposti.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedRecipientReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipientReplacement;

/**
 * Converter class from DTO
 * 
 * @author ovmol1
 *
 */
@Component
public class ReportedRecipientReplacementConverter {

    CurrentUserComponent currentUserComponent;

    @Autowired
    public ReportedRecipientReplacementConverter(CurrentUserComponent currentUserComponent) {}

    /**
     * Convert recipient specific replacements
     * 
     * @param reportedMessage
     * @param reportedRecipientReplacements
     * @return
     * @throws IOException
     */
    public List<ReportedRecipientReplacement> convert(ReportedRecipient reportedRecipient, 
	    List<ReportedRecipientReplacementDTO> replacements) 
		    throws IOException {

	List<ReportedRecipientReplacement> reportedRecipientReplacements = new ArrayList<ReportedRecipientReplacement>();	

	for (ReportedRecipientReplacementDTO replacement : replacements) {

	    ReportedRecipientReplacement messageRecipentReplacement = new ReportedRecipientReplacement();
	    messageRecipentReplacement.setName(replacement.getName());
	    messageRecipentReplacement.setDefaultValue(replacement.getDefaultValue());
	    messageRecipentReplacement.setTimestamp(new Date());

	    messageRecipentReplacement.setReportedRecipient(reportedRecipient);

	    reportedRecipientReplacements.add(messageRecipentReplacement);
	}

	return reportedRecipientReplacements;
    }

    /**
     * Convert to DTO
     * 
     * @param replacements
     * @return
     * @throws IOException
     */
    public List<ReportedRecipientReplacementDTO> convertDTO(List<ReportedRecipientReplacement> replacements) 
	    throws IOException {

	List<ReportedRecipientReplacementDTO> reportedRecipientReplacements = new ArrayList<ReportedRecipientReplacementDTO>();	

	for (ReportedRecipientReplacement replacement : replacements) {

	    ReportedRecipientReplacementDTO messageRecipentReplacement = new ReportedRecipientReplacementDTO();
	    messageRecipentReplacement.setName(replacement.getName());
	    messageRecipentReplacement.setDefaultValue(replacement.getDefaultValue());

	    reportedRecipientReplacements.add(messageRecipentReplacement);
	}

	return reportedRecipientReplacements;
    }


}
