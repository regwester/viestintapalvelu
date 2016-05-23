package fi.vm.sade.ryhmasahkoposti.externalinterface.route;

import fi.vm.sade.ryhmasahkoposti.externalinterface.component.BounceComponent;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BounceRoute extends SpringRouteBuilder {
    private static Logger LOGGER = LoggerFactory.getLogger(BounceRoute.class);
    private static String ROUTE_GET_BOUNCE = "direct:getBounces";
    private BounceComponent bounceComponent;

    @Autowired
    public BounceRoute(BounceComponent bounceComponent) {
        this.bounceComponent = bounceComponent;
    }

    @Override
    public void configure() throws Exception {
        LOGGER.info("Configure route to BounceResource");
        from(ROUTE_GET_BOUNCE).bean(bounceComponent);
    }
}
