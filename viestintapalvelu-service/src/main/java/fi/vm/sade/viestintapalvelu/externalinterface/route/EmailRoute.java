package fi.vm.sade.viestintapalvelu.externalinterface.route;

import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.vm.sade.viestintapalvelu.externalinterface.component.EmailComponent;

public class EmailRoute extends SpringRouteBuilder {

    private static Logger LOGGER = LoggerFactory.getLogger(EmailRoute.class);
    private static String ROUTE_SEND_EMAIL = "direct:sendEmail";
    EmailComponent emailComponent;
    
    public EmailRoute(EmailComponent emailComponent) {
        this.emailComponent = emailComponent;
    }
    
    @Override
    public void configure() throws Exception {
        LOGGER.info("Configure route to OmatTiedotResource");
        from(ROUTE_SEND_EMAIL).bean(emailComponent);
    }
}
