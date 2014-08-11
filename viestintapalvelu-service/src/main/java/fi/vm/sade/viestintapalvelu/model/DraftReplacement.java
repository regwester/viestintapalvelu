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

import fi.vm.sade.generic.model.BaseEntity;

/*
CREATE TABLE kirjeet.luonnoskorvauskentat (
  id bigint NOT NULL,
  version bigint NOT NULL,
  nimi character varying(255),
  oletus_arvo character varying(3000),
  aikaleima timestamp without time zone,
  pakollinen boolean,
  luonnos_id bigint,
  CONSTRAINT luonnoskorvauskentat_pk PRIMARY KEY (id),
  CONSTRAINT korvauskentat_luonnos_id_fkey FOREIGN KEY (luonnos_id)
      REFERENCES kirjeet.luonnos (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) 
 */

@Table(name = "luonnoskorvauskentat", schema= "kirjeet")
@Entity()
public class DraftReplacement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "luonnos_id")
    private Draft draft;

    @Column(name = "nimi")
    private String name = null;

    @Column(name = "oletus_arvo")
    private String defaultValue = null;

    @Column(name = "pakollinen")
    private boolean mandatory = false;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    
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

    public Draft getDraft() {
		return draft;
	}

	public void setDraft(Draft draft) {
		this.draft = draft;
	}

	public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

	@Override
	public String toString() {
		return "Replacement [draft=" + draft + ", name=" + name
				+ ", defaultValue=" + defaultValue + ", mandatory=" + mandatory
				+ ", timestamp=" + timestamp + "]";
	}

}
