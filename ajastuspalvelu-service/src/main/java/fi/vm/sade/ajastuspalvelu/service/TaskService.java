package fi.vm.sade.ajastuspalvelu.service;

import java.util.List;

import fi.vm.sade.ajastuspalvelu.service.dto.TaskDto;

public interface TaskService {
    
    public List<TaskDto> list();
}
