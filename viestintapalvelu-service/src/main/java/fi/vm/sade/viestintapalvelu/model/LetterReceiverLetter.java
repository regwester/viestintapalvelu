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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fi.vm.sade.generic.model.BaseEntity;

/**
 * @author migar1
 */

@Table(name = "vastaanottajakirje", schema = "kirjeet")
@Entity()
public class LetterReceiverLetter extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @OneToOne()
    @JoinColumn(name = "vastaanottaja_id")
    @JsonBackReference
    private LetterReceivers letterReceivers;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "kirje", length = 10 * 1024 * 1024)
    private byte[] letter;

    @Column(name = "sisaltotyyppi")
    private String contentType = "";

    @Column(name = "alkuperainensisaltotyyppi")
    private String originalContentType = "";

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "letterReceiverLetter", cascade = CascadeType.PERSIST)
    private Set<LetterReceiverLetterAttachment> attachments = new HashSet<LetterReceiverLetterAttachment>();

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getLetter() {
        return letter;
    }

    public void setLetter(byte[] letter) {
        this.letter = letter;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getOriginalContentType() {
        return originalContentType;
    }

    public void setOriginalContentType(String originalContentType) {
        this.originalContentType = originalContentType;
    }

    public LetterReceivers getLetterReceivers() {
        return letterReceivers;
    }

    public void setLetterReceivers(LetterReceivers letterReceivers) {
        this.letterReceivers = letterReceivers;
    }

    public Set<LetterReceiverLetterAttachment> getAttachments() {
        return attachments;
    }

    protected void setAttachments(Set<LetterReceiverLetterAttachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "LetterReceiverLetter [letter=" + letter + ", timestamp=" + timestamp + "]";
    }

}
