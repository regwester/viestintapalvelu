package fi.vm.sade.viestintapalvelu.externalinterface.route;

import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.externalinterface.component.OrganizationComponent;

/**
 * Camel ja CXF reititys organisaatiopalveluun
 *  
 * @author vehei1
 *
 */
@Component
public class OrganisaatioRoute extends SpringRouteBuilder {
    private static Logger LOGGER = LoggerFactory.getLogger(OrganisaatioRoute.class);
    private static String ROUTE_GET_ORGANISATION_PARENTS = "direct:getParentOids";
    private OrganizationComponent organizationComponent; 

    /**
     * Muodostin organisaation hakukomponentin asettamiseksi
     * 
     * @param organizationComponent Organisaation hakukomponentti
     */
    @Autowired
    public OrganisaatioRoute(OrganizationComponent organizationComponent) {
        this.organizationComponent = organizationComponent;
    }
    
    /**
     * Muodostaa organisaation isätietojen hakuun liittyvät reitit organisaatiopalveluun
     * 
     */
    @Override
    public void configure() throws Exception {
        LOGGER.info("Configure route to OrganisaatioResource");
        from(ROUTE_GET_ORGANISATION_PARENTS).bean(organizationComponent);
    }
}
