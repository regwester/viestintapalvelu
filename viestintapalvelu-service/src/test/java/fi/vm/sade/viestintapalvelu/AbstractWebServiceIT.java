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
