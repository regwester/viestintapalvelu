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
import org.joda.time.LocalDateTime;
import org.quartz.CronExpression;

import com.google.common.base.Optional;

import fi.vm.sade.ajastuspalvelu.service.scheduling.Schedule;

import static org.joda.time.DateTime.now;

/**
 * User: ratamaa
 * Date: 23.10.2014
 * Time: 13:01
 */
public class SingleScheduledTaskTaskSchedule implements Schedule {
    private final DateTime runtimeForSingle;
    private final String cron;

    public SingleScheduledTaskTaskSchedule(DateTime runtimeForSingle) {
        this.runtimeForSingle = runtimeForSingle;
        this.cron = toCron(runtimeForSingle);
    }

    private String toCron(DateTime dt) {
        LocalDateTime ldt = dt.toLocalDateTime();
        try {
            return new CronExpression(ldt.getSecondOfMinute() + " "
                    + ldt.getMinuteOfHour() + " "
                    + ldt.getHourOfDay() + " "
                    + ldt.getDayOfMonth() + " "
                    + ldt.getMonthOfYear() + " ? " // <- any day of week
                    + ldt.getYear()).getCronExpression();
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean isValid() {
        return runtimeForSingle.isAfter(now());
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

    @Override
    public String toString() {
        return "SingleScheduledTaskTaskSchedule("+this.runtimeForSingle+", cron="+this.cron+")";
    }
}
