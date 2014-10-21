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

import java.util.Date;

import org.springframework.stereotype.Service;

/**
 * User: ratamaa
 * Date: 21.10.2014
 * Time: 13:16
 */
@Service
public class OtherServiceImpl implements OtherService {
    @Override
    public void doSomething(Date previousFireTime, Date nextFireTime, String name) {
        System.out.println("------------------------------------------------");
        System.out.println("Job named: " + name + " was called at " + new Date());
        System.out.println("It was last run at " + previousFireTime + " and next time at: " + nextFireTime);
        System.out.println();
    }
}
