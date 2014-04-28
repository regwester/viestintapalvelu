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

import org.codehaus.jackson.annotate.JsonManagedReference;

import fi.vm.sade.generic.model.BaseEntity;

/**
 *
  CREATE TABLE kirjeet.kirjelahetys ( <br/>
  id bigint NOT NULL,<br/>
  version bigint NOT NULL,<br/>
  template_id bigint NOT NULL,<br/>
  aikaleima timestamp without time zone,<br/>
  oid_tallentaja character varying(255),<br/>
  oid_organisaatio character varying(255),<br/>
  kielikoodi character varying(5),<br/>
  template_name character varying(255),<br/>
  hakukohde character varying(255),<br/>
  tunniste character varying(255),<br/>
  haku character varying(255),<br/>
  CONSTRAINT kirjelahetys_pk PRIMARY KEY (id) <br/>
 *
 * @author migar1
 *
 */

@Table(name = "kirjelahetys", schema="kirjeet")
@Entity(name = "LetterBatch")
public class LetterBatch extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "template_id")
    private Long templateId;
	
	@Column(name = "template_name")
    private String templateName;
	
	@Column(name = "haku")
	private String applicationPeriod;
	
	@Column(name = "hakukohde")
    private String fetchTarget;
	
	@Column(name = "tunniste")
    private String tag;
    
    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
	
    @Column(name = "kielikoodi", nullable = false)
    private String language;
    
    @Column(name = "oid_tallentaja", nullable = true)
    private String storingOid;

    @Column(name = "oid_organisaatio", nullable = true)
    private String organizationOid;
    
    @OneToMany(mappedBy = "letterBatch", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<LetterReplacement> letterReplacements;
 
    @OneToMany(mappedBy = "letterBatch", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<LetterReceivers> letterReceivers;
 
    
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getApplicationPeriod() {
		return applicationPeriod;
	}

	public void setApplicationPeriod(String applicationPeriod) {
		this.applicationPeriod = applicationPeriod;
	}

	public String getFetchTarget() {
		return fetchTarget;
	}

	public void setFetchTarget(String fetchTarget) {
		this.fetchTarget = fetchTarget;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public String getTag() {
        return tag;
    }
    
	@Override
	public String toString() {
		return "LetterBatch [templateId=" + templateId 
				+ ", templateName=" + templateName 
				+ ", applicationPeriod=" + applicationPeriod
				+ ", fetchTarget=" + fetchTarget
				+ ", timestamp=" + timestamp + ", language=" + language
				+ ", storingOid=" + storingOid + ", organizationOid="
				+ organizationOid + "]";
	}

	
}
