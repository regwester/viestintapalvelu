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
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import fi.vm.sade.generic.model.BaseEntity;

/**
 * @author migar1
 */

@Table(name = "vastaanottaja", schema = "kirjeet")
@Entity()
public class LetterReceivers extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kirjelahetys_id")
    @JsonBackReference
    private LetterBatch letterBatch;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "haluttukieli")
    private String wantedLanguage;

    @Column(name = "email_osoite")
    private String emailAddress;

    @Column(name = "oid_henkilo")
    private String oidPerson;

    @OneToMany(mappedBy = "letterReceivers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<LetterReceiverReplacement> letterReceiverReplacement;

    @OneToOne(mappedBy = "letterReceivers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private LetterReceiverAddress letterReceiverAddress;

    @OneToOne(mappedBy = "letterReceivers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private LetterReceiverEmail letterReceiverEmail;

    @OneToOne(mappedBy = "letterReceivers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private LetterReceiverLetter letterReceiverLetter;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "iposti")
    private IPosti containedInIposti;

    @Column(name = "ohita_iposti")
    private boolean skipIPost;

    public LetterBatch getLetterBatch() {
        return letterBatch;
    }

    public void setLetterBatch(LetterBatch letterBatch) {
        this.letterBatch = letterBatch;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Set<LetterReceiverReplacement> getLetterReceiverReplacement() {
        return letterReceiverReplacement;
    }

    public void setLetterReceiverReplacement(Set<LetterReceiverReplacement> letterReceiverReplacement) {
        this.letterReceiverReplacement = letterReceiverReplacement;
    }

    public LetterReceiverAddress getLetterReceiverAddress() {
        return letterReceiverAddress;
    }

    public void setLetterReceiverAddress(LetterReceiverAddress letterReceiverAddress) {
        this.letterReceiverAddress = letterReceiverAddress;
    }

    public LetterReceiverEmail getLetterReceiverEmail() {
        return letterReceiverEmail;
    }

    public void setLetterReceiverEmail(LetterReceiverEmail letterReceiverEmail) {
        this.letterReceiverEmail = letterReceiverEmail;
    }

    public LetterReceiverLetter getLetterReceiverLetter() {
        return letterReceiverLetter;
    }

    public void setLetterReceiverLetter(LetterReceiverLetter letterReceiverLetter) {
        this.letterReceiverLetter = letterReceiverLetter;
    }

    public String getWantedLanguage() {
        return wantedLanguage;
    }

    public void setWantedLanguage(String wantedLanguage) {
        this.wantedLanguage = wantedLanguage;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public IPosti getContainedInIposti() {
        return containedInIposti;
    }

    public void setContainedInIposti(IPosti containedInIposti) {
        this.containedInIposti = containedInIposti;
    }

    public String getOidPerson() {
        return oidPerson;
    }

    public void setOidPerson(String oidPerson) {
        this.oidPerson = oidPerson;
    }

    public boolean getSkipIPost() {
        return skipIPost;
    }

    public void setSkipIPost(boolean skipIPost) {
        this.skipIPost = skipIPost;
    }

    @Override
    public String toString() {
        return "LetterReceivers [letterBatch=" + letterBatch + ", timestamp=" + timestamp + ", letterReceiverReplacement=" + letterReceiverReplacement
                + ", letterReceiverAddress=" + letterReceiverAddress + ", letterReceiverEmail=" + letterReceiverEmail + ", letterReceiverLetter="
                + letterReceiverLetter + ", oidPerson=" + oidPerson + ", skipIPost=" + skipIPost + "]";
    }

}
