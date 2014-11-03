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

import java.text.ParseException;

import org.joda.time.DateTime;
import org.quartz.CronExpression;

import com.google.common.base.Optional;

import fi.vm.sade.ajastuspalvelu.service.scheduling.Schedule;

/**
 * User: ratamaa
 * Date: 23.10.2014
 * Time: 13:01
 */
public class SingleScheduledTaskTaskSchedule implements Schedule {
    private DateTime runtimeForSingle;
    private String cron;

    public SingleScheduledTaskTaskSchedule(DateTime runtimeForSingle) {
        this.runtimeForSingle = runtimeForSingle;
        this.cron = toCron(runtimeForSingle);
    }

    private String toCron(DateTime dt) {
        try {
            return new CronExpression(dt.getSecondOfMinute() + " "
                    + dt.getMinuteOfHour() + " "
                    + dt.getHourOfDay() + " "
                    + dt.getDayOfMonth() + " "
                    + dt.getMonthOfYear() + " ? " // <- any day of week
                    + dt.getYear()).getCronExpression();
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    public DateTime getRuntimeForSingle() {
        return runtimeForSingle;
    }

    @Override
    public String getCron() {
        return this.cron;
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
