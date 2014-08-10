package fi.vm.sade.viestintapalvelu.externalinterface.route;

import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.externalinterface.component.HenkiloComponent;

/**
 * Luokka Camel- ja CXF-reitin luomiseksi autentikaatiopalveluun omien tietojen hakemiseksi
 * 
 * @author vehei1
 *
 */
@Component
public class HenkiloRoute extends SpringRouteBuilder {
	private static Logger LOGGER = LoggerFactory.getLogger(OmattiedotRoute.class);
    private static String ROUTE_GET_HENKILO = "direct:getHenkilo";
    private HenkiloComponent henkiloComponent;
    
    /**
     * Muodostin omat tiedot komponentin asettamiseksi
     * 
     * @param currentUserComponent Hae omat tiedot komponentti
     */
    @Autowired
    public HenkiloRoute(HenkiloComponent henkiloComponent) {
        this.henkiloComponent = henkiloComponent;
    }
    
	/**
	 * Muodostaa omien tietojen hakuun liittyvät reitit autentikointipalveluun
	 * 
	 */
	@Override
	public void configure() throws Exception {
	    LOGGER.info("Configure route to OmatTiedotResource");
	    from(ROUTE_GET_HENKILO).bean(henkiloComponent);
	}
}
