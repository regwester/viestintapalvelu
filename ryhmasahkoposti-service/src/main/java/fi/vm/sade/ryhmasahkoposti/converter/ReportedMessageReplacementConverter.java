package fi.vm.sade.ryhmasahkoposti.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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
    public ReportedMessageReplacementConverter(CurrentUserComponent currentUserComponent) {        
    }

    /**
     * Convert message specific replacements
     * 
     * @param reportedMessage
     * @param templateReplacements
     * @param emailReplacements
     * @return
     * @throws IOException
     */
    public List<ReportedMessageReplacement> convert(ReportedMessage reportedMessage,
                    Set<ReplacementDTO> templateReplacements, List<ReplacementDTO> emailReplacements)
            throws IOException {
        List<ReportedMessageReplacement> reportedMessageReplacements = new ArrayList<ReportedMessageReplacement>();	

        for (ReplacementDTO replacement : templateReplacements) {
            // Check if not send from the email
            ReplacementDTO _replacement = getReplacement(emailReplacements, replacement.getName());
            if (_replacement != null)
                replacement = _replacement;

            ReportedMessageReplacement reportedMessageReplacement = new ReportedMessageReplacement();
            reportedMessageReplacement.setName(replacement.getName());
            reportedMessageReplacement.setDefaultValue(replacement.getDefaultValue());
            reportedMessageReplacement.setTimestamp(new Date());
            reportedMessageReplacement.setReportedMessage(reportedMessage);

            reportedMessageReplacements.add(reportedMessageReplacement);
        }

        return reportedMessageReplacements;
    }

    /**
     * Get field from replacements list. This method is used to get email fields. E.g. subject
     * 
     * @param templateReplacements
     * @param emailReplacements
     * @param replacementName
     * @return {@link ReplacementDTO}
     */
    public ReplacementDTO getEmailFieldFromReplacements(Set<ReplacementDTO> templateReplacements,
                            List<ReplacementDTO> emailReplacements, String replacementName) {
        if (templateReplacements == null && emailReplacements == null) {
            return null;
        }

        for (ReplacementDTO replacement : templateReplacements) {
            if (!StringUtils.equalsIgnoreCase(replacement.getName(), replacementName))  {
                continue;
            }

            // Check if not send from the email
            ReplacementDTO _replacement = getReplacement(emailReplacements, replacement.getName());
            if (_replacement != null) {
                return _replacement;
            } else {
                return replacement;
            }
        }

        return null;
    }

    /**
     * Return the replacement from list if found or null if not found
     * 
     * @param replacements
     * @param replacementName
     * @return {@link ReplacementDTO}
     */
    private ReplacementDTO getReplacement(List<ReplacementDTO> replacements, String replacementName) {
        if (replacements == null)
            return null;

        for (ReplacementDTO replacement : replacements) 
            if (StringUtils.equalsIgnoreCase(replacement.getName(), replacementName))
                return replacement;

        return null;

    }
}
