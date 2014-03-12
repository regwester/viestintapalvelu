package fi.vm.sade.ryhmasahkoposti.externalinterface.common;

import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.LoadBalanceDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringRouteBuilder;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstraktiluokka Camel-reitin luomiseksi
 * 
 */
public abstract class AbstractRouteBuilder extends SpringRouteBuilder {
    private static final int DEFAULT_RETRY_LIMIT = 10;

    @Autowired
    protected ObjectMapperProvider mapperProvider;

    /**
     * Palauttaa reittimäärityksen haluttun palveluun 
     * 
     * @param routeId Pyynnön lähettäjän reitin tunnus
     * @param url Kutsuttavan palvelun URL
     * @param targetDtoType DTO-luokan tyyppi
     * @return Camel-reittimääritys sisältäen prosessin JSON-tietojen muutamiseksi halutuksi DTO-luokaksi  
     */
    protected <T> RouteDefinition getRouteDefinition(String routeId, String url, TypeReference<T> targetDtoType) {
        JacksonJsonProcessor jsonToDtoConverter = new JacksonJsonProcessor(mapperProvider, targetDtoType);
        return from(routeId).setHeader(Exchange.HTTP_METHOD, constant("GET")).to(url).process(jsonToDtoConverter);
    }

    /**
     * Palauttaa reittimäärityksen haluttuun palveluun 
     * 
     * @param routeId Pyynnön lähettäjän reitin tunnus
     * @param url Kutsuttavan palvelun URL
     * @param headerName Header parametrin nimi asetettavaksi sanomalle
     * @param headerValue Header parametrin arvo asetettavaksi sanomalle
     * @param targetDtoType DTO-luokan tyyppi
     * @return Camel-reittimääritys sisältäen prosessin JSON-tietojen muutamiseksi halutuksi DTO-luokaksi 
     */
    protected <T> RouteDefinition getRouteDefinition(String routeId, String url, String headerName,
        Expression headerValue, TypeReference<T> targetDtoType) {
        JacksonJsonProcessor jsonToDtoConverter = new JacksonJsonProcessor(mapperProvider, targetDtoType);
        return from(routeId).setHeader(Exchange.HTTP_METHOD, constant("GET")).setHeader(
            headerName, headerValue).to(url).process(jsonToDtoConverter);
    }

    /**
     * Palauttaa reittimäärityksen haluttuun palveluun 
     * 
     * @param routeId Pyynnön lähettäjän reitin tunnus
     * @param url Kutsuttavan palvelun URL
     * @param headers Kokoelma header parametreja ja arvoja asetettavaksi sanomalle
     * @param targetDtoType DTO-luokan tyyppi
     * @return Camel-reittimääritys sisältäen prosessin JSON-tietojen muutamiseksi halutuksi DTO-luokaksi
     */
    protected <T> RouteDefinition getRouteDefinition(String routeId, String url, HeaderBuilder headers,
        TypeReference<T> targetDtoType) {
        JacksonJsonProcessor jsonToDtoConverter = new JacksonJsonProcessor(mapperProvider, targetDtoType);
        RouteDefinition route = from(routeId).setHeader(Exchange.HTTP_METHOD, constant("GET"));
 
        for (Entry<String, Expression> header : headers.getHeaders().entrySet()) {
            route.setHeader(header.getKey(), header.getValue());
        }
        return route.to(url).process(jsonToDtoConverter);
    }

    /**
     * Lisää reitityksen virhekäsittelyn 
     * 
     * @param route Reitti kutsuttavaan palveluun
     * @return Määrityksen, mitä tehdään virheen sattuessa
     */
    protected LoadBalanceDefinition addRouteErrorHandlers(RouteDefinition route) {
        boolean roundRobin = true;
        boolean inheritErrorHandler = true;
        return route.loadBalance().failover(DEFAULT_RETRY_LIMIT, inheritErrorHandler, roundRobin);
    }

    /**
     * Palauttaa templaten, minkä avulla Camel lähettää sanoman halutulle palvelulle
     * 
     * @return Tamplate, minkä avulla Camel lähettää sanoman halutulle palvelulle
     */
    protected ProducerTemplate getCamelTemplate() {
        return this.getApplicationContext().getBean(ProducerTemplate.class);
    }

    /**
     * Poistaa tyhjät merkit annetusta URL:sta
     * 
     * @param value URL:in arvo
     * @return trimmattu URL
     */
    protected String trim(String value) {
        if (value != null) {
            return value.trim();
        }
        return null;
    }
}
