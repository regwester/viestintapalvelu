package fi.vm.sade.ryhmasahkoposti.externalinterface.route;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.common.AbstractRouteBuilder;

@Component
public class OrganisaatioRoute extends AbstractRouteBuilder {
    private static Logger LOGGER = LoggerFactory.getLogger(OrganisaatioRoute.class);
    private static String ROUTE_GET_ORGANISATION = "direct:getOrganisation"; 

    @Value("${ryhmasahkopostipalvelu.organisaatioService.rest.url}")
    private String organisaatioURI;
    
    /**
     * Muodostaa organisaatiotietojen hakuun liittyvät reitit organisaatiopalveluun
     * 
     */
    @Override
    public void configure() throws Exception {
        // Poistetaan turhat tyhjät URL:sta
        organisaatioURI = trim(organisaatioURI);

        // Henkilöhaku henkilöpalveluunn OID:lla ja saadaan paluuarvona Henkilo-luokan ilmentymä
        TypeReference<OrganisaatioRDTO> organisaatioType = new TypeReference<OrganisaatioRDTO>() {};
        getRouteDefinition(ROUTE_GET_ORGANISATION, organisaatioURI, 
            Exchange.HTTP_PATH, simple("${in.headers.oid}"), organisaatioType);
    }
    
    /**
     * Hakee organissaation tiedot organisaatiopalvelusta
     * 
     * @param oid Organisaation OID-tunnus
     * @return Organisaation tiedot
     */
    public OrganisaatioRDTO getOrganisaatio(String oid) {
        LOGGER.info("[OrganisaatioRoute.getOrganisaatio(" + oid + ")]");
        ProducerTemplate camelTemplate = getCamelTemplate();
        return camelTemplate.requestBodyAndHeader(ROUTE_GET_ORGANISATION, "", "oid", oid, OrganisaatioRDTO.class);
    }
}
