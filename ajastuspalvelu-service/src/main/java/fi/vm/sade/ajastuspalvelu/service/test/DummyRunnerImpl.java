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

package fi.vm.sade.ajastuspalvelu.service.test;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.vm.sade.ajastuspalvelu.service.scheduling.TaskRunner;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.ErrorDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.ScheduledTaskExecutionDetailsDto;

/**
 * User: ratamaa
 * Date: 21.10.2014
 * Time: 13:16
 */
@Component("dummyRunner")
public class DummyRunnerImpl implements TaskRunner {
    private static final Logger logger = LoggerFactory.getLogger(DummyRunnerImpl.class);

    @Override
    public void run(ScheduledTaskExecutionDetailsDto scheduledTask) throws Exception {
        logger.info("------------------------------------------------");
        logger.info("Job id: " + scheduledTask.getTaskId() + " was called at " + new Date());
        logger.info("It was last run at " + scheduledTask.getContext().getPreviousFireTime()
                + " and next time at: " + scheduledTask.getContext().getNextFireTime());
        logger.info("");
    }

    @Override
    public ErrorDto handleError(ScheduledTaskExecutionDetailsDto scheduledTask, ErrorDto error) {
        logger.error("Dummy runner exception handling: " + error.getMessage());
        return error;
    }
}
