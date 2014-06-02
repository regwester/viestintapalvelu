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

import org.codehaus.jackson.annotate.JsonBackReference;

import fi.vm.sade.generic.model.BaseEntity;

/**
 * @author migar1
 *
CREATE TABLE kirjeet.lahetyskorvauskentat(
  id bigint NOT NULL,
  version bigint,
  kirjelahetys_id bigint,
  nimi character varying(255),
  oletus_arvo character varying(3000),
  aikaleima time with time zone,
  pakollinen boolean,
  CONSTRAINT lahetyskorvauskentat_pk PRIMARY KEY (id),
  CONSTRAINT lahetyskorvauskentat_kirjelahetys_id_fkey FOREIGN KEY (kirjelahetys_id)
      REFERENCES kirjeet.kirjelahetys (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */

@Table(name = "lahetyskorvauskentat", schema="kirjeet")
@Entity()
public class LetterReplacement extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kirjelahetys_id")
    @JsonBackReference
    private LetterBatch letterBatch;

    @Column(name = "nimi")
    private String name = null;

    @Column(name = "oletus_arvo")
    private String defaultValue = null;

    @Column(name = "pakollinen")
    private boolean mandatory = false;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
        
    
    public LetterBatch getLetterBatch() {
		return letterBatch;
	}

	public void setLetterBatch(LetterBatch letterBatch) {
		this.letterBatch = letterBatch;
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
		return "LetterReplacement [name="+ name + ", defaultValue=" + defaultValue + ", mandatory="
				+ mandatory + ", timestamp=" + timestamp + "]";
	}


}
