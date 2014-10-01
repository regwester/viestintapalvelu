package fi.vm.sade.viestintapalvelu.model;

import java.io.IOException;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Column(name = "json_arvo")
    private String jsonValue = null;

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

    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @Override
	public String toString() {
		return "LetterReceiverReplacement [name=" + name + ", defaultValue=" + defaultValue
				+ ", mandatory=" + mandatory + ", timestamp=" + timestamp + "]";
	}

    @Transient
    public Object getEffectiveValue(ObjectMapper mapper) throws IOException {
        if (this.jsonValue != null) {
            return mapper.readValue(this.jsonValue, Object.class);
        }
        return this.defaultValue;
    }
}
