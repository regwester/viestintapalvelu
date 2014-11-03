package fi.vm.sade.ajastuspalvelu.service;

import java.util.List;

import org.quartz.SchedulerException;

import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskListDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskModifyDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskSaveDto;

public interface ScheduledTaskService {
    
    public ScheduledTaskListDto insert(ScheduledTaskSaveDto dto) throws SchedulerException;
    
    public void update(ScheduledTaskModifyDto dto) throws SchedulerException;
    
    public void remove(long id) throws SchedulerException;
    
    public List<ScheduledTaskListDto> list();
    
    public ScheduledTaskModifyDto findById(long id);
}
