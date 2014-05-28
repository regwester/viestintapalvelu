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
 *
 * Model used to store default message replacements retrieved from the template
 * 
 *
CREATE TABLE raportoitavaviestikorvauskentat (
 id bigint NOT NULL,
 version bigint NOT NULL,
 raportoitavaviesti_id bigint NOT NULL,
 nimi character varying(255),
 oletus_arvo character varying(3000),
 aikaleima timestamp without time zone,
 CONSTRAINT raportoitavaviestikorvauskentat_pk PRIMARY KEY (id),
 CONSTRAINT raportoitavaviestikorvauskentat_raportoitavaviesti_id_fkey FOREIGN KEY (raportoitavaviesti_id)
     REFERENCES raportoitavaviesti (id) MATCH SIMPLE
     ON UPDATE NO ACTION ON DELETE NO ACTION
 );
 */

@Table(name="raportoitavaviestikorvauskentat")
@Entity
public class ReportedMessageReplacement extends BaseEntity {

    private static final long serialVersionUID = -7834429548964811085L;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="raportoitavaviesti_id")
    private ReportedMessage reportedMessage;

    @Column(name = "nimi")
    private String name = null;

    @Column(name = "oletus_arvo")
    private String defaultValue = null;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    /**
     * @return the reportedMessage
     */
    public ReportedMessage getReportedMessage() {
	return reportedMessage;
    }

    /**
     * @param reportedMessage the reportedMessage to set
     */
    public void setReportedMessage(ReportedMessage reportedMessage) {
	this.reportedMessage = reportedMessage;
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
     * @return the defaultValue
     */
    public String getDefaultValue() {
	return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
	this.defaultValue = defaultValue;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ReportedMessageReplacement [reportedMessage="
		+ reportedMessage + ", name=" + name + ", defaultValue="
		+ defaultValue + ", timestamp=" + timestamp + "]";
    }
}
