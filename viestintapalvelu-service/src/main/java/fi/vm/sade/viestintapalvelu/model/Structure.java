/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.viestintapalvelu.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 9:47
 */
@Entity
@Table(name="rakenne", schema = "kirjeet",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"nimi", "kielikoodi", "aikaleima"}))
public class Structure implements Serializable {
    private static final long serialVersionUID = 6784697038459997383L;

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @GeneratedValue(generator = "rakenne_id_seq")
    @SequenceGenerator(name = "rakenne_id_seq", sequenceName = "rakenne_id_seq")
    private Long id;

    @Column(name="nimi", nullable = false, updatable = false, length = 511)
    private String name;

    // The description text for UI, only the structures with description are to be selectable in he UI
    @Column(name="kuvaus", updatable = false, length = 511)
    private String description;

    @Column(name="kielikoodi", nullable = false, updatable = false, length = 7)
    private String language;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="aikaleima", nullable = false, updatable = false)
    private Date timestamp = new Date();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "structure",
            cascade = CascadeType.PERSIST)
    private Set<ContentStructure> contentStructures = new HashSet<ContentStructure>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "structure",
            cascade = CascadeType.PERSIST)
    private Set<ContentReplacement> replacements = new HashSet<ContentReplacement>();

    public Structure() {
    }

    public Structure(String name, String description, String language) {
        this.name = name;
        this.description = description;
        this.language = language;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Set<ContentStructure> getContentStructures() {
        return contentStructures;
    }

    protected void setContentStructures(Set<ContentStructure> contentStructures) {
        this.contentStructures = contentStructures;
    }

    public Set<ContentReplacement> getReplacements() {
        return replacements;
    }

    protected void setReplacements(Set<ContentReplacement> replacements) {
        this.replacements = replacements;
    }
}
