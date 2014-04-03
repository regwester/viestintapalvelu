package fi.vm.sade.viestintapalvelu.model;



import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.vm.sade.generic.model.BaseEntity;

/**
 * @author migar1
 *
  CREATE TABLE kirjeet.kirjelahetys (
  id bigint NOT NULL,
  version bigint NOT NULL,
  template_id bigint NOT NULL,
  aikaleima timestamp without time zone,
  oid_tallentaja character varying(255),
  oid_organisaatio character varying(255),
  CONSTRAINT kirjelahetys_pk PRIMARY KEY (id)
 */

@Table(name = "kirjelahetys", schema="kirjeet")
@Entity(name = "LetterBatch")
public class LetterBatch extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "template_id")
    private Long templateId;
	
    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
	
    @Column(name = "oid_tallentaja", nullable = true)
    private String storingOid;

    @Column(name = "oid_organisaatio", nullable = true)
    private String organizationOid;
    
    @OneToMany(mappedBy = "letterBatch", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<LetterReplacement> letterReplacements;
 
    @OneToMany(mappedBy = "letterBatch", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<LetterReceivers> letterReceivers;
 
    
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getStoringOid() {
		return storingOid;
	}

	public void setStoringOid(String storingOid) {
		this.storingOid = storingOid;
	}

	public String getOrganizationOid() {
		return organizationOid;
	}

	public void setOrganizationOid(String organizationOid) {
		this.organizationOid = organizationOid;
	}

	public Set<LetterReplacement> getLetterReplacements() {
		return letterReplacements;
	}

	public void setLetterReplacements(Set<LetterReplacement> letterReplacements) {
		this.letterReplacements = letterReplacements;
	}

	public Set<LetterReceivers> getLetterReceivers() {
		return letterReceivers;
	}

	public void setLetterReceivers(Set<LetterReceivers> letterReceivers) {
		this.letterReceivers = letterReceivers;
	}

	@Override
	public String toString() {
		return "LetterBatch [templateId=" + templateId + ", timestamp="
				+ timestamp + ", storingOid=" + storingOid
				+ ", organizationOid=" + organizationOid
				+ ", letterReplacements=" + letterReplacements
				+ ", letterReceivers=" + letterReceivers + "]";
	}

}
