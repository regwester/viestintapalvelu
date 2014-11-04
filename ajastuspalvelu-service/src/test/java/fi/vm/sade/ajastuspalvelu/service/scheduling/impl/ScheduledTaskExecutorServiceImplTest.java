/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.ajastuspalvelu.service.scheduling.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import fi.vm.sade.ajastuspalvelu.dao.ScheduledTaskDao;
import fi.vm.sade.ajastuspalvelu.model.ScheduledRun;
import fi.vm.sade.ajastuspalvelu.model.ScheduledTask;
import fi.vm.sade.ajastuspalvelu.model.Task;
import fi.vm.sade.ajastuspalvelu.service.scheduling.TaskRunner;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.ErrorDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.ScheduledTaskExecutionDetailsDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.TaskResultDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.converter.ScheduledTaskDtoConverter;
import fi.vm.sade.ajastuspalvelu.service.scheduling.exception.RetryException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * User: ratamaa
 * Date: 3.11.2014
 * Time: 16:22
 */
@RunWith(MockitoJUnitRunner.class)
public class ScheduledTaskExecutorServiceImplTest {

    @Mock
    private ScheduledTaskDao scheduledTaskDao;

    @Mock
    private ScheduledTaskDtoConverter scheduledTaskDtoConverter;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private ScheduledTaskExecutorServiceImpl executor;

    @Test
    public void testSuccessRun() throws Exception {
        ScheduledTask task = setup(1l, new TaskRunner() {
            @Override
            public TaskResultDto run(ScheduledTaskExecutionDetailsDto scheduledTask) throws Exception {
                return new TaskResultDto("abc");
            }

            @Override
            public ErrorDto handleError(ScheduledTaskExecutionDetailsDto scheduledTask, ErrorDto error) {
                fail("Not here");
                return error;
            }
        });
        executor.executeScheduledTask(1l, QuartzTriggeredJobDelegatorTest.context(1l));
        assertEquals(1, task.getRuns().size());
        ScheduledRun run = task.getRuns().iterator().next();
        assertEquals(ScheduledRun.State.FINISHED, run.getState());
        assertEquals("abc", run.getExternalId());
        assertNotNull(run.getFinished());
    }

    @Test
    public void testFailureRun() throws Exception {
        ScheduledTask task = setup(1l, new TaskRunner() {
            @Override
            public TaskResultDto run(ScheduledTaskExecutionDetailsDto scheduledTask) throws Exception {
                throw new Exception("MSG");
            }

            @Override
            public ErrorDto handleError(ScheduledTaskExecutionDetailsDto scheduledTask, ErrorDto error) {
                return error;
            }
        });
        try {
            executor.executeScheduledTask(1l, QuartzTriggeredJobDelegatorTest.context(1l));
            fail("Should have thrown");
        } catch (Exception e) {
            assertEquals(1, task.getRuns().size());
            ScheduledRun run = task.getRuns().iterator().next();
            assertEquals(ScheduledRun.State.ERROR, run.getState());
            assertEquals("MSG", run.getErrorMessage());
            assertEquals("MSG", e.getMessage());
            assertNull(run.getFinished());
        }
    }

    @Test
    public void testRetryFailureRun() throws Exception {
        ScheduledTask task = setup(1l, new TaskRunner() {
            @Override
            public TaskResultDto run(ScheduledTaskExecutionDetailsDto scheduledTask) throws Exception {
                throw new Exception("msg");
            }

            @Override
            public ErrorDto handleError(ScheduledTaskExecutionDetailsDto scheduledTask, ErrorDto error) {
                return error.withRetry().withMessage("Cleaned msg");
            }
        });
        try {
            executor.executeScheduledTask(1l, QuartzTriggeredJobDelegatorTest.context(1l));
            fail("Should have thrown");
        } catch(RetryException e) {
            assertEquals(1, task.getRuns().size());
            ScheduledRun run = task.getRuns().iterator().next();
            assertEquals(ScheduledRun.State.ERROR, run.getState());
            assertEquals("Cleaned msg", run.getErrorMessage());
            assertEquals("java.lang.Exception: msg", e.getMessage());
            assertNull(run.getFinished());
        }
    }

    private ScheduledTask setup(long id, TaskRunner runner) {
        ScheduledTask task = scheduledTask(id, "testBean");
        when(applicationContext.getBean(eq("testBean"))).thenReturn(runner);
        when(scheduledTaskDao.read(eq(1l))).thenReturn(task);
        return task;
    }

    private ScheduledTask scheduledTask(long id, String bean) {
        ScheduledTask stask = new ScheduledTask();
        stask.setId(id);
        Task task = new Task();
        task.setBeanName(bean);
        stask.setTask(task);
        return stask;
    }
}
