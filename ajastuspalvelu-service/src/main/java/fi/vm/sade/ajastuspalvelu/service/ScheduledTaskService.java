package fi.vm.sade.ajastuspalvelu.service;

import java.util.List;

import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskDto;

public interface ScheduledTaskService {
    
    public ScheduledTaskDto insert(ScheduledTaskDto dto);
    
    public void update(ScheduledTaskDto dto);
    
    public void remove(long id);
    
    public List<ScheduledTaskDto> list();
}
