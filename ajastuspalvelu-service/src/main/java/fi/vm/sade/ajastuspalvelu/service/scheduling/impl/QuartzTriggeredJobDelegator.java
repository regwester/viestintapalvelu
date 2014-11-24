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

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ajastuspalvelu.service.scheduling.ScheduledTaskExecutorService;
import fi.vm.sade.ajastuspalvelu.service.scheduling.exception.RetryException;

/**
 * User: ratamaa
 * Date: 23.10.2014
 * Time: 12:49
 */
@Component
public class QuartzTriggeredJobDelegator implements Job {
    private static final Logger logger = LoggerFactory.getLogger(QuartzTriggeredJobDelegator.class);

    @Autowired
    private ScheduledTaskExecutorService scheduledTaskExecutorService;

    /**
     * @param context
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Job {} scheduled run start (fired at: {}, last run at {})", context.getJobDetail(),
                context.getFireTime(), context.getNextFireTime());
        try {
            Long id = Long.parseLong(context.getJobDetail().getKey().getName());
            scheduledTaskExecutorService.executeScheduledTask(id, context);
            logger.info("Job {} scheduled ended successfully, next scheduled at {}", context.getJobDetail(),
                    context.getNextFireTime());
        } catch(RetryException e) {
            logger.error("Job "+context.getJobDetail()+" execution failed: "+e.getMessage(), e);
            throw new JobExecutionException(e.getCause(), true);
        } catch(Exception e) {
            logger.error("Job "+context.getJobDetail()+" execution failed: "+e.getMessage(), e);
            throw new JobExecutionException(e); // no retries
        }
    }
}
