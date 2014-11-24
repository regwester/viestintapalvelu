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

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;

import fi.vm.sade.ajastuspalvelu.service.scheduling.ScheduledTaskExecutorService;
import fi.vm.sade.ajastuspalvelu.service.scheduling.exception.RetryException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * User: ratamaa
 * Date: 3.11.2014
 * Time: 16:05
 */
@RunWith(MockitoJUnitRunner.class)
public class QuartzTriggeredJobDelegatorTest {

    @Mock
    private ScheduledTaskExecutorService executor;

    @InjectMocks
    private QuartzTriggeredJobDelegator delegator;

    @Test
    public void testOkRun() throws Exception {
        delegator.execute(context(1l));
        verify(executor).executeScheduledTask(eq(1l), any(JobExecutionContext.class));
    }

    @Test
    public void testWrapRetryException() throws Exception {
        doThrow(new RetryException(new IllegalStateException())).when(executor).executeScheduledTask(eq(1l), any(JobExecutionContext.class));
        try {
            delegator.execute(context(1l));
            fail("Should have rethrown");
        } catch(JobExecutionException e) {
           assertTrue(e.refireImmediately());
        }
    }

    @Test
    public void testNonretryException() throws Exception {
        doThrow(new IllegalStateException()).when(executor).executeScheduledTask(eq(1l), any(JobExecutionContext.class));
        try {
            delegator.execute(context(1l));
            fail("Should have rethrown");
        } catch(JobExecutionException e) {
            assertFalse(e.refireImmediately());
        }
    }

    public static JobExecutionContext context(Long id) {
        final JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setName(""+id);
        return new JobExecutionContext() {
            @Override
            public Scheduler getScheduler() {
                return null;
            }

            @Override
            public Trigger getTrigger() {
                return null;
            }

            @Override
            public Calendar getCalendar() {
                return null;
            }

            @Override
            public boolean isRecovering() {
                return false;
            }

            @Override
            public TriggerKey getRecoveringTriggerKey() throws IllegalStateException {
                return null;
            }

            @Override
            public int getRefireCount() {
                return 0;
            }

            @Override
            public JobDataMap getMergedJobDataMap() {
                return null;
            }

            @Override
            public JobDetail getJobDetail() {
                return jobDetail;
            }

            @Override
            public Job getJobInstance() {
                return null;
            }

            @Override
            public Date getFireTime() {
                return null;
            }

            @Override
            public Date getScheduledFireTime() {
                return null;
            }

            @Override
            public Date getPreviousFireTime() {
                return null;
            }

            @Override
            public Date getNextFireTime() {
                return null;
            }

            @Override
            public String getFireInstanceId() {
                return null;
            }

            @Override
            public Object getResult() {
                return null;
            }

            @Override
            public void setResult(Object result) {
            }

            @Override
            public long getJobRunTime() {
                return 0;
            }

            @Override
            public void put(Object key, Object value) {
            }

            @Override
            public Object get(Object key) {
                return null;
            }
        };
    }
}
