package fi.vm.sade.ajastuspalvelu.api.dto;/*
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * User: ratamaa
 * Date: 22.10.2014
 * Time: 13:24
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchedulerResponse implements Serializable {
    private static final long serialVersionUID = -818989152242206206L;

    private Map<String,Object> commonContext = new HashMap<>();
    private List<ReceiverItem> items = new ArrayList<>();

    public Map<String, Object> getCommonContext() {
        return commonContext;
    }

    public void setCommonContext(Map<String, Object> commonContext) {
        this.commonContext = commonContext;
    }
    
    public void setItems(List<ReceiverItem> items) {
        this.items = items;
    }
    
    public List<ReceiverItem> getItems() {
        return items;
    }
}
