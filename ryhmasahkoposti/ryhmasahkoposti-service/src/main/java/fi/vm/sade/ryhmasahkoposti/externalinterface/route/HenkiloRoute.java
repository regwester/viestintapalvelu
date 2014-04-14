package fi.vm.sade.ryhmasahkoposti.externalinterface.route;

import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.externalinterface.component.PersonComponent;

/**
 * Luokka Camel- ja CXF-reitin luomiseksi henkilöpalveluun henkilötietojen hakemiseksi
 * 
 * @author vehei1
 *
 */
@Component
public class HenkiloRoute extends SpringRouteBuilder {
	private static Logger LOGGER = LoggerFactory.getLogger(HenkiloRoute.class);
    private static String ROUTE_GET_PERSON = "direct:getPerson";
    private PersonComponent personComponent;
    
    /**
     * Muodostin henkilöhakukomponentti asettamista varten
     * 
     * @param personComponent Hae henkilön tiedot komponentti
     */
    @Autowired
    public HenkiloRoute(PersonComponent personComponent) {
        this.personComponent = personComponent;
    }
    
	/**
	 * Muodostaa henkilötietojen hakuun liittyvät reitit henkilöpalveluun
	 * 
	 */
	@Override
	public void configure() throws Exception {
	    LOGGER.info("Configure route to HenkiloResource");
	    from(ROUTE_GET_PERSON).bean(personComponent);
	}
}
