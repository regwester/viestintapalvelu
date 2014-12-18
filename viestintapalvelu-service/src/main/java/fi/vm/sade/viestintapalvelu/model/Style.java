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

import javax.persistence.*;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 9:55
 */
@Entity
@Table(name = "tyyli", schema = "kirjeet",
    uniqueConstraints = @UniqueConstraint(columnNames = {"nimi", "aikaleima"}))
public class Style implements Serializable {
    private static final long serialVersionUID = -2990505170691215066L;

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @GeneratedValue(generator = "tyyli_id_seq")
    @SequenceGenerator(name = "tyyli_id_seq", sequenceName = "tyyli_id_seq")
    private Long id;

    @Column(name="nimi", length = 127, nullable = false, updatable = false)
    private String name;

    @Column(name="tyyli", nullable = false, updatable = false)
    private String style;

    @Column(name="aikaleima", nullable = false, updatable = false)
    private Date timestamp = new Date();

    public Style() {
    }

    public Style(String name, String style) {
        this.name = name;
        this.style = style;
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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
