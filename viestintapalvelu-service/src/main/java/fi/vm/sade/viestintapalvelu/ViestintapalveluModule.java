package fi.vm.sade.viestintapalvelu;

import javax.validation.Validation;

public class ViestintapalveluModule {

    public void configureServlets() {
        Validation.buildDefaultValidatorFactory().getValidator();
        // bind(MessageResource.class);
        // bind(DownloadResource.class);
        // bind(AddressLabelResource.class);
        // bind(JalkiohjauskirjeResource.class);
        // bind(HyvaksymiskirjeResource.class);
        // bind(Validator.class).toInstance();
        // bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
        //
        // Map<String, String> initParameters = new HashMap<String, String>();
        // initParameters.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
        // initParameters.put(PackagesResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS,
        // CORSFilter.class.getName());
        // serve("/" + Urls.API_PATH + "/*").with(GuiceContainer.class,
        // initParameters);
    }
}
