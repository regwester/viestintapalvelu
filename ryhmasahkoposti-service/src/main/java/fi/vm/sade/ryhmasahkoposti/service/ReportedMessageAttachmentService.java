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

import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.service.dto.ReportedRecipientAttachmentSaveDto;

/**
 * Rajapinta raportoitavan ryhmäsähköpostiviestin liittyvien liitteiden
 * käsittelyä varten
 * 
 * @author vehei1
 *
 */
public interface ReportedMessageAttachmentService {

    /**
     * Tallentaa raportoitavan ryhmäsähköpostiviestiin liittyvät liitteet
     * 
     * @param reportedMessage
     *            Raportoitavan ryhmäsähköpostiviestin tiedot
     * @param reportedAttachments
     *            Lista ryhmäsähköpostiin liitettyjen liitteiden tietoja
     */
    void saveReportedMessageAttachments(ReportedMessage reportedMessage, List<ReportedAttachment> reportedAttachments);

    /**
     * Tallentaa raportoitavan ryhmäsähköpostiviestin vastaanottajaan liittyvät
     * liitteet
     *
     * @param emailRecipient
     *            Raportoitavan ryhmäsähköpositn vastaanottaja
     * @param reportedAttachments
     *            Lista vastaajanottajaan liitettyjen liitteiden tietoja
     */
    void saveReportedRecipientAttachments(ReportedRecipient emailRecipient, List<ReportedAttachment> reportedAttachments);

    /**
     * Tallentaa raportoitavan ryhmäsähköpostiviestin vastaanottajan liitteen
     *
     * @param attachmentSaveDto
     * @return tallennetun ReportedMessageRecipientAttachment käsitteen id
     */
    long saveReportedRecipientAttachment(ReportedRecipientAttachmentSaveDto attachmentSaveDto);
}
