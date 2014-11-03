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

package fi.vm.sade.ajastuspalvelu.service.dto;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * User: ratamaa
 * Date: 3.11.2014
 * Time: 9:44
 */
@ApiModel("Ajastetun tehtävän lisäyksen tiedot")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduledTaskModifyDto extends ScheduledTaskSaveDto {
    private static final long serialVersionUID = 8940004793789228189L;

    @NotNull
    @ApiModelProperty(value = "Muokattavan ajastetun tehtävän ID", required = true)
    private Long id;

    public ScheduledTaskModifyDto() {
    }

    public ScheduledTaskModifyDto(Long id, Long taskId, String hakuOid, DateTime runtimeForSingle) {
        super(taskId, hakuOid, runtimeForSingle);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
