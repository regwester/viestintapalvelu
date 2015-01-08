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

import javax.persistence.*;

import fi.vm.sade.viestintapalvelu.model.types.ContentType;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 10:55
 */
@Entity
@Table(name = "sisalto_korvauskentta", schema = "kirjeet",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"rakenne", "avain"}),
                @UniqueConstraint(columnNames = {"rakenne", "nimi"}),
                @UniqueConstraint(columnNames = {"rakenne", "jarjestys"})
        })
public class ContentReplacement implements Serializable {
    private static final long serialVersionUID = 4522333267335734593L;

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @GeneratedValue(generator = "sisalto_korvauskentta_id_seq")
    @SequenceGenerator(name = "sisalto_korvauskentta_id_seq", sequenceName = "sisalto_korvauskentta_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rakenne", nullable = false, updatable = false)
    private Structure structure;

    // The replacement Velocity variable name and a name by which TemplateReplacments are mapped
    @Column(name="avain", nullable = false, updatable = false, length = 127)
    private String key;

    // The order in which shown in the UI
    @Column(name="jarjestys", nullable = false)
    private int orderNumber;

    // The visible name in the UI
    @Column(name="nimi", nullable = false, updatable = false, length = 255)
    private String name;

    // The description for the UI
    @Column(name="kuvaus", nullable = true, updatable = false)
    private String description;

    // The content type for the replacement: (html for rich text and plain for basic input fields in the UI)
    @Enumerated(EnumType.STRING)
    @Column(name="tyyppi", nullable = false, updatable = false)
    private ContentType contentType = ContentType.plain;

    // The number of rows for the input field in UI (1 should indicate a text-type input field
    // (no more than 1 row can be inserted) and  a higher number indicates a default number
    // of rows in textarea/rich editor)
    @Column(name="riveja", nullable = false, updatable = false)
    private int numberOfRows=1;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }
}
