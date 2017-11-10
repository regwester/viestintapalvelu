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

package fi.vm.sade.viestintapalvelu.common.feature;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class JacksonFeature extends JacksonJaxbJsonProvider {

    public JacksonFeature() {
        super();
        setMapper(new ObjectMapper() {{
            registerModule(new JodaModule());
            registerModule(new Jdk8Module());
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }});
    }

}
