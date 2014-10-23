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

package fi.vm.sade.ajastuspalvelu.service.scheduling.dto;

import org.quartz.JobExecutionContext;

/**
 * User: ratamaa
 * Date: 23.10.2014
 * Time: 13:56
 */
public class ScheduledTaskExecutionDetailsDto {
    private Long taskId;
    private Long scheduledTaskId;
    private String templateName;
    private JobExecutionContext context;

    // ... plus other possible scheduled task related parameters

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getScheduledTaskId() {
        return scheduledTaskId;
    }

    public void setScheduledTaskId(Long scheduledTaskId) {
        this.scheduledTaskId = scheduledTaskId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public JobExecutionContext getContext() {
        return context;
    }

    public void setContext(JobExecutionContext context) {
        this.context = context;
    }
}
