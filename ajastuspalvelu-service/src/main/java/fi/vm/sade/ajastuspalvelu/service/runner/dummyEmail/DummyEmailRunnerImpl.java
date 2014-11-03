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

package fi.vm.sade.ajastuspalvelu.service.runner.dummyEmail;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ajastuspalvelu.api.dto.SchedulerResponse;
import fi.vm.sade.ajastuspalvelu.service.external.api.SchedulerResource;
import fi.vm.sade.ajastuspalvelu.service.external.email.EmailService;
import fi.vm.sade.ajastuspalvelu.service.external.email.dto.EmailDetailsDto;
import fi.vm.sade.ajastuspalvelu.service.runner.dto.converter.EmailDetailsDtoConverter;
import fi.vm.sade.ajastuspalvelu.service.scheduling.TaskRunner;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.ErrorDto;
import fi.vm.sade.ajastuspalvelu.service.scheduling.dto.ScheduledTaskExecutionDetailsDto;

/**
 * User: ratamaa
 * Date: 21.10.2014
 * Time: 13:16
 */
@Component("dummyEmail")
public class DummyEmailRunnerImpl implements TaskRunner {
    private static final Logger logger = LoggerFactory.getLogger(DummyEmailRunnerImpl.class);

    @Resource
    private SchedulerResource dummyEmailResourceClient;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailDetailsDtoConverter emailDetailsDtoConverter;

    @Override
    public void run(ScheduledTaskExecutionDetailsDto scheduledTask) throws Exception {
        SchedulerResponse response = dummyEmailResourceClient.get();
        // Expect all receivers to contain email:
        EmailDetailsDto emailDetailsDto = emailDetailsDtoConverter.convert(scheduledTask, response,
                new EmailDetailsDto());
        emailDetailsDto.getReplacements().put("subject", "Testiviesti");
        emailService.sendEmail(emailDetailsDto);
    }

    @Override
    public ErrorDto handleError(ScheduledTaskExecutionDetailsDto scheduledTask, ErrorDto error) {
        logger.error("Dummy email exception handling: " + error.getMessage());
        return error;
    }
}
