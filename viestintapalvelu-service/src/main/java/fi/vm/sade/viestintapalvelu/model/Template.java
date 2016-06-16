/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.viestintapalvelu.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.generic.model.BaseEntity;

@ApiModel(value = "Kirjetemplate")
@Table(name = "kirjepohja", schema= "kirjeet")
@Entity(name = "Template")
public class Template extends BaseEntity {

    private static final long serialVersionUID = 4178735997933155683L;
    
    public enum State {
        luonnos, suljettu, julkaistu
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="rakenne", nullable = false) // -- , updatable = false
    private Structure structure;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "nimi", nullable = false)
    private String name;

    @ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'")
    @Column(name = "kielikoodi", nullable = false)
    private String language;

	@Column(name="versionro", nullable=true)
	private String versionro;

    @ApiModelProperty(value = "Pohjan versiokohtainen kuvaus")
    @Column(name = "kuvaus", nullable = true)
    private String description;

    @Column(name = "oletuspohja", nullable = false)
    private boolean usedAsDefault = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "tila", nullable = false)
    private State state = State.luonnos;
    /**
     * Type email
     */
    public static final String TYPE_EMAIL = "email";

    /**
     * Type document
     */
    public static final String TYPE_LETTER = "letter";

    @Column(name = "oid_tallentaja", nullable = true)
    private String storingOid;

    @Column(name = "oid_organisaatio", nullable = true)
    private String organizationOid;

    @Deprecated
    @OneToMany(mappedBy = "template", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TemplateContent> contents = new HashSet<>();

    @OneToMany(mappedBy = "template", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Replacement> replacements;

    @ApiModelProperty(value = "Liittyvät haut")
    @OneToMany(mappedBy = "template", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TemplateApplicationPeriod> applicationPeriods = new HashSet<>(0);

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getVersionro() {
		return versionro;
	}

	public void setVersionro(String versionro) {
		this.versionro = versionro;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TemplateApplicationPeriod> getApplicationPeriods() {
        return applicationPeriods;
    }

    public boolean isUsedAsDefault() {
        return usedAsDefault;
    }

    public void setUsedAsDefault(boolean defaultTemplate) {
        this.usedAsDefault = defaultTemplate;
    }

    @Deprecated
    public void setContents(Set<TemplateContent> contents) {
        this.contents = contents;
    }
    
    public Set<Replacement> getReplacements() {
        return replacements;
    }
    
    public void setReplacements(Set<Replacement> replacements) {
        this.replacements = replacements;
    }

    protected void setApplicationPeriods(Set<TemplateApplicationPeriod> hakus) {
        this.applicationPeriods = hakus;
    }
    
    public void addApplicationPeriod(TemplateApplicationPeriod haku) {
        this.applicationPeriods.add(haku);
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
    }
    
    public void addReplacement(Replacement replacement) {
        replacements.add(replacement);
    }
}
