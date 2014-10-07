/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.ryhmasahkoposti.dao;

import java.io.Serializable;

import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 10:39
 */
public class RecipientReportedAttachmentQueryResult implements Serializable {
    private static final long serialVersionUID = 6088114037503242471L;
    private Long reportedRecipientId;
    private ReportedAttachment attachment;

    public RecipientReportedAttachmentQueryResult(Long reportedRecipientId, ReportedAttachment attachment) {
        this.reportedRecipientId = reportedRecipientId;
        this.attachment = attachment;
    }

    public Long getReportedRecipientId() {
        return reportedRecipientId;
    }

    public void setReportedRecipientId(Long reportedRecipientId) {
        this.reportedRecipientId = reportedRecipientId;
    }

    public ReportedAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(ReportedAttachment attachment) {
        this.attachment = attachment;
    }
}
