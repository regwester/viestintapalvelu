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

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.google.common.base.Optional;

import fi.vm.sade.ajastuspalvelu.service.scheduling.Schedule;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * User: ratamaa
 * Date: 3.11.2014
 * Time: 15:40
 */
@RunWith(MockitoJUnitRunner.class)
public class QuartzSchedulingServiceImplTest {

    @Mock
    private Scheduler scheduler;

    @InjectMocks
    private QuartzSchedulingServiceImpl quartzSchedulingService;

    @Test
    public void testScheduleJobInvalidSchedule() throws SchedulerException {
        when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(null);
        quartzSchedulingService.scheduleJob(1l, new InvalidSchedule());
        verify(scheduler).addJob(any(JobDetail.class), eq(false));
        verify(scheduler, atMost(0)).scheduleJob(any(Trigger.class));
    }

    @Test
    public void testScheduleJobWithValidSchedule() throws SchedulerException {
        when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(null);
        quartzSchedulingService.scheduleJob(1l, new SingleScheduledTaskTaskSchedule(new DateTime().plusHours(1)));
        verify(scheduler).addJob(any(JobDetail.class), eq(false));
        verify(scheduler).scheduleJob(any(Trigger.class));
    }

    @Test
    public void testRescheduleJob() throws SchedulerException {
        when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(new JobDetailImpl());
        //noinspection unchecked
        when(scheduler.getTriggersOfJob(any(JobKey.class))).thenReturn((List) asList(new CronTriggerImpl()));
        quartzSchedulingService.scheduleJob(1l, new SingleScheduledTaskTaskSchedule(new DateTime().plusHours(1)));
        verify(scheduler).unscheduleJob(any(TriggerKey.class));
        verify(scheduler).scheduleJob(any(Trigger.class));
    }

    @Test
    public void testUnscheduleJob() throws SchedulerException {
        when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(new JobDetailImpl());
        //noinspection unchecked
        when(scheduler.getTriggersOfJob(any(JobKey.class))).thenReturn((List) asList(new CronTriggerImpl()));
        quartzSchedulingService.unscheduleJob(1l);
        verify(scheduler).unscheduleJob(any(TriggerKey.class));
    }

    @Test
    public void testUnscheduleNonFoundJob() throws SchedulerException {
        when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(null);
        quartzSchedulingService.unscheduleJob(1l);
        verify(scheduler, atMost(0)).unscheduleJob(any(TriggerKey.class));
    }

    protected static class InvalidSchedule implements Schedule {
        @Override
        public boolean isValid() {
            return false;
        }
        @Override
        public String getCron() {
            return null;
        }
        @Override
        public Optional<DateTime> getActiveBegin() {
            return Optional.absent();
        }
        @Override
        public Optional<DateTime> getActiveEnd() {
            return Optional.absent();
        }
    }
}
