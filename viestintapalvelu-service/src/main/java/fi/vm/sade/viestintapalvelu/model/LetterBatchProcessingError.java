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

import static javax.persistence.DiscriminatorType.STRING;

@Table(name = "kirjelahetysvirhe", schema = "kirjeet")
@Entity(name = "LetterBatchProcessingError")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tyyppi", discriminatorType = STRING, columnDefinition = "VARCHAR(64)")
public abstract class LetterBatchProcessingError extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kirjelahetys_id", nullable = false)
    @JsonBackReference
    private LetterBatch letterBatch;

    @Column(name = "virheen_syy", nullable = false)
    private String errorCause;

    @Column(name = "aika", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date errorTime;

    public String getErrorCause() {
        return errorCause;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }

    public LetterBatch getLetterBatch() {
        return letterBatch;
    }

    public void setLetterBatch(LetterBatch letterBatch) {
        this.letterBatch = letterBatch;
    }

    public Date getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(Date errorTime) {
        this.errorTime = errorTime;
    }

    @Override
    public String toString() {
        return "LetterBatchProcessingError{" + "errorCause='" + errorCause + '\'' + ", errorTime=" + errorTime + '}';
    }
}
