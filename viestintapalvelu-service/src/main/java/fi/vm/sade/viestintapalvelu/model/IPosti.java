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
package fi.vm.sade.viestintapalvelu.model;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name = "iposti", schema = "kirjeet")
@Entity(name = "IPosti")
public class IPosti extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kirjelahetys_id")
    @JsonBackReference
    private LetterBatch letterBatch;

    @Column(name = "luotu", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @Column(name = "lahetetty")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "aineisto")
    private byte[] content;

    @Column(name = "aineiston_nimi")
    private String contentName = "";

    @Column(name = "sisaltotyyppi")
    private String contentType = "";

    @Column(name = "jarjestysnumero")
    private Integer orderNumber;

    public IPosti() {
    }

    // /"SELECT p.id, p.version, p.kirjelahetys_id, p.luotu from IPosti p where p.sentDate is null",
    public IPosti(long id, long version, Date createDate, LetterBatch lb) {
        this.setId(id);
        this.setVersion(version);
        this.setCreateDate(createDate);
        this.setLetterBatch(lb);

    }

    public IPosti(long id, long version, Date createDate, LetterBatch lb, Date sentDate) {
        this.setId(id);
        this.setVersion(version);
        this.setCreateDate(createDate);
        this.setLetterBatch(lb);
        this.setSentDate(sentDate);
    }

    public LetterBatch getLetterBatch() {
        return letterBatch;
    }

    public void setLetterBatch(LetterBatch letterBatch) {
        this.letterBatch = letterBatch;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date date) {
        this.createDate = date;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date date) {
        this.sentDate = date;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }
}
