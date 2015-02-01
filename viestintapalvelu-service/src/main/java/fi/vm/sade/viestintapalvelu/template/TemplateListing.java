/**
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
package fi.vm.sade.viestintapalvelu.template;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel("Kirjepohjan tiedot kirjepohjan valintaa varten")
public class TemplateListing implements Serializable {

    private static final long serialVersionUID = 1107362510816822354L;

    @ApiModelProperty("Kirjepohjan tunniste")
    public final Long id;

    @ApiModelProperty("Kirjepohjan oid")
    public final String oid;

    @ApiModelProperty("Kirjepohjan tyyppi")
    public final String type;

    @ApiModelProperty("Kirjepohjan kieli")
    public final String language;

    @ApiModelProperty("Haun nimi")
    public final String name;

    @ApiModelProperty(value = "Kirjepohjan tallennusaika")
    private Date timestamp;

    
    public TemplateListing(Long id, String oid, String type, String language, String name, Date timestamp) {
        this.id = id;
        this.oid = oid;
        this.type = type;
        this.name = name;
        this.language = language;
        this.timestamp = timestamp;
    }

}