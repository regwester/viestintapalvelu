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
package fi.vm.sade.ryhmasahkoposti.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name = "raportoitavaviesti_raportoitavaliite")
@Entity
public class ReportedMessageAttachment extends BaseEntity {
    private static final long serialVersionUID = -8639217659820696701L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lahetettyviesti_id")
    private ReportedMessage reportedMessage;

    @Column(name = "raportoitavaliite_id")
    private Long reportedAttachmentID;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public ReportedMessage getReportedMessage() {
        return reportedMessage;
    }

    public void setReportedMessage(ReportedMessage reportedMessage) {
        this.reportedMessage = reportedMessage;
    }

    public Long getReportedAttachmentID() {
        return reportedAttachmentID;
    }

    public void setReportedAttachmentID(Long reportedAttachmentID) {
        this.reportedAttachmentID = reportedAttachmentID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
