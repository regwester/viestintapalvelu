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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name = "raportoitavavastaanottajakorvauskentat")
@Entity
public class ReportedRecipientReplacement extends BaseEntity {

    private static final long serialVersionUID = -7834429548964811085L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raportoitavavastaanottaja_id")
    private ReportedRecipient reportedRecipient;

    @Column(name = "nimi")
    private String name = null;

    @Column(name = "oletus_arvo")
    private String value = null;

    @Column(name = "json_arvo")
    private String jsonValue = null;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    /**
     * @return the reportedRecipient
     */
    public ReportedRecipient getReportedRecipient() {
        return reportedRecipient;
    }

    /**
     * @param reportedRecipient
     *            the reportedRecipient to set
     */
    public void setReportedRecipient(ReportedRecipient reportedRecipient) {
        this.reportedRecipient = reportedRecipient;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ReportedRecipientReplacement [reportedRecipient=" + reportedRecipient + ", name=" + name + ", value=" + value + ", jsonValue=" + jsonValue
                + ", timestamp=" + timestamp + "]";
    }
}
