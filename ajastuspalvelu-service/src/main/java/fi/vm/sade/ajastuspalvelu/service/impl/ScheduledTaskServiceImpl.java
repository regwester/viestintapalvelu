package fi.vm.sade.ajastuspalvelu.service.impl;

import java.util.ArrayList;
import java.util.List;

import jersey.repackaged.com.google.common.collect.ImmutableList;

import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import fi.vm.sade.ajastuspalvelu.dao.ScheduledTaskDao;
import fi.vm.sade.ajastuspalvelu.model.ScheduledTask;
import fi.vm.sade.ajastuspalvelu.service.ScheduledTaskService;
import fi.vm.sade.ajastuspalvelu.service.converter.ScheduledTaskConverter;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.QuartzSchedulingService;

@Service
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    @Autowired
    private ScheduledTaskDao dao;
    
    @Autowired
    private ScheduledTaskConverter scheduledTaskConverter;
    
    @Autowired
    private QuartzSchedulingService schedulingService;
    
    @Transactional
    @Override
    public ScheduledTaskDto insert(ScheduledTaskDto dto) {
        ScheduledTask task = dao.insert(scheduledTaskConverter.convertToModel(dto));
        return scheduledTaskConverter.convertToDto(task);
    }
    
    @Transactional
    @Override
    public void update(ScheduledTaskDto dto) {
        dao.update(scheduledTaskConverter.convertToModel(dto));
    }
    
    @Transactional
    @Override
    public void remove(long id) throws SchedulerException {
      ScheduledTask task = dao.read(id);
      task.setRemoved(new DateTime());
      dao.update(task);
      schedulingService.unscheduleJob(id);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<ScheduledTaskDto> list() {
        List<ScheduledTask> tasks = dao.findAll();
        List<ScheduledTaskDto> dtos = new ArrayList<ScheduledTaskDto>();
        for (ScheduledTask scheduledTask : tasks) {
            dtos.add(scheduledTaskConverter.convertToDto(scheduledTask));
        }
        return dtos;
    }

    @Override
    public ScheduledTaskDto findById(long id) {
        return scheduledTaskConverter.convertToDto(dao.read(id));
    }

}
