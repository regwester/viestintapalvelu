package fi.vm.sade.viestintapalvelu;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.vm.sade.valinta.dokumenttipalvelu.resource.DokumenttiResource;

/**
 * 
 * @author Jussi Jartamo
 * 
 *         Rest Client Jerseylla. CXF:sta oli luovuttava koska se kaytti omaa
 *         rest-2.0-api kuvausta mista seurasi konflikteja muiden rest
 *         palikoiden kanssa. Uusin versio CXF:sta ei taas toteuta client
 *         yhteytta talla hetkella.
 */
@Configuration
public class DokumenttipalveluConfiguration {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DokumenttipalveluConfiguration.class);

    @Bean
    public DokumenttiResource getDokumenttiResource(

    @Value("${dokumenttipalvelu-service.rest.url}") String dokumenttipalveluUrl) {
        LOG.info("Dokumenttipalvelu URL:ssa {}", dokumenttipalveluUrl);
        ClientConfig cc = new ClientConfig().register(JacksonFeature.class);
        Client resource = ClientBuilder.newClient(cc);
        return WebResourceFactory.newResource(DokumenttiResource.class, resource.target(dokumenttipalveluUrl));
    }
}
