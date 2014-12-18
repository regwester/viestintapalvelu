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

import fi.vm.sade.viestintapalvelu.model.types.ContentType;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 10:26
 */
@Entity
@Table(name = "rakenne_sisalto", schema = "kirjeet")
public class Content implements Serializable, NamedContent {
    private static final long serialVersionUID = 8808015115199460274L;

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @GeneratedValue(generator = "rakenne_sisalto_id_seq")
    @SequenceGenerator(name = "rakenne_sisalto_id_seq", sequenceName = "rakenne_sisalto_id_seq")
    private Long id;

    @Column(name="nimi", nullable = false, updatable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="tyyppi", nullable = false, updatable = false)
    private ContentType contentType;

    @Column(name="sisalto", nullable = false, updatable = false)
    private String content;

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

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
