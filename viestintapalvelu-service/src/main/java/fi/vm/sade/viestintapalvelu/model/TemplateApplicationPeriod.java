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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wordnik.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 9.9.2014
 * Time: 10:32
 *
 * CREATE TABLE kirjeet.kirjepohja_haku (
 *  kirjepohja bigint references kirjeet.kirjepohja(id),
 *  haku_oid varchar(255),
 *  primary key(kirjepohja, haku_oid)
 * );
 */
@ApiModel(value = "Kirjetemplatehakuliitos")
@Table(name = "kirjepohja_haku", schema= "kirjeet")
@Entity(name = "TemplateApplicationPeriod")
public class TemplateApplicationPeriod implements Serializable {
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name= "applicationPeriod", column = @Column(name="haku_oid", nullable = false, updatable = false)),
        @AttributeOverride(name="templateId", column = @Column(name="kirjepohja", nullable = false, updatable = false)),
    })
    private TemplateApplicationPeriodId id;

    @MapsId("templateId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="kirjepohja", nullable = false, updatable = false)
    @JsonBackReference
    private Template template;

    public TemplateApplicationPeriod() {
    }

    public TemplateApplicationPeriod(Template template, String applicationPeriod) {
        this.id = new TemplateApplicationPeriodId(template.getId(), applicationPeriod);
        this.template = template;
    }

    public TemplateApplicationPeriodId getId() {
        return id;
    }

    public void setId(TemplateApplicationPeriodId id) {
        this.id = id;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }
}
