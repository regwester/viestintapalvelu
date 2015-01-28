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
import java.util.Set;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageAttachment;

public interface ReportedAttachmentService {

    /**
     * Hakee yksittäisen raportoitavan liitteen
     * 
     * @param attachmentID
     *            Liitteen tunniste
     * @return Tunnistetta vastaava raportoitava liite
     */
    ReportedAttachment getReportedAttachment(Long attachmentID);

    /**
     * Hakee raportoitavat liitteet aiemmin tallennetujen liitteiden tietojen
     * perusteella
     * 
     * @param attachmentResponses
     *            Kokoelma lähetettävien liitteiden tietoja
     * @return Kokoelma raportoitavia liitteitä
     */
    List<ReportedAttachment> getReportedAttachments(List<AttachmentResponse> attachmentResponses);

    /**
     * Hakee raportoitavat liitteet aiemmin tallennetujen liitteiden tietojen
     * perusteella
     * 
     * @param reportedMessageAttachments
     *            Kokoelma lähetettävien viestin liitteiden avaintietoja
     * @return Kokoelma raportoitavia liitteitä
     */
    List<ReportedAttachment> getReportedAttachments(Set<ReportedMessageAttachment> reportedMessageAttachments);

    /**
     * Tallentaa ryhmäsähköpostin raportoitavat liitteet
     * 
     * @param reportedAttachment
     *            Raportoitavan liitteen tiedot {@link ReportedAttachment}
     * @return Liitteen generoitu avain
     */
    Long saveReportedAttachment(ReportedAttachment reportedAttachment);
}
