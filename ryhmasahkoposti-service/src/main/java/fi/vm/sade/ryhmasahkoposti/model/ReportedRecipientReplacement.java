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

/**
 * CREATE TABLE raportoitavavastaanottajakorvauskentat (
 * id bigint NOT NULL,
 * version bigint NOT NULL,
 * raportoitavavastaanottaja_id bigint NOT NULL,
 * nimi character varying(255),
 * oletus_arvo character varying(3000),
 * aikaleima timestamp without time zone,
 * CONSTRAINT raportoitavavastaanottajakorvauskentat_pk PRIMARY KEY (id),
 * CONSTRAINT raportoitavavastaanottajakorvauskentat_raportoitavavastaanottaja_id_fkey FOREIGN KEY (raportoitavavastaanottaja_id)
 * REFERENCES raportoitavavastaanottaja (id) MATCH SIMPLE
 * ON UPDATE NO ACTION ON DELETE NO ACTION
 * );
 */

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

    @Column(name= "json_arvo")
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
     * @param reportedRecipient the reportedRecipient to set
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
     * @param name the name to set
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
     * @param value the value to set
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
     * @param timestamp the timestamp to set
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

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
    @Override
    public String toString() {
        return "ReportedRecipientReplacement [reportedRecipient=" + reportedRecipient
                + ", name=" + name + ", value=" + value + ", jsonValue="+jsonValue
                +", timestamp=" + timestamp + "]";
    }
}
