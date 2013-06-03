package fi.vm.sade.viestintapalvelu.application;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import fi.vm.sade.viestintapalvelu.domain.address.AddressLabelResource;
import fi.vm.sade.viestintapalvelu.domain.download.DownloadResource;
import fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje.HyvaksymiskirjeResource;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.JalkiohjauskirjeResource;

public class ViestintapalveluModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(DownloadResource.class);
		bind(AddressLabelResource.class);
		bind(JalkiohjauskirjeResource.class);
		bind(HyvaksymiskirjeResource.class);
		bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());

		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");

		serve("/" + Urls.API_PATH + "/*").with(GuiceContainer.class,
				initParameters);
	}

	@Provides
	@Singleton
	ObjectMapper objectMapper() {
		return new ViestintapalveluObjectMapper();
	}

	@Provides
	@Singleton
	JacksonJsonProvider jacksonJsonProvider(ObjectMapper mapper) {
		return new JacksonJsonProvider(mapper);
	}
}
