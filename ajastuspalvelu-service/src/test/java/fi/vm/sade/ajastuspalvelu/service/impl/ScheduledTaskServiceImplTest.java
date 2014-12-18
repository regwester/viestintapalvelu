package fi.vm.sade.ajastuspalvelu.service.impl;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.SchedulerException;

import fi.vm.sade.ajastuspalvelu.dao.ScheduledTaskCriteria;
import fi.vm.sade.ajastuspalvelu.dao.ScheduledTaskDao;
import fi.vm.sade.ajastuspalvelu.model.ScheduledTask;
import fi.vm.sade.ajastuspalvelu.service.converter.ScheduledTaskConverter;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskCriteriaDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskListDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskModifyDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskSaveDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.QuartzSchedulingService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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
    public void insertsScheduledTask() throws SchedulerException {
        long id = 1l;
        ScheduledTask task = givenScheduledTask(id);
        when(converter.convert(any(ScheduledTaskSaveDto.class), any(ScheduledTask.class))).thenReturn(task);
        when(dao.insert(any(ScheduledTask.class))).thenReturn(task);
        when(converter.convert(any(ScheduledTask.class))).thenReturn(givenScheduledTaskDto(id));
        assertEquals(task.getId(), service.insert(random(new ScheduledTaskSaveDto())).getId());
    }
    
    @Test
    public void updatesScheduledTask() throws SchedulerException {
        ScheduledTask task = givenScheduledTask(4l);
        when(dao.read(eq(4l))).thenReturn(task);
        when(converter.convert(any(ScheduledTaskSaveDto.class), any(ScheduledTask.class))).thenReturn(task);
        service.update(randomModifyDto(4l));
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
    
    @Test
    public void returnsListOfScheduledTasks() {
        when(dao.find(any(ScheduledTaskCriteria.class))).thenReturn(Arrays.asList(givenScheduledTask(1l), givenScheduledTask(2l), givenScheduledTask(3l)));
        when(converter.convert(any(ScheduledTask.class))).thenReturn(givenScheduledTaskDto(1l));
        List<ScheduledTaskListDto> tasks = service.list(new ScheduledTaskCriteriaDto());
        assertEquals(3, tasks.size());
    }
    
    @Test
    public void returnsScheduledTask() {
        Long id = 7l;
        when(dao.read(id)).thenReturn(givenScheduledTask(id));
        when(converter.convert(any(ScheduledTask.class), any(ScheduledTaskModifyDto.class))).thenReturn(randomModifyDto(id));
        ScheduledTaskModifyDto task = service.findById(id);
        assertEquals(id, task.getId());
    }
    
    private ScheduledTask givenScheduledTask(long id) {
        ScheduledTask task = new ScheduledTask();
        task.setRuntimeForSingle(new DateTime());
        task.setId(id);
        return task;
    }

    private ScheduledTaskModifyDto randomModifyDto(Long id) {
        ScheduledTaskModifyDto dto = new ScheduledTaskModifyDto();
        dto.setId(id);
        random(dto);
        return dto;
    }

    private ScheduledTaskSaveDto random(ScheduledTaskSaveDto dto) {
        dto.setTaskId(123l);
        dto.setRuntimeForSingle(new DateTime());
        dto.setHakuOid("1.9.2.3.4");
        return dto;
    }

    private ScheduledTaskListDto givenScheduledTaskDto(Long id) {
        return new ScheduledTaskListDto(id, "test_task", "1.9.2.3.4", new DateTime(), null);
    }
}
