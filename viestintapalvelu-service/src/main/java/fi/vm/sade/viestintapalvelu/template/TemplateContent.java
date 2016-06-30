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
package fi.vm.sade.viestintapalvelu.template;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TemplateContent", description = "Kirjeen sisältö")
public class TemplateContent implements Comparable<TemplateContent> {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "Nimi")
    private String name;

    @ApiModelProperty(value = "Järjestys")
    private int order;

    @ApiModelProperty(value = "Sisältö")
    private String content;

    @ApiModelProperty(value = "Aikaleima")
    private Date timestamp;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TemplateContent [order=" + order + ", name=" + name + ", content=" + content + ", timestamp=" + timestamp + ", id=" + id + "]";
    }

    @Override
    public int compareTo(TemplateContent o) {
        Integer ord = order;
        return ord.compareTo(o.order);
    }

}
