package fi.vm.sade.ajastuspalvelu.service.impl;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.SchedulerException;

import fi.vm.sade.ajastuspalvelu.dao.ScheduledTaskDao;
import fi.vm.sade.ajastuspalvelu.model.ScheduledTask;
import fi.vm.sade.ajastuspalvelu.service.converter.ScheduledTaskConverter;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.QuartzSchedulingService;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledTaskServiceImplTest {

    @Mock
    private ScheduledTaskDao dao;
    
    @Mock
    private ScheduledTaskConverter converter;
    
    @Mock
    private QuartzSchedulingService schedulingService;
    
    @InjectMocks
    private ScheduledTaskServiceImpl service;
    
    @Test
    public void insertsScheduledTask() {
        long id = 1l;
        ScheduledTask task = givenScheduledTask(id);
        when(converter.convertToModel(any(ScheduledTaskDto.class))).thenReturn(task);
        when(dao.insert(any(ScheduledTask.class))).thenReturn(task);
        when(converter.convertToDto(any(ScheduledTask.class))).thenReturn(givenScheduledTaskDto(id));
        assertEquals(task.getId(), service.insert(givenScheduledTaskDto(null)).id);
    }
    
    @Test
    public void updatesScheduledTask() {
        ScheduledTask task = givenScheduledTask(4l);
        when(converter.convertToModel(any(ScheduledTaskDto.class))).thenReturn(task);
        service.update(givenScheduledTaskDto(4l));
        verify(dao).update(task);
    }
    
    @Test
    public void removesScheduledTask() throws SchedulerException {
        long id = 3l;
        ScheduledTask task = givenScheduledTask(id);
        when(dao.read(id)).thenReturn(task);
        service.remove(id);
        verify(dao).update(task);
        verify(schedulingService).unscheduleJob(id);
        assertNotNull(task.getRemoved());
        
    }
    
    private ScheduledTask givenScheduledTask(long id) {
        ScheduledTask task = new ScheduledTask();
        task.setId(id);
        return task;
    }
    
    private ScheduledTaskDto givenScheduledTaskDto(Long id) {
        return new ScheduledTaskDto(id, "test_task", "1.9.2.3.4", new DateTime());
    }
}
