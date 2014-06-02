package fi.vm.sade.ryhmasahkoposti.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.ReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageReplacement;

/**
 * Converter class from DTO
 * 
 * @author ovmol1
 *
 */
@Component
public class ReportedMessageReplacementConverter {

    CurrentUserComponent currentUserComponent;

    @Autowired
    public ReportedMessageReplacementConverter(CurrentUserComponent currentUserComponent) {}

    /**
     * Convert recipient specific replacements
     * 
     * @param reportedMessage
     * @param reportedRecipientReplacements
     * @return
     * @throws IOException
     */
    public List<ReportedMessageReplacement> convert(ReportedMessage reportedMessage, 
	    Set<ReplacementDTO> replacements) 
		    throws IOException {

	List<ReportedMessageReplacement> reportedMessageReplacements = new ArrayList<ReportedMessageReplacement>();	

	for (ReplacementDTO replacement : replacements) {

	    ReportedMessageReplacement messageRecipentMessageReplacement = new ReportedMessageReplacement();
	    messageRecipentMessageReplacement.setName(replacement.getName());
	    messageRecipentMessageReplacement.setDefaultValue(replacement.getDefaultValue());
	    messageRecipentMessageReplacement.setTimestamp(new Date());

	    messageRecipentMessageReplacement.setReportedMessage(reportedMessage);

	    reportedMessageReplacements.add(messageRecipentMessageReplacement);
	}

	return reportedMessageReplacements;
    }


}
