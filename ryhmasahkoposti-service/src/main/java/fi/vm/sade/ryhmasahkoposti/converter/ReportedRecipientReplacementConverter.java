package fi.vm.sade.ryhmasahkoposti.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedRecipientReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.common.ObjectMapperProvider;
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

        List<ReportedRecipientReplacement> reportedRecipientReplacements = new ArrayList<ReportedRecipientReplacement>();

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
        List<ReportedRecipientReplacementDTO> reportedRecipientReplacements = new ArrayList<ReportedRecipientReplacementDTO>();

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
