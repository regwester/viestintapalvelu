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

package fi.vm.sade.ryhmasahkoposti.model;

import java.util.Date;

import javax.persistence.*;

import fi.vm.sade.generic.model.BaseEntity;

/**
 * User: ratamaa
 * Date: 23.9.2014
 * Time: 15:20
 */
@Entity
@Table(name="raportoitavavastaanottaja_raportoitavaliite")
public class ReportedMessageRecipientAttachment extends BaseEntity {
    private static final long serialVersionUID = -929659275543338034L;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="vastaanottaja", nullable = false)
    private ReportedRecipient recipient;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="liite", nullable = false)
    private ReportedAttachment attachment;

    @Column(name="aikaleima", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    public ReportedRecipient getRecipient() {
        return recipient;
    }

    public void setRecipient(ReportedRecipient recipient) {
        this.recipient = recipient;
    }

    public ReportedAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(ReportedAttachment attachment) {
        this.attachment = attachment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
