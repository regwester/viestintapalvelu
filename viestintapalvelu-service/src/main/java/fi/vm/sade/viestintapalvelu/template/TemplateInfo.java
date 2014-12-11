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

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;

/**
 * @author risal1
 *
 */
@ApiModel("Kirjepohjan tiedot listanäkymiä varten")
public class TemplateInfo implements Serializable {

    private static final long serialVersionUID = 1107362510816822354L;

    @ApiModelProperty("Kirjepohjan tunniste")
    public final Long id;

    @ApiModelProperty("Kirjepohjan nimi")
    public final String name;

    @ApiModelProperty("Kirjepohjan kieli")
    public final String language;

    @ApiModelProperty("Kirjepohjan tila")
    public final State state;

    @ApiModelProperty("Tuetut viestityypit")
    public final Set<ContentStructureType> types;

    public final Date timestamp;

    public TemplateInfo(Long id, String name, String language, State state, Date timestamp, Set<ContentStructureType> types) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.state = state;
        this.timestamp = timestamp;
        this.types = types;
    }

}
