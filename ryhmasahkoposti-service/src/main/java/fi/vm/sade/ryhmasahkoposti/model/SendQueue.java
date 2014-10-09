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

import fi.vm.sade.generic.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User: ratamaa
 * Date: 15.9.2014
 * Time: 10:00
 */
@Entity
@Table(name = "lahetysjono")
public class SendQueue extends BaseEntity {
    private static final long serialVersionUID = 7773014938106380630L;

    @Column(name="luotu", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(name="tila", nullable = false)
    @Enumerated(EnumType.STRING)
    private SendQueueState state = SendQueueState.CREATED;

    @Column(name="viimeksi_kasitelty", nullable=true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastHandledAt;

    @Column(name="suoritettu", nullable=true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "queue")
    private Set<ReportedRecipient> recipients = new HashSet<ReportedRecipient>(0);

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public SendQueueState getState() {
        return state;
    }

    public void setState(SendQueueState state) {
        this.state = state;
    }

    public Date getLastHandledAt() {
        return lastHandledAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public void setLastHandledAt(Date lastHandledAt) {
        this.lastHandledAt = lastHandledAt;
    }

    public Set<ReportedRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<ReportedRecipient> recipients) {
        this.recipients = recipients;
    }

    @Override
    public String toString() {
        return "SendQueue{" +
                "createdAt=" + createdAt +
                ", state=" + state +
                ", lastHandledAt=" + lastHandledAt +
                ", finishedAt=" + finishedAt +
                '}';
    }
}
