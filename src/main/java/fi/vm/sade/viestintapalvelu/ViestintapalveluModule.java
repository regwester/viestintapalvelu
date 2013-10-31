package fi.vm.sade.viestintapalvelu;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.validation.Validation;
import javax.validation.Validator;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import fi.vm.sade.viestintapalvelu.address.AddressLabelResource;
import fi.vm.sade.viestintapalvelu.download.DownloadResource;
import fi.vm.sade.viestintapalvelu.hyvaksymiskirje.HyvaksymiskirjeResource;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.JalkiohjauskirjeResource;
import fi.vm.sade.viestintapalvelu.message.MessageResource;

public class ViestintapalveluModule extends JerseyServletModule {
    @Override
    protected void configureServlets() {
        bind(MessageResource.class);
        bind(DownloadResource.class);
        bind(AddressLabelResource.class);
        bind(JalkiohjauskirjeResource.class);
        bind(HyvaksymiskirjeResource.class);
        bind(Validator.class).toInstance(Validation.buildDefaultValidatorFactory().getValidator());
        bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());

        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
        initParameters.put(PackagesResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, CORSFilter.class.getName());
        serve("/" + Urls.API_PATH + "/*").with(GuiceContainer.class, initParameters);
    }
}
