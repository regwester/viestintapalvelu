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

import java.io.Serializable;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * User: ratamaa
 * Date: 3.11.2014
 * Time: 9:48
 */
@ApiModel("Ajastetun tehtävän lisäyksen tiedot")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduledTaskSaveDto implements Serializable {
    private static final long serialVersionUID = -6956375461878229360L;

    @NotNull
    @ApiModelProperty(value = "Tehtävän ID", required = true)
    private Long taskId;
    @ApiModelProperty(value ="Haun OID", required = false)
    private String hakuOid;
    @ApiModelProperty(value ="Tehtävän suoritusaika. Oletava tulevaisuudessa.", required = true)
    @NotNull @Future
    private DateTime runtimeForSingle;

    public ScheduledTaskSaveDto() {
    }

    public ScheduledTaskSaveDto(Long taskId, String hakuOid, DateTime runtimeForSingle) {
        this.taskId = taskId;
        this.hakuOid = hakuOid;
        this.runtimeForSingle = runtimeForSingle;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getHakuOid() {
        return hakuOid;
    }

    public void setHakuOid(String hakuOid) {
        this.hakuOid = hakuOid;
    }

    public DateTime getRuntimeForSingle() {
        return runtimeForSingle;
    }

    public void setRuntimeForSingle(DateTime runtimeForSingle) {
        this.runtimeForSingle = runtimeForSingle;
    }
}
