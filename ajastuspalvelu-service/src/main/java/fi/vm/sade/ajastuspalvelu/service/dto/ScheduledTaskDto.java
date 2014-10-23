package fi.vm.sade.ajastuspalvelu.service.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduledTaskDto implements Serializable {
    
    public final Long id;
    
    public final String taskName;
    
    public final String hakuOid;
    
    public final DateTime runtimeForSingle;

    public ScheduledTaskDto(Long id, String taskName, String hakuOid, DateTime runtimeForSingle) {
        this.id = id;
        this.taskName = taskName;
        this.hakuOid = hakuOid;
        this.runtimeForSingle = runtimeForSingle;
    }
    
}
