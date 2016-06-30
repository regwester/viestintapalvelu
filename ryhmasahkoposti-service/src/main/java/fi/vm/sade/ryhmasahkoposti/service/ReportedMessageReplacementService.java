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
    List<ReportedMessageReplacement> saveReportedMessageReplacements(List<ReportedMessageReplacement> reportedMessageReplacements);

    /**
     * Save message replacement
     * 
     * @param reportedMessageReplacement
     * @return
     */
    ReportedMessageReplacement saveReportedMessageReplacement(ReportedMessageReplacement reportedMessageReplacement);

    /**
     * Get reported message replacements
     * 
     * @param message
     * @return
     */
    List<ReportedMessageReplacement> getReportedMessageReplacements(ReportedMessage message);

}
