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
package fi.vm.sade.viestintapalvelu.dao.dto;

import java.io.Serializable;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 14:12
 */
@ApiModel("Rakenteen tiedot")
public class StructureListDto implements Serializable {
    private static final long serialVersionUID = 4439159936778931284L;

    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("Kuvaus (käyttöliittymässä näytettävä)")
    private String description;
    @ApiModelProperty("Nimi, yksilöivä")
    private String name;
    @ApiModelProperty("Kielikoodi")
    private String language;
    @ApiModelProperty("Luontiaika")
    private Date timestamp;

    public StructureListDto() {
    }

    public StructureListDto(Long id, String description, String name, String language, Date timestamp) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.language = language;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
