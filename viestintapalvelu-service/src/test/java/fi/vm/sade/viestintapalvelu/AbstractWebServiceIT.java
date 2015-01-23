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
package fi.vm.sade.viestintapalvelu;

import fi.vm.sade.viestintapalvelu.feature.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

import javax.ws.rs.core.Application;

public abstract class AbstractWebServiceIT extends JerseyTest {

    @Override
    protected Application configure() {
        /** Configure TestProperties */
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        /** Load Custom ResourceConfig */
        ResourceConfig rc = new ResourceConfig()
            .register(JacksonFeature.class)
            .packages("fi.vm.sade.viestintapalvelu")
            .property("contextConfigLocation", "classpath:test-application-context.xml");
        return configure(rc);
    }

    protected abstract ResourceConfig configure(ResourceConfig rc);

    protected abstract String getResourcePath();
}
