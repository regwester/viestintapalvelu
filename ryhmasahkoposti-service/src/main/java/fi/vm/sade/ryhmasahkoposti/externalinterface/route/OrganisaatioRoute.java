package fi.vm.sade.ryhmasahkoposti.externalinterface.route;

import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.externalinterface.component.OrganizationComponent;

/**
 * Camel ja CXF reititys organisaatiopalveluun
 *  
 * @author vehei1
 *
 */
@Component
public class OrganisaatioRoute extends SpringRouteBuilder {
    private static Logger LOGGER = LoggerFactory.getLogger(OrganisaatioRoute.class);
    private static String ROUTE_GET_ORGANISATION = "direct:getOrganisation";
    private OrganizationComponent getOrganizationComponent; 

    /**
     * Muodostin organisaation hakukomponentin asettamiseksi
     * 
     * @param getOrganizationComponent Organisaation hakukomponentti
     */
    @Autowired
    public OrganisaatioRoute(OrganizationComponent getOrganizationComponent) {
        this.getOrganizationComponent = getOrganizationComponent;
    }
    
    /**
     * Muodostaa organisaatiotietojen hakuun liittyvät reitit organisaatiopalveluun
     * 
     */
    @Override
    public void configure() throws Exception {
        LOGGER.info("Configure route to OrganisaatioResource");
        from(ROUTE_GET_ORGANISATION).bean(getOrganizationComponent);
    }
}
