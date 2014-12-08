package fi.vm.sade.ajastuspalvelu.service;

import java.util.List;

import fi.vm.sade.ajastuspalvelu.service.dto.TaskListDto;

public interface TaskService {
    
    public List<TaskListDto> list();
}
