/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.viestintapalvelu.feature;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.jaxrs.base.JsonMappingExceptionMapper;
import com.fasterxml.jackson.jaxrs.base.JsonParseExceptionMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class JacksonFeature implements Feature {

    private static final ObjectMapper mapper = new ObjectMapper() {
        private static final long serialVersionUID = 1L;

        {
            registerModule(new JodaModule());
            // True
            enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

            // False
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
    };

    private static final JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider() {
        {
            setMapper(mapper);
        }
    };

    @Override
    public boolean configure(FeatureContext context) {
        // Might need to register additional custom ObjectMapperProvider
        context.register(provider);
        context.register(JsonMappingExceptionMapper.class);
        context.register(JsonParseExceptionMapper.class);
        return true;
    }
}
