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

package fi.vm.sade.ajastuspalvelu.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * User: ratamaa
 * Date: 20.11.2014
 * Time: 16:57
 */
public class ScheduledTaskListWrapperDto implements Serializable  {
    private static final long serialVersionUID = -8011781874704718715L;
    private final int count;
    private final List<ScheduledTaskListDto> results;

    public ScheduledTaskListWrapperDto(int count, List<ScheduledTaskListDto> results) {
        this.count = count;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public List<ScheduledTaskListDto> getResults() {
        return results;
    }
}
