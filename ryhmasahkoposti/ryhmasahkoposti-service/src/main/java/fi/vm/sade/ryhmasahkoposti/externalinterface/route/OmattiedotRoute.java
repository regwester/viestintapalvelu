package fi.vm.sade.ryhmasahkoposti.externalinterface.route;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.ryhmasahkoposti.externalinterface.common.AbstractRouteBuilder;

/**
 * Luokka Camel-reitin luomiseksi autentikaatiopalveluun omien tietojen hakemiseksi
 * 
 * @author vehei1
 *
 */
@Component
public class OmattiedotRoute extends AbstractRouteBuilder {
	private static Logger LOGGER = LoggerFactory.getLogger(OmattiedotRoute.class);
    private static String ROUTE_GET_CURRENT_USER = "direct:getCurrentUser";

	@Value("${ryhmasahkopostipalvelu.authenticationService.omattiedot.rest.url}")
	private String omattiedotURI;
    
	/**
	 * Muodostaa omien tietojen hakuun liittyvät reitit autentikointipalveluun
	 * 
	 */
	@Override
	public void configure() throws Exception {
		// Poistetaan turhat tyhjät URL:sta
		omattiedotURI = trim(omattiedotURI);

		// Palautettavat tyyppiviittaukset
        TypeReference<Henkilo> henkiloType = new TypeReference<Henkilo>() {};
        
        // Nykyisen käyttäjän tietojen haku. Palauttaa Henkilö-luokan ilmentymän.
        getRouteDefinition(ROUTE_GET_CURRENT_USER, omattiedotURI, henkiloType);
	}
	

	/**
     * Hakee käyttäjän henkilötiedot 
     * 
     * @return Käyttäjän henkilötiedot
     */
    public Henkilo getCurrenUser() {
        LOGGER.info("[OmattiedotRoute.getCurrentUser()]");
        Henkilo henkilo = null;
        try {
            ProducerTemplate camelTemplate = getCamelTemplate();
            henkilo = camelTemplate.requestBodyAndHeader(ROUTE_GET_CURRENT_USER, "", "", "", Henkilo.class);
        } catch (Exception e) {
           LOGGER.error("Liittymä virhe authetication-service");
           throw new RuntimeException("Omattiedot haku epäonnistui", e);
        } 
        
        return henkilo;
	}
}
