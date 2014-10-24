package fi.vm.sade.ajastuspalvelu.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto implements Serializable {
    
    public final Long id;
    
    public final String name;
    
    public TaskDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
}
