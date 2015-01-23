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
package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageReplacementDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageReplacement;
import fi.vm.sade.ryhmasahkoposti.service.ReportedMessageReplacementService;
import fi.vm.sade.ryhmasahkoposti.service.ReportedRecipientReplacementService;

/**
 * Implementation class for {@link ReportedRecipientReplacementService}
 * 
 * @author ovmol1
 *
 */
@Service
public class ReportedMessageReplacementServiceImpl implements ReportedMessageReplacementService {

    /**
     * Reported message replacements
     * 
     */
    private ReportedMessageReplacementDAO reportedMessageReplacementDAO;

    @Autowired
    public ReportedMessageReplacementServiceImpl(ReportedMessageReplacementDAO reportedMessageReplacementDAO) {
        this.reportedMessageReplacementDAO = reportedMessageReplacementDAO;
    }

    /**
     * Save message replacements
     * 
     */
    @Override
    public List<ReportedMessageReplacement> saveReportedMessageReplacements(List<ReportedMessageReplacement> reportedMessageReplacements) {

        List<ReportedMessageReplacement> result = new ArrayList<ReportedMessageReplacement>();
        if (reportedMessageReplacements != null) {
            for (ReportedMessageReplacement messageReplacement : reportedMessageReplacements) {
                reportedMessageReplacementDAO.insert(messageReplacement);
                result.add(messageReplacement);
            }
        }
        return result;
    }

    /**
     * Save reported message replacement
     */
    public ReportedMessageReplacement saveReportedMessageReplacement(ReportedMessageReplacement reportedMessageReplacement) {
        return reportedMessageReplacementDAO.insert(reportedMessageReplacement);
    }

    /**
     * Get reported message replacements
     */
    public List<ReportedMessageReplacement> getReportedMessageReplacements(ReportedMessage message) {
        return reportedMessageReplacementDAO.findBy("reportedMessage", message);
    }

}
