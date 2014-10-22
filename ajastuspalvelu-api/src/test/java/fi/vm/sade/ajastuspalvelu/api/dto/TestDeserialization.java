package fi.vm.sade.ajastuspalvelu.api.dto;
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

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import fi.vm.sade.ajastuspalvelu.api.dto.ReceiverItem;
import fi.vm.sade.ajastuspalvelu.api.dto.SchedulerResponse;
import junit.framework.Assert;
import junit.framework.TestCase;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;

/**
 * User: ratamaa
 * Date: 22.10.2014
 * Time: 13:43
 */
@RunWith(JUnit4.class)
public class TestDeserialization {
    private ObjectMapper mapper;

    @Before
    public void before() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
    }

    @Test
    public void testDeserialization() throws IOException {
        String json = "{" +
            "  \"items\": [{ \"oidType\": \"henkilo\", \"oid\": \"test\" }]" +
            "}";

        SchedulerResponse response = mapper.reader(SchedulerResponse.class).readValue(json);
        assertNotNull(response);
        assertNotNull(response.getItems());
        assertEquals(1, response.getItems().size());
        assertTrue(response.getItems().get(0) instanceof ReceiverItem);
        assertEquals("henkilo", ((ReceiverItem) response.getItems().get(0)).getOidType());
        assertEquals("test", ((ReceiverItem) response.getItems().get(0)).getOid());
    }

}
