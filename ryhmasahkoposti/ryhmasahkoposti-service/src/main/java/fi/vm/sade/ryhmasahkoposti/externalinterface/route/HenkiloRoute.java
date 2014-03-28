package fi.vm.sade.ryhmasahkoposti.externalinterface.route;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.ryhmasahkoposti.externalinterface.common.AbstractRouteBuilder;

/**
 * Luokka Camel-reitin luomiseksi henkilöpalveluun henkilötietojen hakemiseksi
 * 
 * @author vehei1
 *
 */
@Component
public class HenkiloRoute extends AbstractRouteBuilder {
	private static Logger LOGGER = LoggerFactory.getLogger(HenkiloRoute.class);
    private static String ROUTE_GET_PERSON = "direct:getPerson";

	@Value("${ryhmasahkopostipalvelu.authenticationService.henkilo.rest.url}")
	private String henkiloURI;
    
	/**
	 * Muodostaa henkilötietojen hakuun liittyvät reitit henkilöpalveluun
	 * 
	 */
	@Override
	public void configure() throws Exception {
		// Poistetaan turhat tyhjät URL:sta
		henkiloURI = trim(henkiloURI);

		// Palautettavat tyyppiviittaukset
        TypeReference<Henkilo> henkiloType = new TypeReference<Henkilo>() {};
        
        // Henkilöhaku OID:lla. Palauttaa Henkilo-luokan ilmentymän
        getRouteDefinition(ROUTE_GET_PERSON, henkiloURI, Exchange.HTTP_PATH, simple("${in.headers.oid}"), henkiloType);
	}
	
	/**
	 * Hakee henkilön tiedot henkilöpalvelusta
	 * 
	 * @param oid Henkilön OID-tunnus
	 * @return Henkilön tiedot
	 */
	public Henkilo getHenkilo(String oid) {
	    LOGGER.info("[HenkiloRoute.getHenkilo(" + oid + ")]");
		ProducerTemplate camelTemplate = getCamelTemplate();
		return camelTemplate.requestBodyAndHeader(ROUTE_GET_PERSON, "", "oid", oid, Henkilo.class);
	}
}
