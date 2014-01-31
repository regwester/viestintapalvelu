package fi.vm.sade.ryhmasahkoposti.route;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;

@Component
public class HenkiloRoute extends AbstractJsonToDtoRouteBuilder {
	private Logger logger = LoggerFactory.getLogger(HenkiloRoute.class);
	
	@Value("${ryhmasahkopostipalvelu.authenticationService.rest.url}")
	private String henkiloURI;
	private static final String REITTI_HAE_HENKILO = "direct:haeHenkilo";
	
	/**
	 * Konfiguroidaan henkilöhaut henkilöpalveluun
	 * 
	 */
	@Override
	public void configure() throws Exception {
		logger.info("Configure route to HenkiloPalvelu.");

		// Trimmataan parametritiedoista saatu URI
		henkiloURI = trim(henkiloURI);
		
		// Muodostetaan omasta palvelusta henkilöhaku henkilön OID:lla ja saadaan paluuarvona Henkilo-luokan ilmentymä
		TypeReference<Henkilo> henkiloType = new TypeReference<Henkilo>() {};
		fromHttpGetToDtos(REITTI_HAE_HENKILO, henkiloURI, Exchange.HTTP_PATH, simple("${in.headers.oid}"), henkiloType);
	}
	
	/**
	 * Hakee henkilön tiedot henkilöpalvelusta
	 * 
	 * @param oid Henkilön OID-tunnus
	 * @return Henkilön tiedot
	 */
	public Henkilo haeHenkilo(String oid) {
		ProducerTemplate camelTemplate = getCamelTemplate();
		return camelTemplate.requestBodyAndHeader(REITTI_HAE_HENKILO, "", "oid", oid, Henkilo.class);
	}
}
