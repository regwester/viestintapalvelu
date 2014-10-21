package fi.vm.sade.ryhmasahkoposti;

import org.glassfish.jersey.server.ResourceConfig;

import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider;
import com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider;

import fi.vm.sade.ryhmasahkoposti.feature.JacksonFeature;
import fi.vm.sade.ryhmasahkoposti.resource.filter.CORSFilter;

public class ApplicationResourceConfig extends ResourceConfig {

    /*@Resource
    WebExceptionMapper exceptionMapper;
    */
    public ApplicationResourceConfig() {

        // Register resources using package scan
        packages("fi.vm.sade.ryhmasahkoposti");
        packages("fi.vm.sade.generic");

        // Register custom filters, providers and features
        register(JacksonFeature.class);
        //register(CorsFilter.class);
        register(CORSFilter.class);
        //register exception handler
        //register(exceptionMapper);
        register(WebExceptionMapper.class);
        // Register swagger resources
        register(ApiListingResourceJSON.class);
        register(JerseyApiDeclarationProvider.class);
        register(JerseyResourceListingProvider.class);
    }
}
