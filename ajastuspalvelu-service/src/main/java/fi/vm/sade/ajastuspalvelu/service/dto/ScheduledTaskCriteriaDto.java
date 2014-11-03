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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fi.vm.sade.ajastuspalvelu.dao.ScheduledTaskCriteria;

/**
 * User: ratamaa
 * Date: 3.11.2014
 * Time: 16:55
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduledTaskCriteriaDto implements ScheduledTaskCriteria {
    private OrderBy orderBy = OrderBy.CREATED_AT;
    private OrderDirection orderDirection = OrderDirection.DESC;

    public ScheduledTaskCriteriaDto() {
    }

    public ScheduledTaskCriteriaDto(OrderBy orderBy, OrderDirection orderDirection) {
        this.orderBy = orderBy;
        this.orderDirection = orderDirection;
    }

    @Override
    public OrderBy getOrderBy() {
        return orderBy;
    }

    @Override
    public OrderDirection getOrderDirection() {
        return orderDirection;
    }

    protected ScheduledTaskCriteriaDto copy() {
        ScheduledTaskCriteriaDto copy = new ScheduledTaskCriteriaDto();
        copy.orderBy = this.orderBy;
        copy.orderDirection = this.orderDirection;
        return copy;
    }

    @Override
    public ScheduledTaskCriteria withOrderBy(OrderBy orderBy) {
        ScheduledTaskCriteriaDto copy = copy();
        copy.orderBy = orderBy;
        return copy;
    }

    @Override
    public ScheduledTaskCriteria withOrderDirection(OrderDirection orderDirection) {
        ScheduledTaskCriteriaDto copy = copy();
        copy.orderDirection = orderDirection;
        return copy;
    }
}
