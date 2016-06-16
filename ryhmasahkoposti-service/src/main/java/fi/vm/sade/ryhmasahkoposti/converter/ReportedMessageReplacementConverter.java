/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
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
    public List<ReportedMessageReplacement> convert(ReportedMessage reportedMessage, Set<ReplacementDTO> templateReplacements,
            List<ReplacementDTO> emailReplacements) {
        List<ReportedMessageReplacement> reportedMessageReplacements = new ArrayList<>();

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
     * Get field from replacements list. This method is used to get email
     * fields. E.g. subject
     * 
     * @param templateReplacements
     * @param emailReplacements
     * @param replacementName
     * @return {@link ReplacementDTO}
     */
    public ReplacementDTO getEmailFieldFromReplacements(Set<ReplacementDTO> templateReplacements, List<ReplacementDTO> emailReplacements, String replacementName) {
        if (templateReplacements == null && emailReplacements == null) {
            return null;
        }

        for (ReplacementDTO replacement : templateReplacements) {
            if (!StringUtils.equalsIgnoreCase(replacement.getName(), replacementName)) {
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
