package fi.vm.sade.viestintapalvelu.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fi.vm.sade.generic.model.BaseEntity;

/**
 * @author migar1
 *
 *CREATE TABLE kirjeet.vastaanottajakirje(
  id bigint NOT NULL,
  version bigint,
  vastaanottaja_id bigint,
  aikaleima time without time zone,
  kirje bytea,
  sisaltotyyppi character varying(255),
  alkuperainensisaltotyyppi character varying(255),
  CONSTRAINT vastaanottajakirje_pk PRIMARY KEY (id),
  CONSTRAINT vastaanottajakirje_vastaanottaja_id_fkey FOREIGN KEY (vastaanottaja_id)
      REFERENCES kirjeet.vastaanottaja (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)

 */

@Table(name = "vastaanottajakirje", schema= "kirjeet")
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

	@Override
	public String toString() {
		return "LetterReceiverLetter [letter=" + letter + ", timestamp=" + timestamp + "]";
	}


}
