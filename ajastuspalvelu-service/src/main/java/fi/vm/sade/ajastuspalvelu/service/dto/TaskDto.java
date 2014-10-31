package fi.vm.sade.ajastuspalvelu.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto implements Serializable {
    private static final long serialVersionUID = 5590623524992599491L;

    public final Long id;
    
    public final String name;
    
    public TaskDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
}
