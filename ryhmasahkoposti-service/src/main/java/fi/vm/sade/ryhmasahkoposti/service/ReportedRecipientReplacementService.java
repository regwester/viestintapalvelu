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
