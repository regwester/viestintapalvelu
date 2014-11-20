package fi.vm.sade.ajastuspalvelu.service;

import java.util.List;

import org.quartz.SchedulerException;

import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskCriteriaDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskListDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskModifyDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskSaveDto;

public interface ScheduledTaskService {
    
    ScheduledTaskListDto insert(ScheduledTaskSaveDto dto) throws SchedulerException;
    
    void update(ScheduledTaskModifyDto dto) throws SchedulerException;
    
    void remove(long id) throws SchedulerException;
    
    List<ScheduledTaskListDto> list(ScheduledTaskCriteriaDto criteria);

    int count(ScheduledTaskCriteriaDto criteria);
    
    ScheduledTaskModifyDto findById(long id);
}
