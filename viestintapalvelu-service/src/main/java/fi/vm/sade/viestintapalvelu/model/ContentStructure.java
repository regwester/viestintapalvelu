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

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.*;

import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.structure.dto.TypedContentStructure;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 9:53
 */
@Entity
@Table(name = "sisalto_rakenne", schema = "kirjeet",
        uniqueConstraints = @UniqueConstraint(columnNames = {"rakenne", "tyyppi"}))
public class ContentStructure implements Serializable, TypedContentStructure {
    private static final long serialVersionUID = -1419222938581144952L;

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @GeneratedValue(generator = "sisalto_rakenne_id_seq")
    @SequenceGenerator(name = "sisalto_rakenne_id_seq", sequenceName = "sisalto_rakenne_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rakenne", nullable = false, updatable = false)
    private Structure structure;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="tyyli", nullable = true, updatable = false)
    private Style style;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="aikaleima", nullable = false, updatable = false)
    private Date timestamp = new Date();

    @Enumerated(EnumType.STRING)
    @Column(name="tyyppi", nullable = false, updatable = false)
    private ContentStructureType type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentStructure", cascade = CascadeType.PERSIST)
    private Set<ContentStructureContent> contents = new TreeSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public ContentStructureType getType() {
        return type;
    }

    public void setType(ContentStructureType type) {
        this.type = type;
    }

    @Override
    public Set<ContentStructureContent> getContents() {
        return contents;
    }

    protected void setContents(Set<ContentStructureContent> contents) {
        this.contents = contents;
    }
}
