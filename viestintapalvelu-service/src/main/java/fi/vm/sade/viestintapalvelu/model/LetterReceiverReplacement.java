package fi.vm.sade.viestintapalvelu.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fi.vm.sade.generic.model.BaseEntity;

/**
 * @author migar1
 *
 *CREATE TABLE kirjeet.vastaanottajakorvauskentat(
  id bigint NOT NULL,
  version bigint,
  vastaanottaja_id bigint,
  nimi character varying(255),
  oletus_arvo character varying(3000),
  pakollinen boolean,
  aikaleimia time with time zone,
  CONSTRAINT vastaanottajakorvauskentat_pk PRIMARY KEY (id),
  CONSTRAINT vastaanottajakorvauskentat_vastaanottaja_id_fkey FOREIGN KEY (vastaanottaja_id)
      REFERENCES kirjeet.vastaanottaja (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)

 */

@Table(name = "vastaanottajakorvauskentat", schema= "kirjeet")
@Entity()
public class LetterReceiverReplacement extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vastaanottaja_id")
    @JsonBackReference
    private LetterReceivers letterReceivers;

    @Column(name = "nimi")
    private String name = null;

    @Column(name = "oletus_arvo")
    private String defaultValue = null;

    @Column(name = "pakollinen")
    private boolean mandatory = false;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
            

	public LetterReceivers getLetterReceivers() {
		return letterReceivers;
	}

	public void setLetterReceivers(LetterReceivers letterReceivers) {
		this.letterReceivers = letterReceivers;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

	@Override
	public String toString() {
		return "LetterReceiverReplacement [letterReceivers=" + letterReceivers
				+ ", name=" + name + ", defaultValue=" + defaultValue
				+ ", mandatory=" + mandatory + ", timestamp=" + timestamp + "]";
	}

}
