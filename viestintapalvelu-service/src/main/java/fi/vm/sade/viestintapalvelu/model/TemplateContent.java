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

import javax.persistence.*;

import fi.vm.sade.generic.model.BaseEntity;

@Deprecated
@Entity
@Table(name = "sisalto", schema = "kirjeet")
public class TemplateContent extends BaseEntity implements Comparable<TemplateContent> {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kirjepohja_id")
    private Template template;

    @Column(name = "jarjestys")
    private int order;

    @Column(name = "nimi")
    private String name;

    @Column(name = "sisalto")
    private String content;

    @Column(name = "tyyppi")
    private String contentType;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "TemplateContent [template=" + template + ", order=" + order + ", name=" + name + ", content=" + content + ", contentType=" + contentType
                + ", timestamp=" + timestamp + "]";
    }

    @Override
    public int compareTo(TemplateContent o) {
        Integer ord = order;
        return ord.compareTo(o.order);
    }
}
