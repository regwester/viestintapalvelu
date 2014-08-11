package fi.vm.sade.viestintapalvelu;

import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider;
import com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider;
import fi.vm.sade.viestintapalvelu.feature.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationResourceConfig extends ResourceConfig {

    public ApplicationResourceConfig() {
        packages("fi.vm.sade.viestintapalvelu", "fi.vm.sade.authentication");

        // Register custom filters, providers and features
        register(JacksonFeature.class);

        // Register swagger resources
        register(ApiListingResourceJSON.class);
        register(JerseyApiDeclarationProvider.class);
        register(JerseyResourceListingProvider.class);
    }
}
