package fi.vm.sade.viestintapalvelu.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import fi.vm.sade.generic.model.BaseEntity;

/**
 * @author migar1
 *
CREATE TABLE kirjeet.vastaanottaja (
  id bigint NOT NULL,
  version bigint,
  kirjelahetys_id bigint,
  aikaleima time with time zone,
  CONSTRAINT vastaanottaja_pk PRIMARY KEY (id),
  CONSTRAINT vastaanottaja_kirjelahetys_id_fkey FOREIGN KEY (kirjelahetys_id)
      REFERENCES kirjeet.kirjelahetys (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */


@Table(name = "vastaanottaja", schema= "kirjeet")
@Entity()
public class LetterReceivers extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kirjelahetys_id")
    @JsonBackReference
    private LetterBatch letterBatch;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
        
    @OneToMany(mappedBy = "letterReceivers", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<LetterReceiverReplacement> letterReceiverReplacement;
     
    @OneToOne(mappedBy = "letterReceivers", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private LetterReceiverAddress letterReceiverAddress;
  
    @OneToOne(mappedBy = "letterReceivers", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private LetterReceiverLetter letterReceiverLetter;
  
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

	public void setLetterReceiverReplacement(
			Set<LetterReceiverReplacement> letterReceiverReplacement) {
		this.letterReceiverReplacement = letterReceiverReplacement;
	}
		
	public LetterReceiverAddress getLetterReceiverAddress() {
		return letterReceiverAddress;
	}

	public void setLetterReceiverAddress(LetterReceiverAddress letterReceiverAddress) {
		this.letterReceiverAddress = letterReceiverAddress;
	}

	public LetterReceiverLetter getLetterReceiverLetter() {
		return letterReceiverLetter;
	}

	public void setLetterReceiverLetter(LetterReceiverLetter letterReceiverLetter) {
		this.letterReceiverLetter = letterReceiverLetter;
	}

	@Override
	public String toString() {
		return "LetterReceivers [letterBatch=" + letterBatch + ", timestamp="
				+ timestamp + ", letterReceiverReplacement="
				+ letterReceiverReplacement + ", letterReceiverAddress="
				+ letterReceiverAddress + "]";
	}

}
