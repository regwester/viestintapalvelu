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
package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageReplacement;

/**
 * Interface to access {@link ReportedMessageReplacement}
 * 
 * @author ovmol1
 *
 */
public interface ReportedMessageReplacementDAO extends JpaDAO<ReportedMessageReplacement, Long> {

    /**
     * Find reported message replacements
     * 
     * @param reportedMessage
     * @return List of reported replacements replacements
     */
    List<ReportedMessageReplacement> findReportedMessageReplacements(ReportedMessage reportedMessage);

}
