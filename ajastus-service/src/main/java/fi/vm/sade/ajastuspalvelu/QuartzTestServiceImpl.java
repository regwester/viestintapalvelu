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
package fi.vm.sade.ajastuspalvelu;

import javax.annotation.PostConstruct;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * User: ratamaa
 * Date: 20.10.2014
 * Time: 17:10
 */
@Service
public class QuartzTestServiceImpl implements Job {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private OtherService otherService;

    @PostConstruct
    public void test() throws SchedulerException {
        System.out.println("INIT");

        cronJob(123456l, "0 * * * * ?");

        scheduler.start();
    }

    public void cronJob(Long id, String cron) throws SchedulerException {
        JobKey key = new JobKey(""+id, "group");
        JobDetail job = scheduler.getJobDetail(key);
        if (job == null) {
            job = newJob(QuartzTestServiceImpl.class)
                    .withIdentity(key)
                    .requestRecovery().storeDurably()
                    .build();
            scheduler.addJob(job, false);
        } else {
            // Remove all triggers from given job:
            for (Trigger trigger : scheduler.getTriggersOfJob(key)) {
                System.out.println("Removed trigger: " + trigger.getKey());
                scheduler.unscheduleJob(trigger.getKey());
            }
        }
        // And schedule with a new trigger:
        Trigger trigger = newTrigger()
                .withIdentity("trigger."+key.getName(), "group")
                .forJob(key)
                // .startAt() // the start moment
                // .endAt() // the end moment
                .withSchedule(cronSchedule(cron))
                .build();
        scheduler.scheduleJob(trigger);
        System.out.println("Scheduled " + trigger);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        otherService.doSomething(jobExecutionContext.getPreviousFireTime(),
                                jobExecutionContext.getNextFireTime(),
                                jobExecutionContext.getJobDetail().getKey().getName());
    }
}
