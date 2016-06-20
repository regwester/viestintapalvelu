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

import fi.vm.sade.externalinterface.common.ObjectMapperProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedRecipientReplacementDTO;
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

    @Autowired
    private ObjectMapperProvider objectMapperProvider;

    /**
     * Convert recipient specific replacements
     * 
     * @param reportedRecipient
     * @param replacements
     * @return
     * @throws IOException
     */
    public List<ReportedRecipientReplacement> convert(ReportedRecipient reportedRecipient,
                                                      List<ReportedRecipientReplacementDTO> replacements)
            throws IOException {

        List<ReportedRecipientReplacement> reportedRecipientReplacements = new ArrayList<>();

        ObjectMapper mapper = objectMapperProvider.getContext(ReportedRecipientReplacementConverter.class);

        for (ReportedRecipientReplacementDTO replacement : replacements) {

            ReportedRecipientReplacement messageRecipentReplacement = new ReportedRecipientReplacement();
            messageRecipentReplacement.setName(replacement.getName());
            if (replacement.getValue() != null) {
                String jsonValue = mapper.writeValueAsString(replacement.getValue());
                messageRecipentReplacement.setJsonValue(jsonValue);
                messageRecipentReplacement.setValue(null);
            } else {
                messageRecipentReplacement.setValue(replacement.getDefaultValue());
                messageRecipentReplacement.setJsonValue(null);
            }
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
        List<ReportedRecipientReplacementDTO> reportedRecipientReplacements = new ArrayList<>();

        ObjectMapper mapper = objectMapperProvider.getContext(ReportedRecipientReplacementConverter.class);

        for (ReportedRecipientReplacement replacement : replacements) {
            reportedRecipientReplacements.add(convert(replacement, new ReportedRecipientReplacementDTO(), mapper));
        }

        return reportedRecipientReplacements;
    }

    public ReportedRecipientReplacementDTO convert(ReportedRecipientReplacement from, ReportedRecipientReplacementDTO to) throws IOException {
        return convert(from, to, objectMapperProvider.getContext(ReportedRecipientReplacementConverter.class));
    }

    public ReportedRecipientReplacementDTO convert(ReportedRecipientReplacement from, ReportedRecipientReplacementDTO to,
                                                   ObjectMapper mapper) throws IOException {
        to.setName(from.getName());
        to.setDefaultValue(from.getValue());
        if (from.getJsonValue() != null) {
            Object value = mapper.readValue(from.getJsonValue(), Object.class);
            to.setValue(value);
        }
        return to;
    }

    public void setObjectMapperProvider(ObjectMapperProvider objectMapperProvider) {
        this.objectMapperProvider = objectMapperProvider;
    }
}
