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

import fi.vm.sade.generic.model.BaseEntity;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 17:37
 *
 * Temporary generated attachment for email generation.
 *
 * Instances of these will be deleted from the database after
 * email sending is complete for the queue in which this receiver
 * is being processed.
 */
@Entity
@Table(name="vastaanottajakirje_liite", schema = "kirjeet")
public class LetterReceiverLetterAttachment extends BaseEntity {
    private static final long serialVersionUID = 8512814336232572709L;

    @ManyToOne(fetch = FetchType.LAZY)
    // TODO: should be nullable=false but due to LetterBuilder.buildPDF email sending, LetterReceiverLetter is not in the DB yet
    @JoinColumn(name="vastaanottajakirje")
    private LetterReceiverLetter letterReceiverLetter;

    @Column(name = "nimi", nullable = false)
    private String name;

    @Column(name="sisalto", nullable = false, length = 10 * 1024 * 1024)
    private byte[] contents;

    @Column(name="tyyppi", nullable = false)
    private String contentType;

    @Column(name="luontiaika", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public LetterReceiverLetter getLetterReceiverLetter() {
        return letterReceiverLetter;
    }

    public void setLetterReceiverLetter(LetterReceiverLetter letterReceiverLetter) {
        this.letterReceiverLetter = letterReceiverLetter;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
