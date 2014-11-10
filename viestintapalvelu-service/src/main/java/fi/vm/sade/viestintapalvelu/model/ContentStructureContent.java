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

import javax.persistence.*;

import fi.vm.sade.viestintapalvelu.model.types.ContentRole;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 10:27
 */
@Entity
@Table(name = "sisalto_rakenne_sisalto", schema = "kirjeet",
    uniqueConstraints = @UniqueConstraint(columnNames = {"sisalto_rakenne", "jarjestys"}))
public class ContentStructureContent implements Serializable {
    private static final long serialVersionUID = 2783410302027964815L;

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name="contentStructureId",
                column = @Column(name="sisalto_rakenne", nullable = false, updatable = false)),
        @AttributeOverride(name="contentId",
                column = @Column(name="sisalto", nullable = false, updatable = false)),
    })
    private ContentStructureContentId id;

    @Enumerated(EnumType.STRING)
    @Column(name="rooli", nullable = false, updatable = false)
    private ContentRole role;

    @Column(name="jarjestys", nullable = false, updatable = false)
    private int orderNumber;

    @MapsId("contentStructureId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sisalto_rakenne", nullable = false, updatable = false)
    private ContentStructure contentStructure;

    @MapsId("contentId")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="sisalto", nullable = false, updatable = false)
    private Content content;

    public ContentStructureContentId getId() {
        return id;
    }

    public void setId(ContentStructureContentId id) {
        this.id = id;
    }

    public ContentStructure getContentStructure() {
        return contentStructure;
    }

    public void setContentStructure(ContentStructure contentStructure) {
        this.contentStructure = contentStructure;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public ContentRole getRole() {
        return role;
    }

    public void setRole(ContentRole role) {
        this.role = role;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
