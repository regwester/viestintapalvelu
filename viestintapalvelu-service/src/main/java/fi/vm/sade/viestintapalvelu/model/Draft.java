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

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.generic.model.BaseEntity;

/*
 * CREATE TABLE kirjeet.luonnos (
  id bigint NOT NULL,
  version bigint NOT NULL,
  kirjepohjan_nimi character varying(255) NOT NULL,
  kirjepohjan_kielikoodi character varying(5) NOT NULL,
  aikaleima timestamp without time zone,
  oid_tallentaja character varying(255),
  oid_organisaatio character varying(255),
  haku character varying(255),
  hakukohde character varying(255),
  tunniste character varying(255),
  CONSTRAINT luonnos_pk PRIMARY KEY (id)
)
 */

@Table(name = "luonnos", schema="kirjeet")
@Entity(name = "Draft")
public class Draft extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "kirjepohjan_nimi", nullable = false)
    private String templateName;
	
    @Column(name = "kirjepohjan_kielikoodi", nullable = false)
    private String templateLanguage;
	
    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
	
    @Column(name = "oid_tallentaja", nullable = true)
    private String storingOid;

    @Column(name = "oid_organisaatio", nullable = true)
    private String organizationOid;	

	@Column(name = "haku")
	private String applicationPeriod;
	
	@Column(name = "hakukohde")
    private String fetchTarget;
	
	@Column(name = "tunniste")
    private String tag;

    @OneToMany(mappedBy = "draft", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<DraftReplacement> replacements;
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateLanguage() {
		return templateLanguage;
	}

	public void setTemplateLanguage(String templateLanguage) {
		this.templateLanguage = templateLanguage;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public Set<DraftReplacement> getReplacements() {
		return replacements;
	}

	public void setReplacements(Set<DraftReplacement> replacements) {
		this.replacements = replacements;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
