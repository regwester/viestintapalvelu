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
import fi.vm.sade.ajastuspalvelu.model.ScheduledTask;
import fi.vm.sade.ajastuspalvelu.model.Task;
import fi.vm.sade.ajastuspalvelu.service.scheduling.TaskRunner;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.ErrorDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.ScheduledTaskExecutionDetailsDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.converter.ScheduledTaskDtoConverter;
import fi.vm.sade.ajastuspalvelu.service.scheduling.exception.RetryException;

import static org.junit.Assert.assertTrue;
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
        final AtomicBoolean b = new AtomicBoolean(false);
        setup(1l, new TaskRunner() {
            @Override
            public void run(ScheduledTaskExecutionDetailsDto scheduledTask) throws Exception {
                b.set(true);
            }

            @Override
            public ErrorDto handleError(ScheduledTaskExecutionDetailsDto scheduledTask, ErrorDto error) {
                return error;
            }
        });
        executor.executeScheduledTask(1l, QuartzTriggeredJobDelegatorTest.context(1l));
        assertTrue(b.get());
    }

    @Test(expected = Exception.class)
    public void testFailureRun() throws Exception {
        setup(1l, new TaskRunner() {
            @Override
            public void run(ScheduledTaskExecutionDetailsDto scheduledTask) throws Exception {
                throw new Exception();
            }

            @Override
            public ErrorDto handleError(ScheduledTaskExecutionDetailsDto scheduledTask, ErrorDto error) {
                return error;
            }
        });
        executor.executeScheduledTask(1l, QuartzTriggeredJobDelegatorTest.context(1l));
    }

    @Test(expected = RetryException.class)
    public void testRetryFailureRun() throws Exception {
        setup(1l, new TaskRunner() {
            @Override
            public void run(ScheduledTaskExecutionDetailsDto scheduledTask) throws Exception {
                throw new Exception();
            }

            @Override
            public ErrorDto handleError(ScheduledTaskExecutionDetailsDto scheduledTask, ErrorDto error) {
                return error.withRetry();
            }
        });
        executor.executeScheduledTask(1l, QuartzTriggeredJobDelegatorTest.context(1l));
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
