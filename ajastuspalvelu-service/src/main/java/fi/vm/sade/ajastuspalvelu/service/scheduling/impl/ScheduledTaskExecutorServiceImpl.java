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

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import fi.vm.sade.ajastuspalvelu.dao.ScheduledTaskDao;
import fi.vm.sade.ajastuspalvelu.model.ScheduledRun;
import fi.vm.sade.ajastuspalvelu.model.ScheduledTask;
import fi.vm.sade.ajastuspalvelu.model.Task;
import fi.vm.sade.ajastuspalvelu.service.scheduling.ScheduledTaskExecutorService;
import fi.vm.sade.ajastuspalvelu.service.scheduling.TaskRunner;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.ErrorDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.ScheduledTaskExecutionDetailsDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.converter.ScheduledTaskDtoConverter;
import fi.vm.sade.ajastuspalvelu.service.scheduling.exception.RetryException;
import fi.vm.sade.viestintapalvelu.common.util.OptionalHelper;

/**
 * User: ratamaa
 * Date: 23.10.2014
 * Time: 13:32
 */
@Service
public class ScheduledTaskExecutorServiceImpl implements ScheduledTaskExecutorService {
    @Autowired
    private ScheduledTaskDao scheduledTaskDao;

    @Autowired
    private ScheduledTaskDtoConverter scheduledTaskDtoConverter;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    @Transactional(noRollbackFor = {RetryException.class, Exception.class,
                    IllegalStateException.class})
    public void executeScheduledTask(Long scheduledTaskId, JobExecutionContext context) throws RetryException, Exception {
        ScheduledTask scheduledTask = Optional.fromNullable(scheduledTaskDao.read(scheduledTaskId))
                .or(OptionalHelper.<ScheduledTask>illegalState("ScheduledTask not found by id=" + scheduledTaskId));
        ScheduledRun run = new ScheduledRun();
        run.setScheduledTask(scheduledTask);
        scheduledTask.getRuns().add(run);

        Task task = scheduledTask.getTask();
        TaskRunner runner = resolveRunner(scheduledTaskId, run, task);
        ScheduledTaskExecutionDetailsDto executionDetails
                = scheduledTaskDtoConverter.convert(scheduledTask, new ScheduledTaskExecutionDetailsDto(),context);

        try {
            runner.run(executionDetails);
        } catch(Exception e) {
            run.setState(ScheduledRun.State.ERROR);
            ErrorDto error = runner.handleError(executionDetails, new ErrorDto(e.getMessage(), e));
            if (error != null) {
                run.setErrorMessage(Optional.fromNullable(error.getMessage()).or(
                        e.getMessage() != null ? e.getMessage() : "Unknown error: " + e.getClass().getCanonicalName()));
                if (error.getCause() != null) {
                    if (error.getMessage() == null && error.getCause().getMessage() != null) {
                        run.setErrorMessage(error.getCause().getMessage());
                    }
                    if (error.isRetry() && !(error.getCause() instanceof RetryException)) {
                        throw new RetryException(error.getCause());
                    }
                    throw error.getCause();
                }
                if (error.isRetry()) {
                    throw new RetryException(e);
                }
            }
            throw e;
        }
        run.setFinished(new DateTime());
        run.setState(ScheduledRun.State.FINISHED);
    }

    private TaskRunner resolveRunner(Long scheduledTaskId, ScheduledRun run, Task task) {
        TaskRunner runner;
        try {
            Object taskRunnerObj = applicationContext.getBean(task.getBeanName());
            if (!(taskRunnerObj instanceof TaskRunner)) {
                run.setState(ScheduledRun.State.ERROR);
                String message = "Bean " + task.getBeanName() + " is not instanceof "
                        + TaskRunner.class.getCanonicalName() + " but a "
                        + taskRunnerObj.getClass().getCanonicalName()
                        + " when executing scheduledTask="+scheduledTaskId
                        + ". Possibly misconfigured task="+task.getId();
                run.setErrorMessage(message);
                throw new IllegalStateException(message);
            }
            runner = (TaskRunner) taskRunnerObj;
        } catch(NoSuchBeanDefinitionException e) {
            run.setState(ScheduledRun.State.ERROR);
            run.setErrorMessage(e.getMessage());
            throw new IllegalStateException("Bean " + task.getBeanName()
                    + " not found when executing scheduledTask="+scheduledTaskId
                    + ". Possibly misconfigured task="+task.getId()+ ". Message: " +e.getMessage(), e);
        } catch(BeansException beansException) {
            run.setState(ScheduledRun.State.ERROR);
            run.setErrorMessage(beansException.getMessage());
            throw beansException;
        }
        return runner;
    }

}
