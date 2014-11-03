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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fi.vm.sade.ajastuspalvelu.service.scheduling.Schedule;

import static org.junit.Assert.*;

/**
 * User: ratamaa
 * Date: 3.11.2014
 * Time: 16:15
 */
@RunWith(JUnit4.class)
public class SingleScheduledTaskTaskScheduleTest {

    @Test
    public void testValid() {
        Schedule schedule = new SingleScheduledTaskTaskSchedule(new DateTime().plusHours(1));
        assertTrue(schedule.isValid());
        assertNotNull(schedule.getCron());
        assertFalse(schedule.getActiveBegin().isPresent());
        assertFalse(schedule.getActiveEnd().isPresent());
    }

    @Test
    public void testInvalid() {
        Schedule schedule = new SingleScheduledTaskTaskSchedule(new DateTime().minusMinutes(1));
        assertFalse(schedule.isValid());
        assertNotNull(schedule.getCron());
    }

}
